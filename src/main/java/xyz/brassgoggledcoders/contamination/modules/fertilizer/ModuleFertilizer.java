package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import java.awt.Color;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.brassgoggledcoders.contamination.*;
import xyz.brassgoggledcoders.contamination.ContaminationMod.ContaminationInteracterProvider;
import xyz.brassgoggledcoders.contamination.api.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.IContaminationType;

@Module(value = ContaminationMod.MODID)
@EventBusSubscriber(modid = ContaminationMod.MODID) //TODO This won't get disabled when the module is disabled
public class ModuleFertilizer extends ModuleBase {

	static IContaminationType fertilizer = new ContaminationType("fertilizer", Color.WHITE.getRGB(), new EffectPotion(70, "poison", true), new DirtDecayEffect());
	
	@Override
    public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ContaminationTypeRegistry.addContaminationType(fertilizer);
	}
	
	@Override
	public void registerBlocks(ConfigRegistry config, BlockRegistry blocks) {
		Fluid algea = new Fluid("algea", new ResourceLocation(ContaminationMod.MODID, "fluids/algea"),
				new ResourceLocation(ContaminationMod.MODID, "fluids/algea"));
		FluidRegistry.registerFluid(algea);
		FluidRegistry.addBucketForFluid(algea);
		blocks.register(new BlockAlgeaFluid("algea", FluidRegistry.getFluid("algea"),
				Material.WATER, DamageSource.DROWN, 2));
	}
	
	@Override
	public String getClientProxyPath() {
		return "xyz.brassgoggledcoders.contamination.modules.fertilizer.ClientProxy";
	}
	
	@SubscribeEvent
	public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() == Items.DYE && EnumDyeColor.byDyeDamage(event.getObject().getMetadata()) == EnumDyeColor.WHITE) {
			event.addCapability(new ResourceLocation(ContaminationMod.MODID, "contamination_interacter"), new ContaminationInteracterProvider(fertilizer, 1));
		}
	}
	
	@Override
	public String getName() {
		return "Fertilizer Runoff";
	}

}
