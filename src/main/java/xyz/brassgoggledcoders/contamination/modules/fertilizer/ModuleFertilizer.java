package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import java.awt.Color;
import java.util.Iterator;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.brassgoggledcoders.contamination.ContaminationMod;
import xyz.brassgoggledcoders.contamination.ContaminationMod.ContaminationInteracterProvider;
import xyz.brassgoggledcoders.contamination.ContaminationType;
import xyz.brassgoggledcoders.contamination.api.*;

@Module(value = ContaminationMod.MODID)
@EventBusSubscriber(modid = ContaminationMod.MODID) //TODO This won't get disabled when the module is disabled
public class ModuleFertilizer extends ModuleBase {

	static IContaminationType fertilizer = new ContaminationType("fertilizer", Color.WHITE.getRGB(), new EffectPoison(), new DirtDecayEffect());
	
	@Override
    public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ContaminationTypeRegistry.addContaminationType(fertilizer);
	}
	
	@SubscribeEvent
	public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() == Items.DYE && EnumDyeColor.byDyeDamage(event.getObject().getMetadata()) == EnumDyeColor.WHITE) {
			event.addCapability(new ResourceLocation(ContaminationMod.MODID, "contamination_interacter"), new ContaminationInteracterProvider(fertilizer, 1));
		}
	}
	
	@SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote) {
            ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
            int delta = 0;
            IContaminationType type = null;
            
            if (stack.hasCapability(ContaminationMod.CONTAMINATION_INTERACTER_CAPABILITY, null)) {
            	IContaminationInteracter interacter = stack.getCapability(ContaminationMod.CONTAMINATION_INTERACTER_CAPABILITY, null);
            	type = interacter.getType();
            	delta = interacter.getContaminationModifier();
            }
            else {
            	return;
            }

            if(delta != 0) {
            	int typePos = ContaminationTypeRegistry.getPosition(type);
                Chunk chunk = event.getWorld().getChunk(event.getPos());
            	IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
            	pollution.set(typePos, pollution.get(typePos) + delta, true);
            	event.getEntityPlayer().sendStatusMessage(new TextComponentString(type.getName() + " pollution: " + pollution.get(typePos)), true);
            }
        }
    }
	
	@SubscribeEvent
	public static void onPlayerUpdate(LivingUpdateEvent event) {
			Chunk chunk = event.getEntityLiving().getEntityWorld().getChunk(event.getEntityLiving().getPosition());
        	IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
        	int current = pollution.get(ContaminationTypeRegistry.getPosition(fertilizer));
        	for(IContaminationEffect effect : fertilizer.getEffectSet()) {
        		if(current >= effect.getThreshold()) {
        			effect.triggerEffect(event.getEntityLiving(), current);
        		}
        	}
	}
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		WorldServer world = DimensionManager.getWorld(0);
		for (Iterator<Chunk> iterator = world.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator()); iterator.hasNext();)
        {
            Chunk chunk = iterator.next();
            IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
            int current = pollution.get(ContaminationTypeRegistry.getPosition(fertilizer));
            if(current > 0) {
            	for(IContaminationEffect effect : fertilizer.getEffectSet()) {
            		if(effect instanceof IWorldTickEffect && current >= effect.getThreshold()) {
            			effect.triggerEffect(chunk);
            		}
            	}
            }
        }
	}
	
	@Override
	public String getName() {
		return "Fertilizer Runoff Pollution";
	}

}
