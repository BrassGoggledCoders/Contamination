package xyz.brassgoggledcoders.contamination;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.brassgoggledcoders.contamination.ContaminationMod.ContaminationInteracterProvider;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;

@Module(value = ContaminationMod.MODID)
@EventBusSubscriber(modid = ContaminationMod.MODID) //TODO This won't get disabled when the module is disabled
public class ModuleVanilla extends ModuleBase {

	@SubscribeEvent
	public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() == Items.FLINT_AND_STEEL) {
			event.addCapability(new ResourceLocation(ContaminationMod.MODID, "contamination_interacter"), new ContaminationInteracterProvider(1));
		}
	}
	
	@SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote) {
            ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
            int delta = 0;
            if (stack.hasCapability(ContaminationMod.CONTAMINATION_INTERACTER_CAPABILITY, null)) {
            	delta = stack.getCapability(ContaminationMod.CONTAMINATION_INTERACTER_CAPABILITY, null).getContaminationModifier();
            }

            if(delta != 0) {
                Chunk chunk = event.getWorld().getChunk(event.getPos());
            	IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
            	pollution.set(pollution.get() + delta, true);
            	event.getEntityPlayer().sendStatusMessage(new TextComponentString("Chunk pollution: " + pollution.get()), true);
            }
        }
    }
	
	@Override
	public String getName() {
		return "Vanilla Pollution";
	}

}
