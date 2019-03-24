package xyz.brassgoggledcoders.contamination.modules.reader;

import com.teamacronymcoders.base.items.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import xyz.brassgoggledcoders.contamination.Contamination;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;
import xyz.brassgoggledcoders.contamination.api.types.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

public class ItemReader extends ItemBase {

	String typeName;
	boolean isDebug;

	public ItemReader(String type) {
		super("contamination_reader_" + type);
		setCreativeTab(CreativeTabs.MISC);
		typeName = type;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
			Chunk chunk = worldIn.getChunk(playerIn.getPosition());
			IContaminationHolder pollution = chunk.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
			if(typeName.equals("debug")) {
				for(IContaminationType type : ContaminationTypeRegistry.getAllTypes()) {
					playerIn.sendStatusMessage(new TextComponentString(type.getLocalizedName() + " "
							+ I18n.translateToLocal("contamination.name") + ": " + pollution.get(type)), false);
				}
			}
			else {
				IContaminationType type = ContaminationTypeRegistry.getFromName(typeName);
				playerIn.sendStatusMessage(new TextComponentString(type.getLocalizedName() + " "
						+ I18n.translateToLocal("contamination.name") + ": " + pollution.get(type)), true);

			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}

}
