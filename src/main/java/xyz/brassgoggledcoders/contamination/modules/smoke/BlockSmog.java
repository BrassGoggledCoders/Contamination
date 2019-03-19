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

	int level;
	
	public BlockSmog(String name) {
		super(Material.AIR, "smog_" + name);
		this.setTickRandomly(true);
		if(name == "source") {
			this.level = 0;
		}
		else if(name == "thick") {
			this.level = 1;
		}
		else {
			this.level = 2;
		}
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
		//Try to drift upwards but stop at about Y=100
		if(pos.getY() <= 100) {
			if(worldIn.isAirBlock(pos.up()) || worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())) {
				worldIn.setBlockState(pos.up(), worldIn.getBlockState(pos), 2);
				worldIn.setBlockToAir(pos);
			}
		}
		
		int x = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
		int y = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
		int z = random.nextBoolean() ? random.nextInt(2) : -random.nextInt(2);
		BlockPos target = pos.add(x, y, z);
		if(random.nextInt(10) == 0 && worldIn.isAirBlock(target)) {
			//Source -> Thick/Thin
			if(level == 0) {
				if(random.nextBoolean()) {
					worldIn.setBlockState(target, ModuleSmoke.smog_thick.getDefaultState(), 2);
				}
				else {
					worldIn.setBlockState(target, ModuleSmoke.smog_thin.getDefaultState(), 2);
				}
			}
			//Thick -> Thin
			else if(level == 1) {
				worldIn.setBlockState(target, ModuleSmoke.smog_thin.getDefaultState(), 2);
			}
			//Thin may decay
//			else {
//				worldIn.setBlockToAir(pos);
//			}
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
	
	@Override
	public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }
}
