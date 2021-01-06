package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ExpectPlatform;
import net.minecraft.advancements.CriterionTrigger;

public final class CriteriaTriggersRegistry {
    private CriteriaTriggersRegistry() {}
    
    /**
     * Invokes {@link net.minecraft.advancements.CriteriaTriggers#register(CriterionTrigger)}.
     *
     * @param trigger The trigger to register
     * @param <T>     The type of trigger
     * @return The trigger registered
     */
    @ExpectPlatform
    public static <T extends CriterionTrigger<?>> T register(T trigger) {
        throw new AssertionError();
    }
}
