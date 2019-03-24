package xyz.brassgoggledcoders.contamination.api.types;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public class ContaminationTypeRegistry {
	private static Map<String, IContaminationType> contaminationTypes = Maps.newHashMap();

	public static void addContaminationType(IContaminationType type) {
		contaminationTypes.put(type.getRegistryName(), type);
	}

	@Nullable
	public static IContaminationType getFromName(String name) {
		return contaminationTypes.get(name);
	}

	public static Collection<IContaminationType> getAllTypes() {
		return contaminationTypes.values();
	}
}
