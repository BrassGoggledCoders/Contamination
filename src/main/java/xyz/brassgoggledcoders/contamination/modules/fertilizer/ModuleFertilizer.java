package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import java.awt.Color;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import xyz.brassgoggledcoders.contamination.*;
import xyz.brassgoggledcoders.contamination.ContaminationMod.ContaminationInteracterProvider;
import xyz.brassgoggledcoders.contamination.api.types.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;
import xyz.brassgoggledcoders.contamination.effects.EffectPotion;

@Module(value = ContaminationMod.MODID)
@EventBusSubscriber(modid = ContaminationMod.MODID) //TODO This won't get disabled when the module is disabled
@ObjectHolder(ContaminationMod.MODID)
public class ModuleFertilizer extends ModuleBase {
	
	public static final Block algea = null;
	static IContaminationType fertilizer = new ContaminationType("fertilizer", Color.WHITE.getRGB(), new EffectPotion(70, "poison", true), new DirtDecayEffect(), new OvergrowthEffect());
	
	@Override
    public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ContaminationTypeRegistry.addContaminationType(fertilizer);
		LootTableList.register(new ResourceLocation(ContaminationMod.MODID, "algea_fishing"));
		EntityPropertyManager.registerProperty(new EntityInBlock.Serializer());
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
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int networkID = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(ContaminationMod.MODID, "fertilizer_creeper"), EntityFertilizerCreeper.class,
				"fertilizer_creeper", networkID++, ContaminationMod.MODID, 64, 1, true);
		EntityRegistry.addSpawn(EntityFertilizerCreeper.class, 100, 1, 2, EnumCreatureType.MONSTER, BiomeDictionary.getBiomes(BiomeDictionary.Type.WET).toArray(new Biome[0]));
		EntityRegistry.addSpawn(EntityFertilizerCreeper.class, 100, 1, 2, EnumCreatureType.MONSTER, BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE).toArray(new Biome[0]));
		EntityRegistry.addSpawn(EntityFertilizerCreeper.class, 150, 1, 2, EnumCreatureType.MONSTER, BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH).toArray(new Biome[0]));
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
	
	//Slightly hacky, but stops me having to copy paste the whole creeper class or use an AT. TODO: Investigate performance impact
	@SubscribeEvent
	public static void grief(EntityMobGriefingEvent event) {
		if(event.getEntity() instanceof EntityFertilizerCreeper) {
			event.setResult(Result.DENY);
		}
	}
	
	
	@Override
	public String getName() {
		return "Fertilizer Runoff";
	}

}
