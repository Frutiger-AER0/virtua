package net.anemoia.virtua.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.anemoia.virtua.client.model.ClockFinLargeModel;
import net.anemoia.virtua.client.renderer.entity.ClockFinRenderer;
import net.anemoia.virtua.core.data.server.ModDatapackBuiltinEntriesProvider;
import net.anemoia.virtua.core.data.server.modifiers.ModChunkGeneratorModifierProvider;
import net.anemoia.virtua.core.other.ModModelLayers;
import net.anemoia.virtua.core.registry.*;
import net.anemoia.virtua.core.registry.helper.ModBlockSubRegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;

@Mod(value = Virtua.MOD_ID)
@EventBusSubscriber(modid = Virtua.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Virtua {
    public static final String MOD_ID = "virtua";
    public static final RegistryHelper REGISTRY_HELPER = RegistryHelper.create(MOD_ID, helper -> helper.putSubHelper(ForgeRegistries.BLOCKS, new ModBlockSubRegistryHelper(helper)));

    public Virtua() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        MinecraftForge.EVENT_BUS.register(this);

        REGISTRY_HELPER.register(bus);
        ModFeatures.FEATURES.register(bus);
        ModPlacementModifierTypes.PLACEMENT_MODIFIER_TYPES.register(bus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModEffects.MOB_EFFECTS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::dataSetup);
        bus.addListener(this::addCreative);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(this::registerLayerDefinitions);
            bus.addListener(this::registerRenderers);
        });

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();

        ModDatapackBuiltinEntriesProvider datapackEntries = new ModDatapackBuiltinEntriesProvider(output, provider);
        generator.addProvider(server, datapackEntries);
        provider = datapackEntries.getRegistryProvider();

        generator.addProvider(server, new ModChunkGeneratorModifierProvider(output, provider));
    }

    @OnlyIn(Dist.CLIENT)
    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CLOCKFIN_LARGE, ClockFinLargeModel::createBodyLayer);
        // Register other model layers here
    }

    @OnlyIn(Dist.CLIENT)
    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.CLOCK_FIN.get(), ClockFinRenderer::new);
        // Register entity renderers here
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            // Add Distilled Time item to the Ingredients tab
            event.accept(ModItems.DISTILLED_TIME);
            event.accept(ModItems.TIME_BOTTLE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            
        }
    }
}
