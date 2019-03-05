package xyz.brassgoggledcoders.contaminationapi;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface IContaminationInteracter {
	int getContaminationModifier();
	
	public static class Storage implements Capability.IStorage<IContaminationInteracter> {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IContaminationInteracter> capability, IContaminationInteracter instance,
				EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<IContaminationInteracter> capability, IContaminationInteracter instance, EnumFacing side,
				NBTBase nbt) {
			
		}
	}

	public static class Implementation implements IContaminationInteracter {
		int value;
		
		public Implementation(int value) {
			this.value = value;
		}

		@Override
		public int getContaminationModifier() {
			return value;
		}
	}

	public static class Factory implements Callable<IContaminationInteracter> {

		@Override
		public IContaminationInteracter call() throws Exception {
			return new Implementation(0);
		}
	}
}
