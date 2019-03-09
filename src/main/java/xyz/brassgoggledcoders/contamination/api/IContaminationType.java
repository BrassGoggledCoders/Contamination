package xyz.brassgoggledcoders.contamination.api;

import java.util.Set;

public interface IContaminationType {
	public String getName();
	public int getColor();
	public Set<IContaminationEffect> getEffectSet();
}
