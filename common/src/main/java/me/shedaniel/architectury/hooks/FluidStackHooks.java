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

import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class FluidStackHooks {
    private FluidStackHooks() {}
    
    @ExpectPlatform
    public static Component getName(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static String getTranslationKey(FluidStack stack) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    @ExpectPlatform
    public static FluidStack read(FriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    @ExpectPlatform
    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    @ExpectPlatform
    public static FluidStack read(CompoundTag tag) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    @ExpectPlatform
    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific bucket amount.
     * Forge: 1000
     * Fabric: 1
     */
    @ExpectPlatform
    public static Fraction bucketAmount() {
        throw new AssertionError();
    }
}
