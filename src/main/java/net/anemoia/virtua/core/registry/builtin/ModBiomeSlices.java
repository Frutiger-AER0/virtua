package net.anemoia.virtua.core.registry.builtin;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.world.modification.ModdedBiomeSlice;
import com.teamabnormals.blueprint.core.registry.BlueprintBiomes;
import com.teamabnormals.blueprint.core.registry.BlueprintDataPackRegistries;
import com.teamabnormals.blueprint.core.util.BiomeUtil;
import com.teamabnormals.blueprint.core.util.BiomeUtil.MultiNoiseModdedBiomeProvider;
import net.anemoia.virtua.core.Virtua;
import net.anemoia.virtua.core.registry.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anemoia.virtua.core.registry.ModBiomes.*;

public class ModBiomeSlices {
    public static final ResourceKey<ModdedBiomeSlice> SANDS_OF_TIME_SLICE = createKey("sands_of_time_slice");
    public static final ResourceKey<ModdedBiomeSlice> REBORN_FOREST_SLICE = createKey("reborn_forest_slice");

    public static final ResourceKey<Biome> REBORN_FOREST_AREA = ModBiomes.createKey("reborn_forest_area");
    public static final ResourceKey<Biome> SPARSE_REBORN_FOREST_AREA = ModBiomes.createKey("sparse_reborn_forest_area");
    public static final ResourceKey<Biome> VOLCANIC_FLATS_AREA = ModBiomes.createKey("volcanic_flats_area");

    public static void bootstrap(BootstapContext<ModdedBiomeSlice> context) {
        List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> entries = new ArrayList<>();
        new VirtuaBiomeBuilder().addBiomes(entries::add);

        context.register(REBORN_FOREST_SLICE, new ModdedBiomeSlice(8, MultiNoiseModdedBiomeProvider.builder().biomes(entries::forEach)
                .area(REBORN_FOREST_AREA, REBORN_FOREST)
                .area(SPARSE_REBORN_FOREST_AREA, SPARSE_REBORN_FOREST)
                .area(VOLCANIC_FLATS_AREA, VOLCANIC_FLATS)
                .build(), LevelStem.OVERWORLD));

        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(SANDS_OF_TIME_SLICE, new ModdedBiomeSlice(15,
                new BiomeUtil.OverlayModdedBiomeProvider(List.of(
                        Pair.of(
                                HolderSet.direct(
                                        Stream.of(Biomes.DESERT)
                                                .map(biomes::getOrThrow)
                                                .collect(Collectors.toList())
                                ),
                                new FixedBiomeSource(Holder.direct(biomes.getOrThrow(ModBiomes.SANDS_OF_TIME)).get())
                        )
                )), LevelStem.OVERWORLD)
        );
    }

    public static ResourceKey<ModdedBiomeSlice> createKey(String name) {
        return ResourceKey.create(BlueprintDataPackRegistries.MODDED_BIOME_SLICES, new ResourceLocation(Virtua.MOD_ID, name));
    }

    @SuppressWarnings("unchecked")
    private static final class VirtuaBiomeBuilder {
        private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
        private final Climate.Parameter[] temperatures = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.45F), Climate.Parameter.span(-0.45F, -0.15F), Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.2F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
        private final Climate.Parameter[] humidities = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.35F), Climate.Parameter.span(-0.35F, -0.1F), Climate.Parameter.span(-0.1F, 0.1F), Climate.Parameter.span(0.1F, 0.3F), Climate.Parameter.span(0.3F, 1.0F)};
        private final Climate.Parameter[] erosions = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.78F), Climate.Parameter.span(-0.78F, -0.375F), Climate.Parameter.span(-0.375F, -0.2225F), Climate.Parameter.span(-0.2225F, 0.05F), Climate.Parameter.span(0.05F, 0.45F), Climate.Parameter.span(0.45F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
        private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
        private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
        private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
        private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
        private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
        private final ResourceKey<Biome> VANILLA = BlueprintBiomes.ORIGINAL_SOURCE_MARKER;
        private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, REBORN_FOREST_AREA, REBORN_FOREST_AREA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA}
        };
        private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
                {VANILLA, null, VANILLA, null, null},
                {null, null, null, null, VANILLA},
                {VANILLA, null, null, VANILLA, null},
                {null, null, VANILLA, null, null},
                {null, null, null, null, null}
        };
        private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, REBORN_FOREST_AREA, REBORN_FOREST_AREA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA}
        };
        private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
                {VANILLA, null, null, null, null},
                {null, null, VANILLA, null, VANILLA},
                {null, null, VANILLA, VANILLA, null},
                {null, null, null, VOLCANIC_FLATS_AREA, SPARSE_REBORN_FOREST_AREA},
                {null, null, null, null, null}
        };
        private final ResourceKey<Biome>[][] SHATTERED_BIOMES = new ResourceKey[][]{
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {VANILLA, VANILLA, VANILLA, VANILLA, VANILLA},
                {null, null, null, REBORN_FOREST_AREA, REBORN_FOREST_AREA},
                {null, null, null, null, null}
        };

        private void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
            this.addOffCoastBiomes(consumer);
            this.addInlandBiomes(consumer);
            this.addUndergroundBiomes(consumer);
        }

        private void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(-1.2F, -0.35F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, VANILLA);
            this.addSurfaceBiome(consumer, Climate.Parameter.span(-1.0F, 0.2F), this.FULL_RANGE, Climate.Parameter.span(-0.35F, -0.19F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, VANILLA);
            this.addSurfaceBiome(consumer, this.temperatures[4], Climate.Parameter.span(0.1F, 1.0F), Climate.Parameter.span(-0.35F, -0.19F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, VANILLA);
        }

        private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
            this.addMidSlice(consumer, Climate.Parameter.span(-1.0F, -0.93333334F));
            this.addHighSlice(consumer, Climate.Parameter.span(-0.93333334F, -0.7666667F));
            this.addPeaks(consumer, Climate.Parameter.span(-0.7666667F, -0.56666666F));
            this.addHighSlice(consumer, Climate.Parameter.span(-0.56666666F, -0.4F));
            this.addMidSlice(consumer, Climate.Parameter.span(-0.4F, -0.26666668F));
            this.addLowSlice(consumer, Climate.Parameter.span(-0.26666668F, -0.05F));
            this.addValleys(consumer, Climate.Parameter.span(-0.05F, 0.05F));
            this.addLowSlice(consumer, Climate.Parameter.span(0.05F, 0.26666668F));
            this.addMidSlice(consumer, Climate.Parameter.span(0.26666668F, 0.4F));
            this.addHighSlice(consumer, Climate.Parameter.span(0.4F, 0.56666666F));
            this.addPeaks(consumer, Climate.Parameter.span(0.56666666F, 0.7666667F));
            this.addHighSlice(consumer, Climate.Parameter.span(0.7666667F, 0.93333334F));
            this.addMidSlice(consumer, Climate.Parameter.span(0.93333334F, 1.0F));
        }

        private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
            for (int i = 0; i < this.temperatures.length; ++i) {
                Climate.Parameter climate$parameter = this.temperatures[i];

                for (int j = 0; j < this.humidities.length; ++j) {
                    Climate.Parameter climate$parameter1 = this.humidities[j];
                    ResourceKey<Biome> resourcekey = this.pickMiddleBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey3 = this.pickPlateauBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey4 = this.pickShatteredBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey6 = this.pickPeakBiome(i, j, weirdness);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, resourcekey6);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, resourcekey6);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, resourcekey3);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, resourcekey3);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, resourcekey4);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, resourcekey);
                }
            }
        }

        private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
            for (int i = 0; i < this.temperatures.length; ++i) {
                Climate.Parameter climate$parameter = this.temperatures[i];

                for (int j = 0; j < this.humidities.length; ++j) {
                    Climate.Parameter climate$parameter1 = this.humidities[j];
                    ResourceKey<Biome> resourcekey = this.pickMiddleBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey3 = this.pickPlateauBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey4 = this.pickShatteredBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey6 = this.pickSlopeBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey7 = this.pickPeakBiome(i, j, weirdness);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions[0], weirdness, 0.0F, resourcekey6);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, resourcekey7);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, resourcekey6);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, resourcekey3);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, resourcekey3);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, resourcekey4);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, resourcekey);
                }
            }
        }

        private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, VANILLA);

            this.addSurfaceBiome(consumer, Climate.Parameter.span(-1.0F, 0.2F), FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, VANILLA);
            this.addSurfaceBiome(consumer, Climate.Parameter.span(0.6F, 1.0F), FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, VANILLA);

            for (int i = 0; i < this.temperatures.length; ++i) {
                Climate.Parameter climate$parameter = this.temperatures[i];

                for (int j = 0; j < this.humidities.length; ++j) {
                    Climate.Parameter climate$parameter1 = this.humidities[j];
                    ResourceKey<Biome> resourcekey = this.pickMiddleBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey3 = this.pickShatteredBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey4 = this.pickPlateauBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey5 = this.pickBeachBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey7 = this.pickShatteredCoastBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey8 = this.pickSlopeBiome(i, j, weirdness);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, resourcekey8);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions[1], weirdness, 0.0F, i == 0 ? resourcekey8 : resourcekey4);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions[2], weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions[2], weirdness, 0.0F, resourcekey4);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[3], weirdness, 0.0F, resourcekey);
                    if (weirdness.max() < 0L) {
                        this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[4], weirdness, 0.0F, resourcekey5);
                        this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, resourcekey);
                    } else {
                        this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, resourcekey);
                    }

                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, resourcekey7);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, resourcekey3);
                    if (weirdness.max() < 0L) {
                        this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, resourcekey5);
                    } else {
                        this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, resourcekey);
                    }

                    if (i == 0) {
                        this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, resourcekey);
                    }
                }
            }
        }

        private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, VANILLA);

            this.addSurfaceBiome(consumer, Climate.Parameter.span(-1.0F, 0.2F), FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, VANILLA);
            this.addSurfaceBiome(consumer, Climate.Parameter.span(0.6F, 1.0F), FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, VANILLA);

            for (int i = 0; i < this.temperatures.length; ++i) {
                Climate.Parameter climate$parameter = this.temperatures[i];

                for (int j = 0; j < this.humidities.length; ++j) {
                    Climate.Parameter climate$parameter1 = this.humidities[j];
                    ResourceKey<Biome> resourcekey = this.pickMiddleBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey3 = this.pickBeachBiome(i, j, weirdness);
                    ResourceKey<Biome> resourcekey5 = this.pickShatteredCoastBiome(i, j, weirdness);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, Climate.Parameter.span(this.erosions[3], this.erosions[4]), weirdness, 0.0F, resourcekey3);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, resourcekey5);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, resourcekey);
                    this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, resourcekey3);
                    if (i == 0) {
                        this.addSurfaceBiome(consumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, resourcekey);
                    }
                }
            }
        }

        private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
            this.addRiverBiomes(consumer, Climate.Parameter.span(-1.0F, 0.2F), FULL_RANGE, weirdness, VANILLA);
            this.addRiverBiomes(consumer, Climate.Parameter.span(0.6F, 1.0F), FULL_RANGE, weirdness, VANILLA);

            for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; ++temperatureIndex) {
                Climate.Parameter temperature = this.temperatures[temperatureIndex];
                for (int humidityIndex = 0; humidityIndex < this.humidities.length; ++humidityIndex) {
                    Climate.Parameter humidity = this.humidities[humidityIndex];
                }
            }
        }

        private void addRiverBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter weirdness, ResourceKey<Biome> biome) {
            this.addSurfaceBiome(consumer, temperature, humidity, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, weirdness.max() < 0L ? VANILLA : biome);
            this.addSurfaceBiome(consumer, temperature, humidity, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, biome);
            this.addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, biome);
            this.addSurfaceBiome(consumer, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, biome);
            this.addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, biome);
        }

        private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
            this.addUndergroundBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, VANILLA);
            this.addUndergroundBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, VANILLA);
        }

        private ResourceKey<Biome> pickMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
            if (weirdness.max() < 0L) {
                return this.MIDDLE_BIOMES[temperatureIndex][humidityIndex];
            } else {
                ResourceKey<Biome> resourcekey = this.MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex];
                return resourcekey == null ? this.MIDDLE_BIOMES[temperatureIndex][humidityIndex] : resourcekey;
            }
        }

        private ResourceKey<Biome> pickShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
            ResourceKey<Biome> resourcekey = weirdness.max() >= 0L ? this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : this.pickBeachBiome(temperatureIndex, humidityIndex, weirdness);
            return resourcekey;
        }

        private ResourceKey<Biome> pickBeachBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
            if (temperatureIndex == 0) {
                return VANILLA;
            } else {
                return VANILLA;
            }
        }

        private ResourceKey<Biome> pickPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
            if (weirdness.max() < 0L) {
                return this.PLATEAU_BIOMES[temperatureIndex][humidityIndex];
            } else {
                ResourceKey<Biome> resourcekey = this.PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex];
                return resourcekey == null ? this.PLATEAU_BIOMES[temperatureIndex][humidityIndex] : resourcekey;
            }
        }

        private ResourceKey<Biome> pickPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
            if (temperatureIndex <= 2) {
                return VANILLA;
            } else {
                if (temperatureIndex == 3)
                    return VANILLA;
            }
            return VANILLA;
        }

        private ResourceKey<Biome> pickSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
            if (temperatureIndex >= 3) {
                return this.pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
            } else {
                return VANILLA;
            }
        }

        private ResourceKey<Biome> pickShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
            ResourceKey<Biome> resourcekey = this.SHATTERED_BIOMES[temperatureIndex][humidityIndex];
            return resourcekey == null ? this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : resourcekey;
        }

        private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
            consumer.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), biome));
            consumer.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), biome));
        }

        private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
            consumer.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.span(0.2F, 0.9F), weirdness, offset), biome));
        }
    }
}
