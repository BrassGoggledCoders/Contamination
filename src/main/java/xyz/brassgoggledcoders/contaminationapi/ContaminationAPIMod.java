package xyz.brassgoggledcoders.contaminationapi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ContaminationAPIMod.MODID, name = ContaminationAPIMod.MODNAME, version = ContaminationAPIMod.MODVERSION)
@EventBusSubscriber
public class ContaminationAPIMod {

	public static final String MODID = "contaminationapi";
	public static final String MODNAME = "ContaminationAPI";
	public static final String MODVERSION = "@VERSION@";
	
	@CapabilityInject(IContaminationHolder.class)
    public static Capability<IContaminationHolder> CONTAMINATION_HOLDER_CAPABILITY = null;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		CapabilityManager.INSTANCE.register(IContaminationHolder.class, new IContaminationHolder.Storage(), new IContaminationHolder.Factory());
	}
	
	@SubscribeEvent
	public static void attachChunkCaps(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(new ResourceLocation(MODID, "contamination"), new ContaminationProvider(event.getObject()));
	}

    public static class ContaminationProvider implements ICapabilitySerializable<NBTBase>
    {
        private final IContaminationHolder contamination;

        public ContaminationProvider(Chunk chunk)
        {
            contamination = new IContaminationHolder.SafeImplementation(chunk);
        }

        @Override
        public NBTBase serializeNBT() {
            return CONTAMINATION_HOLDER_CAPABILITY.getStorage().writeNBT(CONTAMINATION_HOLDER_CAPABILITY, contamination, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
        	CONTAMINATION_HOLDER_CAPABILITY.getStorage().readNBT(CONTAMINATION_HOLDER_CAPABILITY, contamination, null, nbt);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CONTAMINATION_HOLDER_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CONTAMINATION_HOLDER_CAPABILITY ? CONTAMINATION_HOLDER_CAPABILITY.cast(contamination) : null;
        }
}
    
    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote) {
            ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
            int delta = 0;
            if (stack.getItem() == Items.FLINT_AND_STEEL)
            {
                delta = 1;
            }
            else if (stack.getItem() == Item.getItemFromBlock(Blocks.SAPLING))
            {
                delta = -1;
            }

            if (delta != 0)
            {
                Chunk chunk = event.getWorld().getChunk(event.getPos());
                IContaminationHolder pollution = chunk.getCapability(CONTAMINATION_HOLDER_CAPABILITY, null);
                pollution.set(pollution.get() + delta, true);

                event.getEntityPlayer().sendStatusMessage(new TextComponentString("Chunk pollution: " + pollution.get()), true);
            }
        }
}
}
