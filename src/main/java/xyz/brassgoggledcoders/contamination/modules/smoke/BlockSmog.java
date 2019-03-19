package xyz.brassgoggledcoders.contamination.modules.smoke;

import java.util.Random;

import com.teamacronymcoders.base.blocks.BlockBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSmog extends BlockBase {

	public BlockSmog() {
		super(Material.AIR, "smog");
		this.setTickRandomly(true);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return Block.NULL_AABB;
    }
	
	@Override
	public boolean causesSuffocation(IBlockState state)
    {
        return true;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
		//Try to drift upwards
		if(worldIn.isAirBlock(pos.up()) || worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())) {
			worldIn.setBlockState(pos.up(), ModuleSmoke.smog.getDefaultState(), 2);
			worldIn.setBlockToAir(pos);
		}
		//TODO Implement several levels of smog with varying thicknesses, and the thinnest will not spread
//		int x = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
//		int y = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
//		int z = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
//		BlockPos target = pos.add(x, y, z);
//		if(worldIn.isAirBlock(target) || worldIn.getBlockState(target).getBlock().isReplaceable(worldIn, target)) {
//			worldIn.setBlockState(target, ModuleSmoke.smog.getDefaultState(), 2);
//		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
	
}
