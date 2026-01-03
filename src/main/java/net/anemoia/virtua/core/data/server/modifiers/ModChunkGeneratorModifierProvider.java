package net.anemoia.virtua.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.world.modification.chunk.ChunkGeneratorModifierProvider;
import com.teamabnormals.blueprint.common.world.modification.chunk.modifiers.SurfaceRuleModifier;
import net.anemoia.virtua.core.Virtua;
import net.anemoia.virtua.core.registry.ModBlocks;
import net.anemoia.virtua.core.registry.ModBiomes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public class ModChunkGeneratorModifierProvider extends ChunkGeneratorModifierProvider {
    public ModChunkGeneratorModifierProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(Virtua.MOD_ID, output, provider);
    }

    @Override
    protected void registerEntries(Provider provider) {
        ConditionSource isSandsOfTime = isBiome(ModBiomes.SANDS_OF_TIME);

        RuleSource sandstone = state(Blocks.SANDSTONE.defaultBlockState());

        RuleSource sandOfTime = state(ModBlocks.SAND_OF_TIME.get().defaultBlockState());

        RuleSource sandOfTimeRule = sandRuleSource(sandOfTime, sandstone);

        this.entry("virtua_rule_source").selects("minecraft:overworld")
                .addModifier(new SurfaceRuleModifier(ifTrue(abovePreliminarySurface(), ifTrue(isSandsOfTime, sequence(ifTrue(noiseRange(0.3F, 2.5F), sandOfTimeRule), sandOfTimeRule))), false));
    }

    private RuleSource sandRuleSource(RuleSource sand, RuleSource sandstone) {
        return sequence(
                ifTrue(ON_FLOOR, ifTrue(waterBlockCheck(-1, 0), sequence(ifTrue(ON_CEILING, sandstone), sand))),
                ifTrue(waterStartCheck(-6, -1), sequence(ifTrue(UNDER_FLOOR, sequence(ifTrue(ON_CEILING, sandstone), sand)), ifTrue(VERY_DEEP_UNDER_FLOOR, sandstone)))
        );
    }

    private static ConditionSource noiseRange(double low, double high) {
        return noiseCondition(Noises.SURFACE, low / 8.25D, high / 8.25D);
    }

    private static ConditionSource surfaceNoiseAbove(double noise) {
        return noiseCondition(Noises.SURFACE, noise / 8.25D, Double.MAX_VALUE);
    }
}
