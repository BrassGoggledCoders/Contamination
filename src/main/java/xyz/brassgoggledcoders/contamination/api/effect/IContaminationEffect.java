package xyz.brassgoggledcoders.contamination.api.effect;

import java.util.Random;

import net.minecraft.world.EnumDifficulty;

public abstract interface IContaminationEffect {
	// Level of pollution at which this effect starts to trigger
	public int getThreshold();

	// Reduction in chunk pollution carrying out this effect will trigger
	public int getReductionOnEffect(EnumDifficulty enumDifficulty, Random rand);
}
