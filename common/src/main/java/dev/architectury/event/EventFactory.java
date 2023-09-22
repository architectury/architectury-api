/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.event;

import com.google.common.reflect.AbstractInvocationHandler;
import dev.architectury.annotations.ForgeEvent;
import dev.architectury.annotations.ForgeEventCancellable;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public final class EventFactory {
    private EventFactory() {
    }
    
    public static <T> Event<T> of(Function<List<T>, T> function) {
        return new EventImpl<>(function);
    }
    
    @SafeVarargs
    public static <T> Event<T> createLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createLoop((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    private static <T, R> R invokeMethod(T listener, Method method, Object[] args) throws Throwable {
        return (R) MethodHandles.lookup().unreflect(method)
                .bindTo(listener).invokeWithArguments(args);
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createLoop(Class<T> clazz) {
        return of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    invokeMethod(listener, method, args);
                }
                return null;
            }
        }));
    }
    
    @SafeVarargs
    public static <T> Event<T> createEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createEventResult((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createEventResult(Class<T> clazz) {
        return of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (EventResult) Objects.requireNonNull(invokeMethod(listener, method, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
    }
    
    @SafeVarargs
    public static <T> Event<T> createCompoundEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createCompoundEventResult((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createCompoundEventResult(Class<T> clazz) {
        return of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (CompoundEventResult) Objects.requireNonNull(invokeMethod(listener, method, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return CompoundEventResult.pass();
            }
        }));
    }
    
    @SafeVarargs
    public static <T> Event<Consumer<T>> createConsumerLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createConsumerLoop((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<Consumer<T>> createConsumerLoop(Class<T> clazz) {
        Event<Consumer<T>> event = of(listeners -> (Consumer<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{Consumer.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    invokeMethod(listener, method, args);
                }
                return null;
            }
        }));
        Class<?> superClass = clazz;
        do {
            if (superClass.isAnnotationPresent(ForgeEvent.class)) {
                return attachToForge(event);
            }
            superClass = superClass.getSuperclass();
        } while (superClass != null);
        return event;
    }
    
    @SafeVarargs
    public static <T> Event<EventActor<T>> createEventActorLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createEventActorLoop((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<EventActor<T>> createEventActorLoop(Class<T> clazz) {
        Event<EventActor<T>> event = of(listeners -> (EventActor<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{EventActor.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (EventResult) invokeMethod(listener, method, args);
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
        Class<?> superClass = clazz;
        do {
            
            if (superClass.isAnnotationPresent(ForgeEventCancellable.class)) {
                return attachToForgeEventActorCancellable(event);
            }
            superClass = superClass.getSuperclass();
        } while (superClass != null);
        superClass = clazz;
        do {
            
            if (superClass.isAnnotationPresent(ForgeEvent.class)) {
                return attachToForgeEventActor(event);
            }
            superClass = superClass.getSuperclass();
        } while (superClass != null);
        return event;
    }
    
    @ExpectPlatform
    @ApiStatus.Internal
    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActor(Event<EventActor<T>> event) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActorCancellable(Event<EventActor<T>> event) {
        throw new AssertionError();
    }
    
    private static class EventImpl<T> implements Event<T> {
        private final Function<List<T>, T> function;
        private final Map<EventPriority, IndividualInvoker<T>> invokers;
        private List<T> listeners;
        private final Invoker<T> overallInvoker;
        private final T emptyInvoker;
        
        public EventImpl(Function<List<T>, T> function) {
            this.function = function;
            this.invokers = new EnumMap<>(EventPriority.class);
            this.invokers.put(EventPriority.NORMAL, new IndividualInvoker<>(this.function));
            this.overallInvoker = new Invoker<>(this.function, this.listeners);
            this.buildListeners();
            this.emptyInvoker = this.function.apply(Collections.emptyList());
        }
        
        private void buildListeners() {
            if (this.invokers.size() == 1) {
                this.listeners = this.invokers.values().iterator().next().listeners;
            } else {
                this.listeners = new ArrayList<>();
                for (var invoker : this.invokers.values()) {
                    this.listeners.addAll(invoker.listeners);
                }
            }
            this.overallInvoker.listeners = this.listeners;
            this.overallInvoker.invoker = null;
        }
        
        @Override
        public T invoker() {
            return this.overallInvoker.invoker();
        }
        
        @Override
        public T invoker(EventPriority priority) {
            IndividualInvoker<T> invoker = this.invokers.get(priority);
            
            if (invoker != null) {
                return invoker.invoker();
            } else {
                return emptyInvoker;
            }
        }
        
        @Override
        public void register(T listener) {
            this.register(EventPriority.NORMAL, listener);
        }
        
        @Override
        public void register(EventPriority priority, T listener) {
            this.getOrCreatePriority(priority).register(listener);
            this.buildListeners();
        }
        
        @Override
        public void unregister(T listener) {
            for (var invoker : this.invokers.values()) {
                invoker.unregister(listener);
            }
            this.buildListeners();
        }
        
        @Override
        public boolean isRegistered(T listener) {
            return overallInvoker.listeners.contains(listener);
        }
        
        private IndividualInvoker<T> getOrCreatePriority(EventPriority priority) {
            IndividualInvoker<T> invoker = this.invokers.get(priority);
            
            if (invoker == null) {
                invoker = new IndividualInvoker<>(this.function);
                this.invokers.put(priority, invoker);
            }
            
            return invoker;
        }
        
        @Override
        public void clearListeners() {
            this.invokers.put(EventPriority.NORMAL, new IndividualInvoker<>(this.function));
            this.buildListeners();
        }
    }
    
    private static class Invoker<T> {
        protected final Function<List<T>, T> function;
        protected T invoker = null;
        protected List<T> listeners;
        
        public Invoker(Function<List<T>, T> function) {
            this.function = function;
            this.listeners = new ArrayList<>();
        }
        
        public Invoker(Function<List<T>, T> function, List<T> listeners) {
            this.function = function;
            this.listeners = listeners;
        }
        
        public T invoker() {
            if (invoker == null) {
                update();
            }
            return invoker;
        }
        
        public void update() {
            if (listeners.size() == 1) {
                invoker = listeners.get(0);
            } else {
                invoker = function.apply(listeners);
            }
        }
    }
    
    private static class IndividualInvoker<T> extends Invoker<T> {
        public IndividualInvoker(Function<List<T>, T> function) {
            super(function);
        }
        
        public IndividualInvoker(Function<List<T>, T> function, ArrayList<T> listeners) {
            super(function, listeners);
        }
        
        public void register(T listener) {
            listeners.add(listener);
            invoker = null;
        }
        
        public void unregister(T listener) {
            listeners.remove(listener);
            if (listeners instanceof ArrayList<T> arrayList) {
                arrayList.trimToSize();
            }
            invoker = null;
        }
        
        public void clearListeners() {
            listeners.clear();
            if (listeners instanceof ArrayList<T> arrayList) {
                arrayList.trimToSize();
            }
            invoker = null;
        }
    }
}
