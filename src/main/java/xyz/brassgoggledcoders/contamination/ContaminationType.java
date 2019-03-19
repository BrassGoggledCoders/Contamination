package xyz.brassgoggledcoders.contamination;

import java.util.Set;

import com.google.common.collect.Sets;

import xyz.brassgoggledcoders.contamination.api.IContaminationType;
import xyz.brassgoggledcoders.contamination.api.effect.IContaminationEffect;

public class ContaminationType implements IContaminationType {
	public String name;
	public int color;
	public Set<IContaminationEffect> effectSet;
	
	public ContaminationType(String name, int color, IContaminationEffect... effectSet) {
		this.name = name;
		this.color = color;
		this.effectSet = Sets.newHashSet(effectSet);
	}

	@Override
	public String getRegistryName() {
		return name;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public Set<IContaminationEffect> getEffectSet() {
		return effectSet;
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if(o instanceof IContaminationType) {
//			((IContaminationType) o).getName().equalsIgnoreCase(this.getName());
//		}
//		return false;
//	}
}
