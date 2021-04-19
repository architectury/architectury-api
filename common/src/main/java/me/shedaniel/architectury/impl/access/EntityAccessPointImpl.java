package me.shedaniel.architectury.impl.access;

import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.EntityAccess;
import me.shedaniel.architectury.core.access.specific.EntityAccessPoint;
import net.minecraft.world.entity.Entity;

public class EntityAccessPointImpl<T, SELF extends EntityAccessPointImpl<T, SELF>> implements EntityAccessPoint<T, SELF> {
    private final AccessPoint<EntityAccess<T>, ?> parent;
    
    public EntityAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> entity -> {
            return processIterable(ts, entity);
        });
    }
    
    private T processIterable(Iterable<EntityAccess<T>> iterable, Entity entity) {
        for (EntityAccess<T> accessor : iterable) {
            T t = accessor.getByEntity(entity);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<EntityAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T getByEntity(Entity entity) {
        return get().getByEntity(entity);
    }
}
