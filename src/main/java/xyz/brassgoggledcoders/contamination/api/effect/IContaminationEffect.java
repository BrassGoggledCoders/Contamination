package xyz.brassgoggledcoders.contamination.api.effect;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public interface IContaminationEffect {

	public int getThreshold();
	
	public default void triggerEffect() {
		
	}
	
	public default void triggerEffect(@Nullable EntityPlayer player) {
		
	}

	public default void triggerEffect(@Nullable EntityLivingBase entityLiving) {
		
	}
	
	public default void triggerEffect(@Nullable EntityLivingBase entityLiving, int contaminationLevel) {
		
	}

	public default void triggerEffect(Chunk chunk) {
		
	}
}
