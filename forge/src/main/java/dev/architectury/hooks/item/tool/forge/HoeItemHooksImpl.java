package dev.architectury.hooks.item.tool.forge;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.world.BlockEvent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class HoeItemHooksImpl {
    public static void addTillable(Block input, Predicate<UseOnContext> predicate, Consumer<UseOnContext> action, Function<UseOnContext, BlockState> function) {
        MinecraftForge.EVENT_BUS.<BlockEvent.BlockToolModificationEvent>addListener(event -> {
            UseOnContext context = event.getContext();
            if (ToolActions.HOE_TILL == event.getToolAction() && context.getItemInHand().canPerformAction(ToolActions.HOE_TILL)
                    && event.getState().is(input) && predicate.test(context)) {
                if (!event.isSimulated()) {
                    action.accept(context);
                }
                
                event.setFinalState(function.apply(context));
            }
        });
    }
}
