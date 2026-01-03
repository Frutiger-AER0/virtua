package net.anemoia.virtua.core.registry;

import net.anemoia.virtua.core.Virtua;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Virtua.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SANDS_OF_TIME_TAB = CREATIVE_MODE_TABS.register("sands_of_time_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.DISTILLED_TIME.get()))
                    .title(Component.translatable("creativetab.sands_of_time_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.SAND_OF_TIME.get());
                        pOutput.accept(ModBlocks.REBAR_SPROUTS.get());
                        pOutput.accept(ModItems.TIME_BOTTLE.get());
                        pOutput.accept(ModItems.DISTILLED_TIME.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
