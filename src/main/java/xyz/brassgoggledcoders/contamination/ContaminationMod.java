package xyz.brassgoggledcoders.contamination;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.teamacronymcoders.base.BaseModFoundation;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.brassgoggledcoders.contamination.api.*;
import xyz.brassgoggledcoders.contamination.api.effect.*;
import xyz.brassgoggledcoders.contamination.events.ContaminationUpdateEvent;

@Mod(modid = ContaminationMod.MODID, name = ContaminationMod.MODNAME, version = ContaminationMod.MODVERSION)
@EventBusSubscriber
public class ContaminationMod extends BaseModFoundation<ContaminationMod> {

	static int currentTicks;
	final static int maxTicks = 20;

	public static final String MODID = "contamination";
	public static final String MODNAME = "Contamination";
	public static final String MODVERSION = "@VERSION@";
	@Instance(MODID)
	public static ContaminationMod instance;
	
	static {
		FluidRegistry.enableUniversalBucket();
	}

	public ContaminationMod() {
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
	}

	@SubscribeEvent
	public static void attachChunkCaps(AttachCapabilitiesEvent<Chunk> event) {
		// TODO Only attach the capability when an interacter is placed in the chunk?
		event.addCapability(new ResourceLocation(MODID, "contamination_holder"),
				new ContaminationProvider(event.getObject()));
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
	public ContaminationMod getInstance() {
		return instance;
	}

	@SubscribeEvent
	public static void onUseItem(PlayerInteractEvent.RightClickBlock event) {
		if(!event.getWorld().isRemote) {
			ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
			int delta = 0;
			IContaminationType type = null;

			if(stack.hasCapability(ContaminationMod.CONTAMINATION_INTERACTER_CAPABILITY, null)) {
				IContaminationInteracter interacter = stack
						.getCapability(ContaminationMod.CONTAMINATION_INTERACTER_CAPABILITY, null);
				type = interacter.getType();
				delta = interacter.getContaminationModifier();
			}
			else {
				return;
			}

			if(delta != 0) {
				int typePos = ContaminationTypeRegistry.getPosition(type);
				Chunk chunk = event.getWorld().getChunk(event.getPos());
				IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY,
						null);
				pollution.set(typePos, pollution.get(typePos) + delta, true);
				MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(chunk, ContaminationTypeRegistry.getAtPosition(typePos), pollution.get(typePos), delta));
			}
		}
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		// Only run full logic every second
		if(currentTicks < maxTicks) {
			currentTicks++;
		}
		else {
			WorldServer world = DimensionManager.getWorld(0);
			for(Iterator<Chunk> iterator = world
					.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator()); iterator.hasNext();) {
				Chunk chunk = iterator.next();
				// Begin contamination spread handling
				Chunk n1 = world.getChunk(chunk.x + 1, chunk.z);
				if(n1.isLoaded()) {
					trySpreadPollution(chunk, n1);
				}
				Chunk n2 = world.getChunk(chunk.x - 1, chunk.z);
				if(n2.isLoaded()) {
					trySpreadPollution(chunk, n2);
				}
				Chunk n3 = world.getChunk(chunk.x, chunk.z + 1);
				if(n3.isLoaded()) {
					trySpreadPollution(chunk, n3);
				}
				Chunk n4 = world.getChunk(chunk.x, chunk.z - 1);
				if(n4.isLoaded()) {
					trySpreadPollution(chunk, n4);
				}
				// Begin contamination effect handling
				IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY,
						null);
				for(int pos = 0; pos < ContaminationTypeRegistry.getNumberOfTypes(); pos++) {
					IContaminationType type = ContaminationTypeRegistry.getAtPosition(pos);
					int current = pollution.get(pos);
					if(current > 0) {
						for(IContaminationEffect effect : type.getEffectSet()) {
							if(effect instanceof IWorldTickEffect && current >= effect.getThreshold()) {
								((IWorldTickEffect) effect).triggerEffect(chunk);
								int red = effect.getReductionOnEffect(world.getDifficulty(), world.rand);
								if(red > 0) {
									pollution.set(pos, red, true);
									MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(chunk, ContaminationTypeRegistry.getAtPosition(pos), pollution.get(pos), red));
								}
							}
						}
					}
				}
			}
			currentTicks = 0;
		}
	}

	private static void trySpreadPollution(Chunk source, Chunk neighbour) {
		// TODO: The higher the pollution in a chunk the greater the chance to spread
		if(source.getWorld().rand.nextInt(200) == 0) {
			IContaminationHolder sourceC = source.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
			IContaminationHolder neighbourC = neighbour.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY,
					null);
			for(int pos = 0; pos < ContaminationTypeRegistry.getNumberOfTypes(); pos++) {
				if(sourceC.get(pos) > 1 && sourceC.get(pos) > neighbourC.get(pos)) {
					sourceC.set(pos, sourceC.get(pos) - 1, true);
					MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(source, ContaminationTypeRegistry.getAtPosition(pos), sourceC.get(pos), 1));
					neighbourC.set(pos, neighbourC.get(pos) + 1, true);
					MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(neighbour, ContaminationTypeRegistry.getAtPosition(pos), neighbourC.get(pos), 1));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityUpdate(LivingUpdateEvent event) {
		Chunk chunk = event.getEntityLiving().getEntityWorld().getChunk(event.getEntityLiving().getPosition());
		IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
		for(int pos = 0; pos < ContaminationTypeRegistry.getNumberOfTypes(); pos++) {
			IContaminationType type = ContaminationTypeRegistry.getAtPosition(pos);
			int current = pollution.get(pos);
			for(IContaminationEffect effect : type.getEffectSet()) {
				if(effect instanceof IEntityTickEffect) {
					if(current >= effect.getThreshold()) {
						((IEntityTickEffect) effect).triggerEffect(event.getEntityLiving(), current);
					}
				}
			}
		}
	}
}
