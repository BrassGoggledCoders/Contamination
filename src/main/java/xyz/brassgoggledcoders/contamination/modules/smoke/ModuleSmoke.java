package xyz.brassgoggledcoders.contamination.modules.smoke;

import java.awt.Color;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import xyz.brassgoggledcoders.contamination.Contamination;
import xyz.brassgoggledcoders.contamination.Contamination.ContaminationInteracterProvider;
import xyz.brassgoggledcoders.contamination.ContaminationType;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;
import xyz.brassgoggledcoders.contamination.api.types.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;
import xyz.brassgoggledcoders.contamination.effects.EffectOverlay;
import xyz.brassgoggledcoders.contamination.effects.EffectPotion;

@Module(value = Contamination.MODID)
@EventBusSubscriber(modid = Contamination.MODID) //TODO This won't get disabled when the module is disabled
@ObjectHolder(Contamination.MODID)
public class ModuleSmoke extends ModuleBase {

	public static IContaminationType smoke = new ContaminationType("smoke", Color.BLACK.getRed(), new EffectPotion(500, "blindness", false), new KillLeavesEffect(), new KillPlantsEffect(), new EffectOverlay(100, new ResourceLocation(Contamination.MODID, "textures/gui/overlay/smoke.png")));
	public static final Block smog_source = null;
	public static final Block smog_thick = null;
	public static final Block smog_thin = null;
	
	@Override
    public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ContaminationTypeRegistry.addContaminationType(smoke);
	}
	
	@SubscribeEvent
	public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof EntityBlaze) {
			event.addCapability(new ResourceLocation(Contamination.MODID, "contamination_interacter"), new ContaminationInteracterProvider(smoke, 1));
		}
	}
	
	@SubscribeEvent
	public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() == Items.FLINT_AND_STEEL) {
			event.addCapability(new ResourceLocation(Contamination.MODID, "contamination_interacter"), new ContaminationInteracterProvider(smoke, 1));
		}
	}
	
	@Override
	public void registerBlocks(ConfigRegistry config, BlockRegistry blocks) {
		blocks.register(new BlockSmog("source"));
		blocks.register(new BlockSmog("thick"));
		blocks.register(new BlockSmog("thin"));
	}

	@Override
	public String getName() {
		return "Smoke";
	}
	
	//TODO Make generic
	@SubscribeEvent
	public static void onBlockPlaced(BlockEvent.PlaceEvent event) {
		if(event.getPlacedBlock().getBlock() == Blocks.TORCH) {
			IContaminationHolder holder = event.getPlayer().getEntityWorld().getChunk(event.getPos()).getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
			holder.modify(smoke, 1);
		}
	}
}
