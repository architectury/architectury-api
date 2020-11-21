package me.shedaniel.architectury.hooks.forge;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class FluidStackHooksImpl {
    public static ITextComponent getName(FluidStack stack) {
        return stack.getName();
    }
    
    public static String getTranslationKey(FluidStack stack) {
        return stack.getTranslationKey();
    }
    
    public static FluidStack read(PacketBuffer buf) {
        net.minecraftforge.fluids.FluidStack stack = net.minecraftforge.fluids.FluidStack.readFromPacket(buf);
        return FluidStack.create(stack.getFluid().delegate, Fraction.ofWhole(stack.getAmount()), stack.getTag());
    }
    
    public static void write(FluidStack stack, PacketBuffer buf) {
        stack.write(buf);
    }
    
    public static FluidStack read(CompoundNBT tag) {
        net.minecraftforge.fluids.FluidStack stack = net.minecraftforge.fluids.FluidStack.loadFluidStackFromNBT(tag);
        return FluidStack.create(stack.getFluid().delegate, Fraction.ofWhole(stack.getAmount()), stack.getTag());
    }
    
    public static CompoundNBT write(FluidStack stack, CompoundNBT tag) {
        return stack.write(tag);
    }
    
    public static Fraction bucketAmount() {
        return Fraction.ofWhole(1000);
    }
}
