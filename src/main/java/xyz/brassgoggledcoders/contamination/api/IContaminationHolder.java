package xyz.brassgoggledcoders.contamination.api;

import java.util.Arrays;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public interface IContaminationHolder {

	int get(int loc);

	int[] getAll();
	
	void set(int pos, int value, boolean markDirty);
	
	void setAll(int[] values);

	public static class Implementation implements IContaminationHolder {
		private int[] values = new int[ContaminationTypeRegistry.getNumberOfTypes()];

		@Override
		public int[] getAll() {
			return values;
		}

		@Override
		public void set(int pos, int value, boolean markDirty) {
			this.values[pos] = value;
		}

		@Override
		public int get(int pos) {
			if(pos >= values.length) {
				values = Arrays.copyOf(values, ContaminationTypeRegistry.getNumberOfTypes());
			}
			return values[pos];
		}


		@Override
		public void setAll(int[] values) {
			this.values = values;
		}
	}

	public static class SafeImplementation extends Implementation {
		private final Chunk chunk;

		public SafeImplementation(Chunk chunk) {
			this.chunk = chunk;
		}

		@Override
		public void set(int pos, int value, boolean markDirty) {
			super.set(pos, value, markDirty);
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
			return new NBTTagIntArray(instance.getAll());
		}

		@Override
		public void readNBT(Capability<IContaminationHolder> capability, IContaminationHolder instance, EnumFacing side,
				NBTBase nbt) {
			if(nbt instanceof NBTTagIntArray) {
				instance.setAll(((NBTTagIntArray) nbt).getIntArray());
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
