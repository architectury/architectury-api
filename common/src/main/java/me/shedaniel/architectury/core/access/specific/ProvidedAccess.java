package me.shedaniel.architectury.core.access.specific;

@FunctionalInterface
public interface ProvidedAccess<T> {
    T get(Object object);
}
