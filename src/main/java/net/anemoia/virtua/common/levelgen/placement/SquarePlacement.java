package net.anemoia.virtua.common.levelgen.placement;

import com.mojang.serialization.Codec;
import net.anemoia.virtua.core.registry.ModPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class SquarePlacement extends PlacementModifier {
    public static final SquarePlacement INSTANCE = new SquarePlacement();
    public static final Codec<SquarePlacement> CODEC = Codec.unit(() -> INSTANCE);

    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        return Stream.of(new BlockPos(pos.getX() + 8, pos.getY(), pos.getZ() + 8));
    }

    public PlacementModifierType<?> type() {
        return ModPlacementModifierTypes.SQUARE_CENTER.get();
    }
}
