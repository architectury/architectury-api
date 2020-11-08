/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.hooks;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class FluidStackHooks {
    private FluidStackHooks() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static Component getName(FluidStack stack) {
        return IMPL.getName(stack);
    }
    
    public static String getTranslationKey(FluidStack stack) {
        return IMPL.getTranslationKey(stack);
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    public static FluidStack read(FriendlyByteBuf buf) {
        return IMPL.read(buf);
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        IMPL.write(stack, buf);
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    public static FluidStack read(CompoundTag tag) {
        return IMPL.read(tag);
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        return IMPL.write(stack, tag);
    }
    
    /**
     * Platform-specific bucket amount.
     * Forge: 1000
     * Fabric: 1
     */
    public static Fraction bucketAmount() {
        return IMPL.bucketAmount();
    }
    
    public interface Impl {
        Fraction bucketAmount();
        
        Component getName(FluidStack stack);
        
        String getTranslationKey(FluidStack stack);
        
        FluidStack read(FriendlyByteBuf buf);
        
        void write(FluidStack stack, FriendlyByteBuf buf);
        
        FluidStack read(CompoundTag tag);
        
        CompoundTag write(FluidStack stack, CompoundTag tag);
    }
    
    static {
        ArchitecturyPopulator.populate();
    }
}
