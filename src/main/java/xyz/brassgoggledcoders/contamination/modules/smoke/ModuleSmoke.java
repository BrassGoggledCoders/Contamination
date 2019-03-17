package xyz.brassgoggledcoders.contamination.modules.smoke;

import java.awt.Color;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.brassgoggledcoders.contamination.*;
import xyz.brassgoggledcoders.contamination.ContaminationMod.ContaminationInteracterProvider;
import xyz.brassgoggledcoders.contamination.api.*;
import xyz.brassgoggledcoders.contamination.api.effect.IContaminationEffect;

@Module(value = ContaminationMod.MODID)
@EventBusSubscriber(modid = ContaminationMod.MODID) //TODO This won't get disabled when the module is disabled
public class ModuleSmoke extends ModuleBase {

	static IContaminationType smoke = new ContaminationType("smoke", Color.BLACK.getRed(), new ContaminationEffectTest(), new ContaminationEffectBlindness());
	
	@Override
    public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ContaminationTypeRegistry.addContaminationType(smoke);
	}
	
	@SubscribeEvent
	public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() == Items.FLINT_AND_STEEL) {
			event.addCapability(new ResourceLocation(ContaminationMod.MODID, "contamination_interacter"), new ContaminationInteracterProvider(smoke, 1));
		}
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			Chunk chunk = player.getEntityWorld().getChunk(player.getPosition());
        	IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
        	int current = pollution.get(ContaminationTypeRegistry.getPosition(smoke));
        	for(IContaminationEffect effect : smoke.getEffectSet()) {
        		if(current >= effect.getThreshold()) {
        			effect.triggerEffect(player);
        		}
        	}
		}
	}
	
	@Override
	public String getName() {
		return "Smoke";
	}

}
