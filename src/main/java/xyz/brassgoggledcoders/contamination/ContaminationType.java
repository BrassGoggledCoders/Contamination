package xyz.brassgoggledcoders.contamination;

import java.util.HashMap;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import xyz.brassgoggledcoders.contamination.api.effect.*;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

public class ContaminationType implements IContaminationType {
	public String name;
	public int color;
	public HashMap<EnumEffectType, Set<IContaminationEffect>> effectSets = Maps.newHashMap();

	public ContaminationType(String name, int color, IContaminationEffect... effectSet) {
		this.name = name;
		this.color = color;
		// TODO This needs to be automatic in some way
		Set<IContaminationEffect> other = Sets.newHashSet();
		Set<IContaminationEffect> entityTick = Sets.newHashSet();
		Set<IContaminationEffect> worldTick = Sets.newHashSet();
		Set<IContaminationEffect> overlay = Sets.newHashSet();
		for(IContaminationEffect effect : effectSet) {
			if(effect instanceof IEntityTickEffect) {
				entityTick.add(effect);
			}
			else if(effect instanceof IWorldTickEffect) {
				worldTick.add(effect);
			}
			else if(effect instanceof IOverlayEffect) {
				overlay.add(effect);
			}
			else {
				other.add(effect);
			}
		}
		effectSets.put(EnumEffectType.ENTITYTICK, entityTick);
		effectSets.put(EnumEffectType.WORLDTICK, worldTick);
		effectSets.put(EnumEffectType.OVERLAY, overlay);
		effectSets.put(EnumEffectType.OTHER, other);
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
