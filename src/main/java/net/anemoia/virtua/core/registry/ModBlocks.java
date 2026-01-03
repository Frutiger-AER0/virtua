package net.anemoia.virtua.core.registry;

import net.anemoia.virtua.core.Virtua;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Virtua.MOD_ID);

    public static final RegistryObject<Block> SAND_OF_TIME = registerBlock("sand_of_time",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND)));
    public static final RegistryObject<Block> REBAR_SPROUTS = registerBlock("rebar_sprouts",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH)) {
                @Override
                protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
                    // Allow placement on any block
                    return true;
                }

                @Override
                public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
                    // 12x12x12 box inset by 2px (use Block.box with 0..16 coordinates)
                    return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
                }

                @Override
                public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
                    // No collision (walk-through). Use Shapes.empty() if you want no collision.
                    return Shapes.empty();
                }
            }
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
