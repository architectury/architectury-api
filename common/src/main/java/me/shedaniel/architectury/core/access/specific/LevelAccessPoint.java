package me.shedaniel.architectury.core.access.specific;

import me.shedaniel.architectury.core.access.DelegateAccessPoint;
import me.shedaniel.architectury.impl.LevelAccessPointImpl;

public interface LevelAccessPoint<T, SELF extends LevelAccessPoint<T, SELF>> extends DelegateAccessPoint<LevelAccess<T>, SELF>, LevelAccess<T> {
    static <T> LevelAccessPoint<T, ?> create() {
        return new LevelAccessPointImpl<>();
    }
}
