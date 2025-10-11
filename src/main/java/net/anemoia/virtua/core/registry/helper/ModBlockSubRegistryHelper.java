package net.anemoia.virtua.core.registry.helper;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockSubRegistryHelper extends BlockSubRegistryHelper {
    public ModBlockSubRegistryHelper(RegistryHelper parent) {
        super(parent, parent.getItemSubHelper().getDeferredRegister(), parent.getBlockSubHelper().getDeferredRegister());
    }

    public <B extends Block> RegistryObject<B> createWallOrVerticalBlock(String name, String wallName, Supplier<? extends B> supplier, Supplier<? extends B> wallSupplier) {
        RegistryObject<B> block = this.deferredRegister.register(wallName, wallSupplier);
        return block;
    }
}
