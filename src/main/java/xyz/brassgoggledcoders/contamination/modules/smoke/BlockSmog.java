package xyz.brassgoggledcoders.contamination.modules.smoke;

import java.util.Random;

import com.teamacronymcoders.base.blocks.BlockBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSmog extends BlockBase {

	int level;

	public BlockSmog(String name) {
		super(Material.AIR, "smog_" + name);
		setTickRandomly(true);
		if(name == "source") {
			level = 0;
		}
		else if(name == "thick") {
			level = 1;
		}
		else {
			level = 2;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return Block.NULL_AABB;
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return true;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		// Try to drift upwards but stop at about Y=100
		if(pos.getY() <= 100) {
			// Intentionally using == Blocks.AIR check to prevent smog replacing
			// itself/logic blocks.
			if(worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
				worldIn.setBlockState(pos.up(), worldIn.getBlockState(pos), 2);
				worldIn.setBlockToAir(pos);
			}
		}

		int x = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
		int y = -random.nextInt(3);
		int z = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
		BlockPos target = pos.add(x, y, z);
		// Intentionally using == Blocks.AIR check to prevent smog replacing
		// itself/logic blocks.
		if(random.nextInt(10) == 0 && worldIn.getBlockState(target).getBlock() == Blocks.AIR) {
			// Source -> Thick/Thin
			if(level == 0) {
				if(random.nextBoolean()) {
					worldIn.setBlockState(target, ModuleSmoke.smog_thick.getDefaultState(), 2);
				}
				else {
					worldIn.setBlockState(target, ModuleSmoke.smog_thin.getDefaultState(), 2);
				}
			}
			// Thick -> Thin
			else if(level == 1) {
				worldIn.setBlockState(target, ModuleSmoke.smog_thin.getDefaultState(), 2);
			}
			// Thin may decay
			// else {
			// worldIn.setBlockToAir(pos);
			// }
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();
		return block != this;
	}
}
