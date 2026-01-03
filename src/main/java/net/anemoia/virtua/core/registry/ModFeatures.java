package net.anemoia.virtua.core.registry;

import com.google.common.collect.Lists;
import net.anemoia.virtua.common.levelgen.feature.StonePatchFeature;
import net.anemoia.virtua.common.levelgen.feature.configurations.LargeDiskConfiguration;
import net.anemoia.virtua.core.Virtua;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@EventBusSubscriber(modid = Virtua.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Virtua.MOD_ID);
    public static final RegistryObject<Feature<LargeDiskConfiguration>> STONE_PATCH = FEATURES.register("stone_patch", () -> new StonePatchFeature(LargeDiskConfiguration.CODEC));

    public static final class ModConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_PATCH = createKey("stone_patch");

        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
            register(context, STONE_PATCH, ModFeatures.STONE_PATCH.get(), new LargeDiskConfiguration(Blocks.STONE.defaultBlockState(), UniformInt.of(0, 1), 1, Lists.newArrayList(ModBlocks.SAND_OF_TIME.get().defaultBlockState(), Blocks.SANDSTONE.defaultBlockState())));
        }

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Virtua.MOD_ID, name));
        }

        public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
            context.register(key, new ConfiguredFeature<>(feature, config));
        }
    }

    public static final class ModPlacedFeatures {
        public static final ResourceKey<PlacedFeature> STONE_PATCH = createKey("stone_patch");

        public static void bootstrap(BootstapContext<PlacedFeature> context) {
            register(context, STONE_PATCH, ModConfiguredFeatures.STONE_PATCH, List.of(RarityFilter.onAverageOnceEvery(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        }

        public static ResourceKey<PlacedFeature> createKey(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Virtua.MOD_ID, name));
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
            context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
            register(context, key, feature, List.of(modifiers));
        }
    }
}
