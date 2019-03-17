package xyz.brassgoggledcoders.contamination.api;

import java.util.Set;

import xyz.brassgoggledcoders.contamination.api.effect.IContaminationEffect;

public interface IContaminationType {
	public String getName();
	public int getColor();
	//TODO Typed sub-sets to reduce loop sizes
	public Set<IContaminationEffect> getEffectSet();
}
