package xyz.brassgoggledcoders.contamination;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
}