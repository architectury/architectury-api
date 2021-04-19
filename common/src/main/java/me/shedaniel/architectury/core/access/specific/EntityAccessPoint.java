package me.shedaniel.architectury.core.access.specific;

import me.shedaniel.architectury.core.access.DelegateAccessPoint;
import me.shedaniel.architectury.impl.access.EntityAccessPointImpl;

public interface EntityAccessPoint<T, SELF extends EntityAccessPoint<T, SELF>> extends DelegateAccessPoint<EntityAccess<T>, SELF>, EntityAccess<T> {
    static <T> EntityAccessPoint<T, ?> create() {
        return new EntityAccessPointImpl<>();
    }
}
