package xyz.brassgoggledcoders.contamination;

import xyz.brassgoggledcoders.contamination.api.IContaminationEffect;

public class ContaminationEffectTest implements IContaminationEffect {

	@Override
	public int getThreshold() {
		return 5;
	}

	@Override
	public void triggerEffect() {
		ContaminationMod.instance.getLogger().devInfo("Effect triggered");
	}

}
