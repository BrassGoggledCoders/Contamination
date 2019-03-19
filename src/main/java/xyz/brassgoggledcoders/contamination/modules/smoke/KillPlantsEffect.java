package xyz.brassgoggledcoders.contamination.modules.smoke;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.IPlantable;
import xyz.brassgoggledcoders.contamination.Contamination;
import xyz.brassgoggledcoders.contamination.api.effect.IWorldTickEffect;

public class KillPlantsEffect implements IWorldTickEffect {

	@Override
	public int getThreshold() {
		return 70;
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
        if(world.isAreaLoaded(randomPos, 1) && world.getBlockState(randomPos).getBlock() instanceof IPlantable) {
        	world.setBlockToAir(randomPos);
        	chunk.markDirty();
            Contamination.instance.getLogger().devInfo(randomPos.toString());
        }
	}

	@Override
	public int getReductionOnEffect(EnumDifficulty enumDifficulty, Random rand) {
		if(EnumDifficulty.HARD.equals(enumDifficulty)) {
			return 0;
		}
		return rand.nextInt(4);
	}

}
