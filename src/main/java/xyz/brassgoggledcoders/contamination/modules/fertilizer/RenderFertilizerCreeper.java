package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.contamination.Contamination;

public class RenderFertilizerCreeper extends RenderLiving<EntityFertilizerCreeper> {
	public static final ResourceLocation TEXTURES = new ResourceLocation(Contamination.MODID,
			"textures/entity/fertilizer_creeper.png");

	public RenderFertilizerCreeper(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelCreeper(), 0.5F);

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFertilizerCreeper entity) {
		return TEXTURES;
	}
}
