package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import java.util.Random;

import com.google.gson.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.properties.EntityProperty;
import xyz.brassgoggledcoders.contamination.ContaminationMod;

public class EntityInBlock implements EntityProperty
{
	private final boolean inBlock;
	
	public EntityInBlock(boolean inBlock)
    {
        this.inBlock = inBlock;
    }

	@Override
	public boolean testProperty(Random random, Entity entityIn) {
		boolean flag = false;
		BlockPos blockpos = new BlockPos(entityIn);
        IBlockState iblockstate = entityIn.getEntityWorld().getBlockState(blockpos);
        if(iblockstate == ModuleFertilizer.algea) {
        	flag = true;
        }
		return flag == inBlock;
	}
	
	public static class Serializer extends EntityProperty.Serializer<EntityInBlock>
    {
        protected Serializer()
        {
            super(new ResourceLocation(ContaminationMod.MODID, "in_block"), EntityInBlock.class);
        }

        public JsonElement serialize(EntityInBlock property, JsonSerializationContext serializationContext)
        {
            return new JsonPrimitive(property.inBlock);
        }

        public EntityInBlock deserialize(JsonElement element, JsonDeserializationContext deserializationContext)
        {
            return new EntityInBlock(JsonUtils.getBoolean(element, ContaminationMod.MODID + ":in_block"));
        }
    }
}