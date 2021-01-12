package me.shedaniel.architectury.item.fabric;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class ArchitecturyItemPropertiesFabric extends FabricItemSettings {
    public boolean canRepair;
    public Supplier<Callable<BlockEntityWithoutLevelRenderer>> ister;

    public ArchitecturyItemPropertiesFabric setISTER(Supplier<Callable<BlockEntityWithoutLevelRenderer>> ister) {
        this.ister = ister;
        return this;
    }
}
