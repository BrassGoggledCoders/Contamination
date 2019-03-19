package xyz.brassgoggledcoders.contamination.api.modifiers;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

public interface IContaminationInteracter {

	IContaminationType getType();
	int getContaminationModifier();
	
	//Nothing to be stored.
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

	//Not to be used
	public static class Factory implements Callable<IContaminationInteracter> {
		@Override
		public IContaminationInteracter call() throws Exception {
			return new Implementation(null, 0);
		}
	}
}
