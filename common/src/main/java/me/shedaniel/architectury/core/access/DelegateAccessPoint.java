package me.shedaniel.architectury.core.access;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DelegateAccessPoint<T, SELF extends AccessPoint<T, SELF>> extends AccessPoint<T, SELF> {
    AccessPoint<T, ?> getParent();
    
    @ApiStatus.Internal
    default AccessPoint<T, SELF> castedParent() {
        return (AccessPoint<T, SELF>) getParent();
    }
    
    @Override
    default T get() {
        return getParent().get();
    }
    
    @Override
    default SELF addListener(Consumer<SELF> listener) {
        return castedParent().addListener(listener);
    }
    
    @Override
    default SELF add(T provider) {
        return castedParent().add(provider);
    }
    
    @Override
    default SELF addAll(Collection<T> providers) {
        return castedParent().addAll(providers);
    }
    
    @Override
    default SELF dependsOn(SELF access) {
        return castedParent().dependsOn(access);
    }
    
    @Override
    default <E> SELF dependsOn(AccessPoint<E, ?> accessPoint, Function<E, T> function) {
        return castedParent().dependsOn(accessPoint, function);
    }
}
