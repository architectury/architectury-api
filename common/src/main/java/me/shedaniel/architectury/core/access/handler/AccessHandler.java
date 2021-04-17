package me.shedaniel.architectury.core.access.handler;

import me.shedaniel.architectury.core.access.AccessPoint;

public interface AccessHandler {
    <T> T get(AccessPoint<T, ?> accessPoint);
}
