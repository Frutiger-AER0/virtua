package net.anemoia.virtua.core.other;

import net.anemoia.virtua.core.Virtua;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModModelLayers {
    public static final ModelLayerLocation CLOCKFIN_LARGE = register("clockfin_large");

    public static ModelLayerLocation register(String name) {
        return register(name, "main");
    }

    public static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(Virtua.location(name), layer);
    }
}
