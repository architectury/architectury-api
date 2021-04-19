package me.shedaniel.architectury.impl.access;

import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.LevelAccess;
import me.shedaniel.architectury.core.access.specific.LevelAccessPoint;
import me.shedaniel.architectury.core.access.specific.ProvidedAccess;
import me.shedaniel.architectury.core.access.specific.ProvidedAccessPoint;
import net.minecraft.world.level.Level;

public class ProvidedAccessPointImpl<T, SELF extends ProvidedAccessPointImpl<T, SELF>> implements ProvidedAccessPoint<T, SELF> {
    private final AccessPoint<ProvidedAccess<T>, ?> parent;
    
    public ProvidedAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> object -> {
            return processIterable(ts, object);
        });
    }
    
    private T processIterable(Iterable<ProvidedAccess<T>> iterable, Object object) {
        for (ProvidedAccess<T> accessor : iterable) {
            T t = accessor.get(object);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<ProvidedAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T get(Object object) {
        return get().get(object);
    }
}
