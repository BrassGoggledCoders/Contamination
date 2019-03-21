package xyz.brassgoggledcoders.contamination.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import xyz.brassgoggledcoders.contamination.api.types.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

public interface IContaminationHolder {

	int get(IContaminationType type);
	
	void set(IContaminationType type, int value);
	
	default void modify(IContaminationType type, int delta) {
		set(type, get(type) + delta);
	}
	
	NBTBase writeToNBT();
	void readFromNBT(NBTBase tag);

	public static class Implementation implements IContaminationHolder {

		HashMap<IContaminationType, Integer> contaminations = Maps.newHashMap();
		
		@Override
		public int get(IContaminationType type) {
			if(type == null /*What are you even doing?!*/ || !contaminations.containsKey(type) /*There is no pollution of that kind (yet >:D)*/) {
				return 0;
			}
			return contaminations.get(type);
		}

		@Override
		public void set(IContaminationType type, int value) {
			if(value < 0) {
				//Negative pollution is not allowed
				value = 0;
			}
			contaminations.put(type, value);
		}
		
		
		@Override
		public NBTBase writeToNBT() {
			NBTTagCompound tagCompound = new NBTTagCompound();
			for(Map.Entry<IContaminationType, Integer> entry: contaminations.entrySet()) {
			    tagCompound.setInteger(entry.getKey().getRegistryName(), entry.getValue());
			}
			return tagCompound;
		}

		@Override
		public void readFromNBT(NBTBase tag) {
			contaminations.clear();
			if(tag instanceof NBTTagCompound) { //If not something has gone wrong!
				NBTTagCompound comp = (NBTTagCompound) tag;
				for(String key : comp.getKeySet()) {
					contaminations.put(ContaminationTypeRegistry.getFromName(key), comp.getInteger(key));
				}
			}
		}

		
	}

	public static class SafeImplementation extends Implementation {
		private final Chunk chunk;

		public SafeImplementation(Chunk chunk) {
			this.chunk = chunk;
		}

		@Override
		public int get(IContaminationType type) {
			if(!contaminations.containsKey(type)) {
				contaminations.put(type, 0);
				return 0;
			}
			return contaminations.get(type);
		}

		@Override
		public void set(IContaminationType type, int value) {
			contaminations.put(type, value);
			chunk.markDirty();
		}
	}

	public static class Storage implements Capability.IStorage<IContaminationHolder> {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IContaminationHolder> capability, IContaminationHolder instance,
				EnumFacing side) {
			return instance.writeToNBT();
		}

		@Override
		public void readNBT(Capability<IContaminationHolder> capability, IContaminationHolder instance, EnumFacing side,
				NBTBase nbt) {
			instance.readFromNBT(nbt);
		}
	}

	//Should not be used
	public static class Factory implements Callable<IContaminationHolder> {
		@Override
		public IContaminationHolder call() throws Exception {
			return new Implementation();
		}
	}

}
