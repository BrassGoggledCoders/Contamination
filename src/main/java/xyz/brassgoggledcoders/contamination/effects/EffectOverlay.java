package xyz.brassgoggledcoders.contamination.effects;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import xyz.brassgoggledcoders.contamination.api.effect.IOverlayEffect;

public class EffectOverlay implements IOverlayEffect {

	int threshold;
	ResourceLocation overlayLocation;

	public EffectOverlay(int threshold, ResourceLocation overlayLocation) {
		this.threshold = threshold;
		this.overlayLocation = overlayLocation;
	}

	@Override
	public int getThreshold() {
		return threshold;
	}

	@Override
	public int getReductionOnEffect(EnumDifficulty enumDifficulty, Random rand) {
		// Nope. Too easy.
		return 0;
	}

	@Override
	public void triggerEffect(RenderGameOverlayEvent event) {
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		final int i = event.getResolution().getScaledWidth();
		final int k = event.getResolution().getScaledHeight();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(overlayLocation);
		final Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator.getBuffer().pos(i / 2 - k, k, -90D).tex(0.0D, 1.0D).endVertex();
		tessellator.getBuffer().pos(i / 2 + k, k, -90D).tex(1.0D, 1.0D).endVertex();
		tessellator.getBuffer().pos(i / 2 + k, 0.0D, -90D).tex(1.0D, 0.0D).endVertex();
		tessellator.getBuffer().pos(i / 2 - k, 0.0D, -90D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
