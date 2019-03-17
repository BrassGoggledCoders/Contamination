package xyz.brassgoggledcoders.contamination.modules.reader;

import com.teamacronymcoders.base.items.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import xyz.brassgoggledcoders.contamination.ContaminationMod;
import xyz.brassgoggledcoders.contamination.api.*;

public class ItemReader extends ItemBase {

	int contaminationPos;
	boolean isDebug;
	
	public ItemReader(int contaminationPos) {
		super("contamination_reader" + contaminationPos);
		this.setCreativeTab(CreativeTabs.MISC);
		this.contaminationPos = contaminationPos;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(!worldIn.isRemote) {
			Chunk chunk = worldIn.getChunk(playerIn.getPosition());
	    	IContaminationHolder pollution = chunk.getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY, null);
	    	if(contaminationPos > 0) {
	    		 IContaminationType type = ContaminationTypeRegistry.getAtPosition(contaminationPos);
	    		 playerIn.sendStatusMessage(new TextComponentString(type.getName() + " pollution: " + pollution.get(contaminationPos)), true);
	    	}
	    	else {
		    	for(int pos = 0; pos < ContaminationTypeRegistry.getNumberOfTypes(); pos++) {
		            IContaminationType type = ContaminationTypeRegistry.getAtPosition(pos);
		            playerIn.sendStatusMessage(new TextComponentString(type.getName() + " pollution: " + pollution.get(pos)), false);
		    	}
	    	}
    	}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

}
