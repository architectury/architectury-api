package me.shedaniel.architectury.hooks.forge;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;

public final class FluidStackHooksForge {
    private FluidStackHooksForge() {}
    
    public static FluidStack fromForge(net.minecraftforge.fluids.FluidStack stack) {
        return FluidStack.create(stack.getFluid().delegate, Fraction.ofWhole(stack.getAmount()), stack.getTag());
    }
    
    public static net.minecraftforge.fluids.FluidStack toForge(FluidStack stack) {
        return new net.minecraftforge.fluids.FluidStack(stack.getRawFluid(), stack.getAmount().intValue(), stack.getTag());
    }
}
