package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import xyz.brassgoggledcoders.contamination.ContaminationMod;
import xyz.brassgoggledcoders.contamination.api.effect.IWorldTickEffect;

public class OvergrowthEffect implements IWorldTickEffect {

	@Override
	public int getThreshold() {
		return 30;
	}

	@Override
	public void triggerEffect(Chunk chunk) {
		World world = chunk.getWorld();
		if(world.isRemote) {
			return;
		}
		int x = chunk.x * 16 + world.rand.nextInt(16);
		int z = chunk.z * 16 + world.rand.nextInt(16);
        BlockPos randomPos = new BlockPos(x, chunk.getHeight(new BlockPos(x, 0, z)), z);
        randomPos = randomPos.down();
        if(world.isAreaLoaded(randomPos, 1) && world.getBlockState(randomPos).getBlock() == Blocks.DIRT && (world.isAirBlock(randomPos.up()) || world.getBlockState(randomPos.up()).getBlock() == Blocks.TALLGRASS) && world.isAirBlock(randomPos.up(2))) {
        	world.setBlockState(randomPos.up(), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 2);
        	world.setBlockState(randomPos.up(2), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 3);
        	chunk.markDirty();
            ContaminationMod.instance.getLogger().devInfo(randomPos.toString());
        }
	}

	@Override
	public int getReductionOnEffect(EnumDifficulty enumDifficulty, Random rand) {
		if(EnumDifficulty.HARD.equals(enumDifficulty)) {
			return 0;
		}
		return rand.nextInt(2);
	}

}
