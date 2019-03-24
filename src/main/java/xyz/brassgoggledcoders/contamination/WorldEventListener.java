package xyz.brassgoggledcoders.contamination;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;
import xyz.brassgoggledcoders.contamination.modules.smoke.ModuleSmoke;

public class WorldEventListener implements IWorldEventListener {
	private final WorldServer world;

	public WorldEventListener(WorldServer worldServerIn) {
		world = worldServerIn;
	}

	@Override
	public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
		if(newState.getBlock() == Blocks.FIRE || newState.getBlock() == Blocks.LAVA) {
			IContaminationHolder holder = worldIn.getChunk(pos)
					.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
			if(holder == null) {
				// What?
				return;
			}
			holder.modify(ModuleSmoke.smoke, 1);
		}
		else if(newState.getBlock() == Blocks.LIT_FURNACE) {
			IContaminationHolder holder = worldIn.getChunk(pos)
					.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
			if(holder == null) {
				// What?
				return;
			}
			holder.modify(ModuleSmoke.smoke, 3);
		}
	}

	@Override
	public void notifyLightSet(BlockPos pos) {

	}

	@Override
	public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playSoundToAllNearExcept(EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x,
			double y, double z, float volume, float pitch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playRecord(SoundEvent soundIn, BlockPos pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord,
			double xSpeed, double ySpeed, double zSpeed, int... parameters) {
		// TODO This has to be dome with packets because these particles don't get
		// spawned on the server
		// if(particleID == EnumParticleTypes.SMOKE_LARGE.getParticleID()) {
		// int smoke = ContaminationTypeRegistry.getPosition(ModuleSmoke.smoke);
		// IContaminationHolder holder = world.getChunk(new BlockPos(xCoord, yCoord,
		// zCoord)).getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY,
		// null);
		// holder.set(smoke, holder.get(smoke) + 2, true);
		// }
		// else if(particleID == EnumParticleTypes.SMOKE_NORMAL.getParticleID() ||
		// particleID == EnumParticleTypes.FLAME.getParticleID()) {
		// int smoke = ContaminationTypeRegistry.getPosition(ModuleSmoke.smoke);
		// IContaminationHolder holder = world.getChunk(new BlockPos(xCoord, yCoord,
		// zCoord)).getCapability(ContaminationMod.CONTAMINATION_HOLDER_CAPABILITY,
		// null);
		// holder.set(smoke, holder.get(smoke) + 1, true);
		// }
	}

	@Override
	public void spawnParticle(int id, boolean ignoreRange, boolean minimiseParticleLevel, double x, double y, double z,
			double xSpeed, double ySpeed, double zSpeed, int... parameters) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntityAdded(Entity entityIn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntityRemoved(Entity entityIn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void broadcastSound(int soundID, BlockPos pos, int data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
		// TODO Auto-generated method stub

	}

}
