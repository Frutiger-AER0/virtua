package net.anemoia.virtua.core.registry;

import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import net.anemoia.virtua.common.entity.ClockFin;
import net.anemoia.virtua.core.Virtua;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = Virtua.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static final EntitySubRegistryHelper HELPER = Virtua.REGISTRY_HELPER.getEntitySubHelper();

    public static final RegistryObject<EntityType<ClockFin>> CLOCK_FIN = HELPER.createEntity("clock_fin", ClockFin::new, ClockFin::new, MobCategory.WATER_AMBIENT, 0.3F, 0.3F);

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CLOCK_FIN.get(), ClockFin.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(CLOCK_FIN.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ClockFin::checkClockFinSpawnRules, Operation.AND);
    }
}
