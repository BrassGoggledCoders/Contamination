package xyz.brassgoggledcoders.contamination.effects;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import xyz.brassgoggledcoders.contamination.api.effect.IEntityTickEffect;

public class EffectPotion implements IEntityTickEffect {

	int threshold;
	String effectName;
	boolean isChance;
	
	public EffectPotion(int threshold, String effectName, boolean isChance) {
		this.threshold = threshold;
		this.effectName = effectName;
	}
	
	@Override
	public int getThreshold() {
		return threshold;
	}

	@Override
	public void triggerEffect(EntityLivingBase entityLiving, int contaminationLevel) {
		//TODO make properly generic
		if(isChance) {
			if(entityLiving.getEntityWorld().rand.nextInt(100 + threshold - contaminationLevel) == 0) {
				entityLiving.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(effectName),60, 0));
			}
		}
		else {
			entityLiving.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(effectName),60, 0, true, false));
		}
	}

	@Override
	public int getReductionOnEffect(EnumDifficulty enumDifficulty, Random rand) {
		if(EnumDifficulty.HARD.equals(enumDifficulty)) {
			return 0;
		}
		return rand.nextInt(3);
	}
}