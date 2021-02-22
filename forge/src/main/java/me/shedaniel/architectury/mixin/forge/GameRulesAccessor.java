package me.shedaniel.architectury.mixin.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(GameRules.class)
public interface GameRulesAccessor {
    /**
     * Spliting simple classes because mixin can't handle refmap using the same name
     */
    @Mixin(GameRules.BooleanValue.class)
    interface BooleanValue {
        @Invoker("create")
        static GameRules.Type<GameRules.BooleanValue> invokeCreateArchitectury(boolean value, BiConsumer<MinecraftServer, GameRules.BooleanValue> biConsumer) {
            throw new AssertionError();
        }
    }
    
    @Mixin(GameRules.BooleanValue.class)
    interface BooleanValueSimple {
        @Invoker("create")
        static GameRules.Type<GameRules.BooleanValue> invokeCreateArchitectury(boolean value) {
            throw new AssertionError();
        }
    }
    
    @Mixin(GameRules.IntegerValue.class)
    interface IntegerValue {
        @Invoker("create")
        static GameRules.Type<GameRules.IntegerValue> invokeCreateArchitectury(int value, BiConsumer<MinecraftServer, GameRules.IntegerValue> biConsumer) {
            throw new AssertionError();
        }
    }
    
    @Mixin(GameRules.IntegerValue.class)
    interface IntegerValueSimple {
        @Invoker("create")
        static GameRules.Type<GameRules.IntegerValue> invokeCreateArchitectury(int value) {
            throw new AssertionError();
        }
    }
}
