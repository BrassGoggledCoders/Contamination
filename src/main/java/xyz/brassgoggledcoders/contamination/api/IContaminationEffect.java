package xyz.brassgoggledcoders.contamination.api;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;

public interface IContaminationEffect {
	//NYI
	public int getThreshold();
	
	public default void triggerEffect() {
		
	}
	
	public default void triggerEffect(@Nullable EntityPlayer player) {
		
	}
}
