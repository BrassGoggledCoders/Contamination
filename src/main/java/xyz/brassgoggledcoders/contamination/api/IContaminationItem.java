package xyz.brassgoggledcoders.contamination.api;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface IContaminationItem {

	IContaminationType getType();
	int getContaminationModifier();
	
	public static class Storage implements Capability.IStorage<IContaminationItem> {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IContaminationItem> capability, IContaminationItem instance,
				EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<IContaminationItem> capability, IContaminationItem instance, EnumFacing side,
				NBTBase nbt) {
			
		}
	}

	public static class Implementation implements IContaminationItem {
		IContaminationType type;
		int value;
		
		public Implementation(IContaminationType type, int value) {
			this.value = value;
			this.type = type;
		}

		@Override
		public int getContaminationModifier() {
			return value;
		}

		@Override
		public IContaminationType getType() {
			return type;
		}
	}

	public static class Factory implements Callable<IContaminationItem> {
		@Override
		public IContaminationItem call() throws Exception {
			return new Implementation(null, 0);
		}
	}
}
