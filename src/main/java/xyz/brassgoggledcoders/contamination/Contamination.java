package xyz.brassgoggledcoders.contamination;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.teamacronymcoders.base.BaseModFoundation;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;
import xyz.brassgoggledcoders.contamination.api.modifiers.IContaminationInteracter;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;
import xyz.brassgoggledcoders.contamination.network.PacketSyncContamination;

@Mod(modid = Contamination.MODID, name = Contamination.MODNAME, version = Contamination.MODVERSION)
public class Contamination extends BaseModFoundation<Contamination> {

	public static final String MODID = "contamination";
	public static final String MODNAME = "Contamination";
	public static final String MODVERSION = "@VERSION@";
	@Instance(MODID)
	public static Contamination instance;
	
	static {
		FluidRegistry.enableUniversalBucket();
	}

	public Contamination() {
		super(MODID, MODNAME, MODVERSION, null);
	}

	@CapabilityInject(IContaminationHolder.class)
	public static Capability<IContaminationHolder> CONTAMINATION_HOLDER_CAPABILITY = null;
	@CapabilityInject(IContaminationInteracter.class)
	public static Capability<IContaminationInteracter> CONTAMINATION_INTERACTER_CAPABILITY = null;

	@EventHandler
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		CapabilityManager.INSTANCE.register(IContaminationHolder.class, new IContaminationHolder.Storage(),
				new IContaminationHolder.Factory());
		CapabilityManager.INSTANCE.register(IContaminationInteracter.class, new IContaminationInteracter.Storage(),
				new IContaminationInteracter.Factory());
		
		Contamination.instance.getPacketHandler().registerPacket(PacketSyncContamination.Handler.class, PacketSyncContamination.class,
				Side.CLIENT);
	}

	public static class ContaminationInteracterProvider implements ICapabilityProvider {
		private final IContaminationInteracter contamination;

		public ContaminationInteracterProvider(IContaminationType type, int value) {
			contamination = new IContaminationInteracter.Implementation(type, value);
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CONTAMINATION_INTERACTER_CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			return capability == CONTAMINATION_INTERACTER_CAPABILITY
					? CONTAMINATION_INTERACTER_CAPABILITY.cast(contamination)
					: null;
		}
	}

	public static class ContaminationProvider implements ICapabilitySerializable<NBTBase> {
		private final IContaminationHolder contamination;

		public ContaminationProvider(Chunk chunk) {
			contamination = new IContaminationHolder.SafeImplementation(chunk);
		}

		@Override
		public NBTBase serializeNBT() {
			return CONTAMINATION_HOLDER_CAPABILITY.getStorage().writeNBT(CONTAMINATION_HOLDER_CAPABILITY, contamination,
					null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			CONTAMINATION_HOLDER_CAPABILITY.getStorage().readNBT(CONTAMINATION_HOLDER_CAPABILITY, contamination, null,
					nbt);
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CONTAMINATION_HOLDER_CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			return capability == CONTAMINATION_HOLDER_CAPABILITY ? CONTAMINATION_HOLDER_CAPABILITY.cast(contamination)
					: null;
		}
	}

	@Override
	public Contamination getInstance() {
		return instance;
	}
}
