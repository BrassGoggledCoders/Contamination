package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.brassgoggledcoders.contamination.api.IContaminationEffect;

public class EffectPoison implements IContaminationEffect {

	@Override
	public int getThreshold() {
		return 30;
	}

	
	public void triggerEffect(@Nullable EntityLivingBase entityLiving, int contaminationLevel) {
		if(entityLiving.getEntityWorld().rand.nextInt(130 - contaminationLevel) == 0) {
			entityLiving.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("poison"),60, 0));
		}
	}
}
