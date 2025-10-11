package net.anemoia.virtua.client.model;

import net.anemoia.virtua.common.entity.ClockFin;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;


public class ClockFinLargeModel<T extends ClockFin> extends HierarchicalModel<T> {
	private final ModelPart body;
	private final ModelPart fin;

	public ClockFinLargeModel(ModelPart root) {
        super(RenderType::entityTranslucent);
		this.body = root.getChild("body");
		this.fin = body.getChild("fin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition part = mesh.getRoot();

		PartDefinition body = part.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -7.0F, -12.0F, 0.0F, 13.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 4.0F));

		PartDefinition fin = body.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(0, 30).addBox(0.0349F, -3.0F, -0.0006F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

    @Override
    public ModelPart root() {
        return body;
    }

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 1.0f;
        if (!entity.isInWater()) {
            f = 1.5f;
        }

        this.fin.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
	}
}