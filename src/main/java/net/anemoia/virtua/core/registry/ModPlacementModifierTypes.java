package net.anemoia.virtua.core.registry;

import net.anemoia.virtua.common.levelgen.placement.SquarePlacement;
import net.anemoia.virtua.core.Virtua;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModPlacementModifierTypes {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Virtua.MOD_ID);

    public static final RegistryObject<PlacementModifierType<SquarePlacement>> SQUARE_CENTER = PLACEMENT_MODIFIER_TYPES.register("square_center", () -> () -> SquarePlacement.CODEC);
}
