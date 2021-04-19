package me.shedaniel.architectury.impl.access;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import me.shedaniel.architectury.core.access.AccessPoint;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public final class AccessPointImpl<T, SELF extends AccessPointImpl<T, SELF>> implements AccessPoint<T, SELF> {
    protected final Function<Collection<T>, T> compiler;
    protected List<Object> providers = new ArrayList<>();
    @Nullable
    protected Consumer<SELF> listener;
    protected T compiledFunction;
    
    public AccessPointImpl(Function<Collection<T>, T> compiler) {
        this.compiler = compiler;
        this.recompile();
    }
    
    protected void recompile() {
        this.compiledFunction = compile(this.providers, this.compiler);
        
        if (this.listener != null) {
            this.listener.accept((SELF) this);
        }
    }
    
    @Override
    public T get() {
        return this.compiledFunction;
    }
    
    /**
     * @return an invoker that contains all functions except those from the passed registry (the supplier should be called each time for it to be updated
     */
    public Supplier<T> getExcluding(Collection<AccessPointImpl<?, ?>> accesses) {
        AtomicReference<T> reference = new AtomicReference<>();
        this.addListener(a -> {
            T val = compile(a.getWithout(accesses), a.compiler);
            reference.set(val);
        });
        return reference::get;
    }
    
    @Override
    public SELF dependsOn(SELF access) {
        return dependsOn(access, Function.identity(), true);
    }
    
    @Override
    public <E> SELF dependsOn(AccessPoint<E, ?> accessPoint, Function<E, T> function) {
        return this.dependsOn((AccessPointImpl<E, ?>) accessPoint, function, true);
    }
    
    @Override
    public SELF addListener(Consumer<SELF> listener) {
        if (this.listener == null) {
            this.listener = listener;
        } else {
            Consumer<SELF> old = this.listener;
            this.listener = access -> {
                old.accept(access);
                listener.accept(access);
            };
        }
        return (SELF) this;
    }
    
    public SELF before(T func) {
        this.providers.add(0, func);
        this.recompile();
        return (SELF) this;
    }
    
    @Override
    public SELF add(T provider) {
        this.providers.add(provider);
        this.recompile();
        return (SELF) this;
    }
    
    @Override
    public SELF addAll(Collection<T> providers) {
        this.providers.addAll(providers);
        this.recompile();
        return (SELF) this;
    }
    
    <Entries> Iterable<Object> getWithout(Collection<AccessPointImpl<?, ?>> accesses) {
        return Iterables.transform(Iterables.filter(this.providers, o -> !(o instanceof Func && accesses.contains(((Func<?, ?>) o).dep))), delegate -> {
            if (delegate instanceof Func) {
                // our own delegate
                Func<Entries, T> current = (Func<Entries, T>) delegate;
                Func<Entries, T> copied = new Func<>(current.dep, current.mapping);
                
                HashSet<AccessPointImpl<?, ?>> newAccesses = new HashSet<>(accesses);
                newAccesses.add(this);
                Iterable<Object> val = current.dep.getWithout(newAccesses);
                copied.inputs = val;
                copied.delegate = current.mapping.apply(compile(val, current.dep.compiler));
                return copied;
            }
            return delegate;
        });
    }
    
    private <E> SELF dependsOn(AccessPointImpl<E, ?> access, Function<E, T> function, boolean end) {
        Func<E, T> dependency = new Func<>(access, function);
        if (end) this.providers.add(dependency);
        else this.providers.add(0, dependency);
        access.addListener(a -> {
            HashSet<AccessPointImpl<?, ?>> accesses = new HashSet<>();
            Iterable<Object> inputs = a.getWithout(accesses);
            if (dependency.inputs == null || !Iterables.elementsEqual(inputs, dependency.inputs)) {
                dependency.inputs = inputs;
                dependency.delegate = function.apply(compile(inputs, a.compiler));
                this.recompile();
            }
        });
        return (SELF) this;
    }
    
    private static final class Func<Entries, Target> {
        private final AccessPointImpl<Entries, ?> dep;
        private final Function<Entries, Target> mapping;
        private Target delegate;
        private Iterable<Object> inputs;
        
        private Func(AccessPointImpl<Entries, ?> dep, Function<Entries, Target> mapping) {
            this.dep = dep;
            this.mapping = mapping;
        }
        
        public Target getDelegate() {
            return delegate;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Func)) {
                return false;
            }
            
            Func<?, ?> func = (Func<?, ?>) o;
            
            if (!Objects.equals(this.dep, func.dep)) {
                return false;
            }
            if (!Objects.equals(this.mapping, func.mapping)) {
                return false;
            }
            return Iterables.elementsEqual(this.inputs, func.inputs);
        }
        
        @Override
        public int hashCode() {
            int result = this.dep != null ? this.dep.hashCode() : 0;
            result = 31 * result + (this.mapping != null ? this.mapping.hashCode() : 0);
            result = 31 * result + (this.delegate != null ? this.delegate.hashCode() : 0);
            return result;
        }
    }
    
    private static <A> A compile(Iterable<Object> vals, Function<Collection<A>, A> compiler) {
        return compiler.apply(StreamSupport.stream(vals.spliterator(), false)
                .<A>map(AccessPointImpl::get)
                .collect(ImmutableList.toImmutableList()));
    }
    
    private static <T> T get(Object o) {
        if (o instanceof AccessPointImpl.Func) {
            return ((Func<?, T>) o).getDelegate();
        } else if (o instanceof Supplier) {
            return ((Supplier<T>) o).get();
        }
        return (T) o;
    }
}
