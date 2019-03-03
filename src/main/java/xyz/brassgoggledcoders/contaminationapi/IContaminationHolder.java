package xyz.brassgoggledcoders.contaminationapi;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public interface IContaminationHolder {

	int get();

	void set(int value, boolean markDirty);

	public static class Implementation implements IContaminationHolder {
		private int value;

		@Override
		public int get() {
			return value;
		}

		@Override
		public void set(int value, boolean markDirty) {
			this.value = value;
		}
	}

	public static class SafeImplementation extends Implementation {
		private final Chunk chunk;

		public SafeImplementation(Chunk chunk) {
			this.chunk = chunk;
		}

		@Override
		public void set(int value, boolean markDirty) {
			super.set(value, markDirty);
			if(markDirty) {
				chunk.markDirty();
			}
		}
	}

	public static class Storage implements Capability.IStorage<IContaminationHolder> {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IContaminationHolder> capability, IContaminationHolder instance,
				EnumFacing side) {
			return new NBTTagInt(instance.get());
		}

		@Override
		public void readNBT(Capability<IContaminationHolder> capability, IContaminationHolder instance, EnumFacing side,
				NBTBase nbt) {
			if(nbt instanceof NBTTagInt) {
				// The state is being loaded and not updated. We set the value silently to avoid
				// unnecessary dirty chunks
				instance.set(((NBTTagInt) nbt).getInt(), false);
			}
		}
	}

	public static class Factory implements Callable<IContaminationHolder> {

		@Override
		public IContaminationHolder call() throws Exception {
			return new Implementation();
		}
	}

}
