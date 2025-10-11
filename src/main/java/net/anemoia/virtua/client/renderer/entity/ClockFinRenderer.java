package net.anemoia.virtua.client.renderer.entity;

import net.anemoia.virtua.client.model.ClockFinLargeModel;
import net.anemoia.virtua.common.entity.ClockFin;
import net.anemoia.virtua.core.Virtua;
import net.anemoia.virtua.core.other.ModModelLayers;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ClockFinRenderer extends MobRenderer<ClockFin, HierarchicalModel<ClockFin>> {
    private static final ResourceLocation LARGE_CLOCK_FIN_TEXTURE = new ResourceLocation(Virtua.MOD_ID, "textures/entity/clock_fin_large.png");
    //private static final ResourceLocation SMALL_CLOCK_FIN_TEXTURE = new ResourceLocation(Virtua.MOD_ID, "textures/entity/clock_fin_small.png");

    private final ClockFinLargeModel<ClockFin> largeModel;
    // private final ClockFinSmallModel<ClockFin> smallModel;

    public ClockFinRenderer(EntityRendererProvider.Context context) {
        super(context, new ClockFinLargeModel<>(context.bakeLayer(ModModelLayers.CLOCKFIN_LARGE)), 0.3F);
        this.largeModel = new ClockFinLargeModel<>(context.bakeLayer(ModModelLayers.CLOCKFIN_LARGE));
        // this.smallModel = new ClockFinSmallModel<>(context.bakeLayer(ModModelLayers.CLOCKFIN_SMALL));
    }

    @Override
    public HierarchicalModel<ClockFin> getModel() {
        // Return different models based on entity size or other properties
        // return entity.isLarge() ? largeModel : smallModel;

        return largeModel;
    }

    @Override
    public ResourceLocation getTextureLocation(ClockFin entity) {
        // Return different textures based on entity size or other properties
        // return entity.isLarge() ? LARGE_CLOCK_FIN_TEXTURE : SMALL_CLOCK_FIN_TEXTURE;

        return LARGE_CLOCK_FIN_TEXTURE;
    }
}
