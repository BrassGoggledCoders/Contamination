package xyz.brassgoggledcoders.contamination.modules.vanilla;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.brassgoggledcoders.contamination.api.IContaminationEffect;

public class ContaminationEffectBlindness implements IContaminationEffect {

	@Override
	public int getThreshold() {
		return 8;
	}

	@Override
	public void triggerEffect(EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("blindness"),40, 0, true, false));
	}

}
