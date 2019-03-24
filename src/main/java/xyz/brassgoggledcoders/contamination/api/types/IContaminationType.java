package xyz.brassgoggledcoders.contamination.api.types;

import java.util.Set;

import net.minecraft.util.text.translation.I18n;
import xyz.brassgoggledcoders.contamination.api.effect.EnumEffectType;
import xyz.brassgoggledcoders.contamination.api.effect.IContaminationEffect;

@SuppressWarnings("deprecation")
public interface IContaminationType {
	public String getRegistryName();

	public default String getLocalizedName() {
		return I18n.translateToLocal("contamination." + getRegistryName());
	}

	public int getColor();

	// TODO Make this a hashmap with threshold as the key to filter out currently
	// irrelevant effects. But how do we resolve duplicate keys?
	public Set<IContaminationEffect> getEffectSet(EnumEffectType type);
}
