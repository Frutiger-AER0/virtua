package net.anemoia.virtua.common.levelgen.feature;

import com.mojang.serialization.Codec;
import net.anemoia.virtua.common.levelgen.feature.configurations.LargeDiskConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class StonePatchFeature extends Feature<LargeDiskConfiguration> {
    public StonePatchFeature(Codec<LargeDiskConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeDiskConfiguration> context) {
        LargeDiskConfiguration config = context.config();
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos pos = context.origin();

        if (worldIn.getFluidState(pos).is(FluidTags.WATER)) {
            return false;
        } else {
            int i = 0;
            int j = Math.max(config.radius().sample(rand), 0);

            for (int k = pos.getX() - j; k <= pos.getX() + j; ++k) {
                for (int l = pos.getZ() - j; l <= pos.getZ() + j; ++l) {
                    int i1 = k - pos.getX();
                    int j1 = l - pos.getZ();
                    if (i1 * i1 + j1 * j1 <= j * j) {
                        for (int k1 = pos.getY() - config.halfHeight(); k1 <= pos.getY() + config.halfHeight(); ++k1) {
                            BlockPos blockpos = new BlockPos(k, k1, l);
                            BlockState blockstate = worldIn.getBlockState(blockpos);

                            for (BlockState blockstate1 : config.targets()) {
                                if (blockstate1.getBlock() == blockstate.getBlock()) {
                                    // Replace matching surface blocks with stone
                                    worldIn.setBlock(blockpos, Blocks.STONE.defaultBlockState(), 2);
                                    ++i;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return i > 0;
        }
    }
}
