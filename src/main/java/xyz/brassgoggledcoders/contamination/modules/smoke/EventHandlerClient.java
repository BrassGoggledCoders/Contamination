package xyz.brassgoggledcoders.contamination.modules.smoke;

import java.awt.Color;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.brassgoggledcoders.contamination.Contamination;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;

//@EventBusSubscriber(value = Side.CLIENT, modid = Contamination.MODID)
public class EventHandlerClient {
	@SubscribeEvent
	public static void fogColours(EntityViewRenderEvent.FogColors event) {
		IContaminationHolder holder = Minecraft.getMinecraft().world
				.getChunk(Minecraft.getMinecraft().player.getPosition())
				.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
		if(holder.get(ModuleSmoke.smoke) > 0) {
			Color col = new Color(ModuleSmoke.smoke.getColor());
			event.setRed(col.getRed());
			event.setBlue(col.getBlue());
			event.setGreen(col.getGreen());
		}
	}

	@SubscribeEvent
	public static void colourizers(ColorHandlerEvent.Block event) {
		event.getBlockColors().registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos,
					int tintIndex) {
				if(worldIn != null) {
					Color color = new Color(BiomeColorHelper.getGrassColorAtPos(worldIn, pos));
					if(worldIn instanceof World) {
						World world = (World) worldIn;
						Chunk chunk = world.getChunk(pos);
						IContaminationHolder holder = chunk.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY,
								null);
						int smoke = holder.get(ModuleSmoke.smoke);
						if(smoke > 0) {
							// Color smokeCol = new Color(ModuleSmoke.smoke.getColor());
							// int red = MathHelper.clamp(smoke / 100, color.getRed(), smokeCol.getRed());
							// int green = MathHelper.clamp(smoke / 100, color.getGreen(),
							// smokeCol.getGreen());
							// int blue = MathHelper.clamp(smoke / 100, color.getBlue(),
							// smokeCol.getBlue());
							// FMLLog.warning("" + red);
							// FMLLog.warning("" + green);
							// FMLLog.warning("" + blue);
							color = new Color(657951);// 'Dirty' green
						}
					}
					return color.getRGB();
				}
				return ColorizerGrass.getGrassColor(0.5D, 1.0D);
			}
		}, Blocks.GRASS);
	}
}
