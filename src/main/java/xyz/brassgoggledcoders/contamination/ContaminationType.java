package xyz.brassgoggledcoders.contamination;

import java.util.HashMap;
import java.util.Set;

import com.google.common.collect.Sets;

import xyz.brassgoggledcoders.contamination.api.effect.*;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

public class ContaminationType implements IContaminationType {
	public String name;
	public int color;
	public HashMap<EnumEffectType, Set<IContaminationEffect>> effectSets;
	
	public ContaminationType(String name, int color, IContaminationEffect... effectSet) {
		this.name = name;
		this.color = color;
		Set<IContaminationEffect> other = Sets.newHashSet();
		Set<IContaminationEffect> entityTick = Sets.newHashSet();
		Set<IContaminationEffect> worldTick = Sets.newHashSet();
		for(IContaminationEffect effect : effectSet) {
			if(effect instanceof IEntityTickEffect) {
				entityTick.add(effect);
			}
			else if(effect instanceof IWorldTickEffect) {
				worldTick.add(effect);
			}
			else {
				other.add(effect);
			}
		}
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
	public Set<IContaminationEffect> getEffectSet(EnumEffectType type) {
		return effectSets.get(type);
	}
}
