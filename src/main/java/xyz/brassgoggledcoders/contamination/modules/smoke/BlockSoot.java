package xyz.brassgoggledcoders.contamination.modules.smoke;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamacronymcoders.base.blocks.BlockBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.*;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//TODO If soot falls on soot it should merge
public class BlockSoot extends BlockBase {

	public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
	// TODO Fix bad mojang code
	protected static final AxisAlignedBB[] AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D) };

	public BlockSoot() {
		super(Material.SNOW, "soot");
		setDefaultState(blockState.getBaseState().withProperty(LAYERS, Integer.valueOf(1)));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB[state.getValue(LAYERS).intValue()];
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getValue(LAYERS).intValue() < 5;
	}

	@Deprecated
	@Override
	public boolean isTopSolid(IBlockState state) {
		return state.getValue(LAYERS).intValue() == 8;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		int i = blockState.getValue(LAYERS).intValue() - 1;
		AxisAlignedBB axisalignedbb = blockState.getBoundingBox(worldIn, pos);
		return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX,
				i * 0.125F, axisalignedbb.maxZ);
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
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should
	 * perform any checks during a neighbor change. Cases may include when redstone
	 * power is updated, cactus blocks popping off due to a neighboring solid block,
	 * etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(!worldIn.isRemote) {
			checkFallable(worldIn, pos);
		}
	}

	private void checkFallable(World worldIn, BlockPos pos) {
		if(!worldIn.isRemote) {
			if((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())))
					&& pos.getY() >= 0) {
				// if (worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
				// {
				EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, pos.getX() + 0.5D, pos.getY(),
						pos.getZ() + 0.5D, worldIn.getBlockState(pos));
				worldIn.spawnEntity(entityfallingblock);
			}
			// }
		}
	}
	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World worldIn) {
		return 2;
	}

	public static boolean canFallThrough(IBlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER
				|| material == Material.LAVA;
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
	}

	@SuppressWarnings("deprecation")
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		if(side == EnumFacing.UP) {
			return true;
		}
		else {
			IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
			return iblockstate.getBlock() == this
					&& iblockstate.getValue(LAYERS).intValue() >= blockState.getValue(LAYERS).intValue() ? false
							: super.shouldSideBeRendered(blockState, blockAccess, pos, side);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LAYERS, Integer.valueOf((meta & 7) + 1));
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getValue(LAYERS).intValue() == 1;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(LAYERS).intValue() - 1;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { LAYERS });
	}

}
