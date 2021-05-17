package me.shedaniel.architectury.test.entity;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public class TestEntity extends AbstractMinecart {
    
    public static final EntityType<TestEntity> TYPE = EntityType.Builder.of(TestEntity::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("test_entity");
    
    public TestEntity(EntityType<? extends AbstractMinecart> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkManager.createEntitySpawnPacket(this);
    }
}
