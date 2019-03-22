package xyz.brassgoggledcoders.contamination;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.brassgoggledcoders.contamination.Contamination.ContaminationProvider;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;
import xyz.brassgoggledcoders.contamination.api.effect.*;
import xyz.brassgoggledcoders.contamination.api.modifiers.IContaminationInteracter;
import xyz.brassgoggledcoders.contamination.api.types.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;
import xyz.brassgoggledcoders.contamination.events.ContaminationUpdateEvent;
import xyz.brassgoggledcoders.contamination.network.PacketSyncContamination;

@EventBusSubscriber(modid = Contamination.MODID)
public class EventHandlerCommon {
	
	@SubscribeEvent
	public static void attachChunkCaps(AttachCapabilitiesEvent<Chunk> event) {
		// TODO Only attach the capability when an interacter is placed in the chunk?
		event.addCapability(new ResourceLocation(Contamination.MODID, "contamination_holder"),
				new ContaminationProvider(event.getObject()));
	}
	
	@SubscribeEvent
	public static void onUseItem(PlayerInteractEvent.RightClickBlock event) {
		if(!event.getWorld().isRemote) {
			ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
			int delta = 0;
			IContaminationType type = null;

			if(stack.hasCapability(Contamination.CONTAMINATION_INTERACTER_CAPABILITY, null)) {
				IContaminationInteracter interacter = stack
						.getCapability(Contamination.CONTAMINATION_INTERACTER_CAPABILITY, null);
				type = interacter.getType();
				delta = interacter.getContaminationModifier();
			}
			else {
				return;
			}

			if(delta != 0) {
				Chunk chunk = event.getWorld().getChunk(event.getPos());
				IContaminationHolder pollution = chunk.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY,
						null);
				pollution.modify(type, delta);
				MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(chunk, type, pollution.get(type), delta));
			}
		}
	}
	
	static int currentTicks;
	final static int maxTicks = 20;

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
				IContaminationHolder holder = chunk.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY,
						null);
				for(IContaminationType type : ContaminationTypeRegistry.getAllTypes()) {
					if(holder == null || type == null) {
						//What??
						return;
					}
					int current = holder.get(type);
					if(current > 0) {
						for(IContaminationEffect effect : type.getEffectSet(EnumEffectType.WORLDTICK)) {
							if(effect instanceof IWorldTickEffect && current >= effect.getThreshold()) {
								((IWorldTickEffect) effect).triggerEffect(chunk);
								int delta = effect.getReductionOnEffect(world.getDifficulty(), world.rand);
								if(delta > 0) {
									holder.modify(type, delta);
									MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(chunk, type, holder.get(type), delta));
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
			IContaminationHolder sourceC = source.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
			IContaminationHolder neighbourC = neighbour.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY,
					null);
			if(sourceC == null || neighbourC == null) {
				//What?
				return;
			}
			for(IContaminationType type : ContaminationTypeRegistry.getAllTypes()) {
				if(sourceC.get(type) > 1 && sourceC.get(type) > neighbourC.get(type)) {
					sourceC.modify(type, -1);
					MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(source, type, sourceC.get(type), 1));
					neighbourC.modify(type, 1);
					MinecraftForge.EVENT_BUS.post(new ContaminationUpdateEvent(neighbour, type, neighbourC.get(type), 1));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityUpdate(LivingUpdateEvent event) {
		Chunk chunk = event.getEntityLiving().getEntityWorld().getChunk(event.getEntityLiving().getPosition());
		IContaminationHolder holder = chunk.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
		//Modify contamination from contaminating entities
		if(event.getEntity().hasCapability(Contamination.CONTAMINATION_INTERACTER_CAPABILITY, null)) {
			IContaminationInteracter con = event.getEntity().getCapability(Contamination.CONTAMINATION_INTERACTER_CAPABILITY, null);
			holder.modify(con.getType(), con.getContaminationModifierPerTick());
		}
		//Trigger contamination effects onto entities
		for(IContaminationType type : ContaminationTypeRegistry.getAllTypes()) {
			if(holder == null || type == null) {
				//What??
				return;
			}
			int current = holder.get(type);
			for(IContaminationEffect effect : type.getEffectSet(EnumEffectType.ENTITYTICK)) {
				if(effect instanceof IEntityTickEffect) {
					if(current >= effect.getThreshold()) {
						((IEntityTickEffect) effect).triggerEffect(event.getEntityLiving(), current);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if(!event.getWorld().isRemote) {
			event.getWorld().addEventListener(new WorldEventListener((WorldServer) event.getWorld()));
		}
	}
	
	 @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent event) {
        EntityPlayer player = event.getPlayer();
        Chunk chunk = event.getChunkInstance();

        if(player != null && chunk != null) {
            Contamination.instance.getPacketHandler().sendToPlayer(new PacketSyncContamination(chunk.x, chunk.z, chunk.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null).writeToNBT()), (EntityPlayerMP) player);
        }
    }
}
