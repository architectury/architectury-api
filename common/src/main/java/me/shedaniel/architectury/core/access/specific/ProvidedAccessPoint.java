package me.shedaniel.architectury.core.access.specific;

import me.shedaniel.architectury.core.access.DelegateAccessPoint;
import me.shedaniel.architectury.impl.access.ProvidedAccessPointImpl;

public interface ProvidedAccessPoint<T, SELF extends ProvidedAccessPoint<T, SELF>> extends DelegateAccessPoint<ProvidedAccess<T>, SELF>, ProvidedAccess<T> {
    static <T> ProvidedAccessPoint<T, ?> create() {
        return new ProvidedAccessPointImpl<>();
    }
}
