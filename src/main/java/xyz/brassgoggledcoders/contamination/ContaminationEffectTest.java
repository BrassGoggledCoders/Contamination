package xyz.brassgoggledcoders.contamination;

import xyz.brassgoggledcoders.contamination.api.IContaminationEffect;

public class ContaminationEffectTest implements IContaminationEffect {

	@Override
	public int getWeight() {
		return 0;
	}

	@Override
	public void triggerEffect() {
		ContaminationMod.instance.getLogger().devInfo("Effect triggered");
	}

}
