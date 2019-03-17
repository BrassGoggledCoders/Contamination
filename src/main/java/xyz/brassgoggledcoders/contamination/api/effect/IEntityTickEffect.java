package xyz.brassgoggledcoders.contamination.api.effect;

import net.minecraft.entity.EntityLivingBase;

public interface IEntityTickEffect extends IContaminationEffect {
	public void triggerEffect(EntityLivingBase entityLiving, int contaminationLevel);
}
