package net.anemoia.virtua.core.registry;

import net.anemoia.virtua.core.Virtua;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Virtua.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModBiomes {
    public static final ResourceKey<Biome> SANDS_OF_TIME = createKey("sands_of_time");
    public static final ResourceKey<Biome> REBORN_FOREST = createKey("reborn_forest");
    public static final ResourceKey<Biome> SPARSE_REBORN_FOREST = createKey("sparse_reborn_forest");
    public static final ResourceKey<Biome> VOLCANIC_FLATS = createKey("volcanic_flats");

    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> feature = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(SANDS_OF_TIME, sandsOfTime(feature, carvers));
        context.register(REBORN_FOREST, rebornForest(feature, carvers));
        context.register(SPARSE_REBORN_FOREST, sparseRebornForest(feature, carvers));
        context.register(VOLCANIC_FLATS, volcanicFlats(feature, carvers));
    }

    public static ResourceKey<Biome> createKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(Virtua.MOD_ID, name));
    }

    public static Biome sandsOfTime(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, features.getOrThrow(ModFeatures.ModPlacedFeatures.STONE_PATCH));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .skyColor(3519728)
                                .fogColor(2421480)
                                .waterColor(2421480)
                                .waterFogColor(3519728)
                                .build()
                )
                .mobSpawnSettings(
                        new MobSpawnSettings.Builder()
                                .build()
                )
                .generationSettings(generation.build())
                .build();
    }

    public static Biome rebornForest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(1.6F)
                .downfall(0.2F)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .skyColor(15891999)
                                .fogColor(0)
                                .waterColor(5852484)
                                .waterFogColor(5852484)
                                .build()
                )
                .mobSpawnSettings(
                        new MobSpawnSettings.Builder()
                                .build()
                )
                .generationSettings(generation.build())
                .build();
    }

    public static Biome sparseRebornForest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(1.6F)
                .downfall(0.2F)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .skyColor(15891999)
                                .fogColor(0)
                                .waterColor(5852484)
                                .waterFogColor(5852484)
                                .build()
                )
                .mobSpawnSettings(
                        new MobSpawnSettings.Builder()
                                .build()
                )
                .generationSettings(generation.build())
                .build();
    }

    public static Biome volcanicFlats(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(1.6F)
                .downfall(0.2F)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .skyColor(15891999)
                                .fogColor(0)
                                .waterColor(5852484)
                                .waterFogColor(5852484)
                                .build()
                )
                .mobSpawnSettings(
                        new MobSpawnSettings.Builder()
                                .build()
                )
                .generationSettings(generation.build())
                .build();
    }
}
