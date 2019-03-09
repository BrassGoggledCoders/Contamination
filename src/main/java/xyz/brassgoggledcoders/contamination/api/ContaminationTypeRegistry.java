package xyz.brassgoggledcoders.contamination.api;

import java.util.LinkedList;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

public class ContaminationTypeRegistry {
	private static LinkedList<IContaminationType> contaminationTypes = Lists.newLinkedList();
	
	public static int getNumberOfTypes() {
		return contaminationTypes.size();
	}
	
	public static boolean addContaminationType(IContaminationType type) {
		return contaminationTypes.add(type);
	}
	
	@Nullable
	public static IContaminationType getFromName(String name) {
		for(IContaminationType type : contaminationTypes) {
			if(type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
	public static IContaminationType getAtPosition(int pos) {
		return contaminationTypes.get(pos);
	}
	
	public static int getPosition(IContaminationType type) {
		return contaminationTypes.indexOf(type);
	}
}
