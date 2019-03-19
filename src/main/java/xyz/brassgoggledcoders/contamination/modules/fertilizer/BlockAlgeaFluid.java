package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import com.teamacronymcoders.base.blocks.BlockFluidBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import xyz.brassgoggledcoders.contamination.Contamination;

public class BlockAlgeaFluid extends BlockFluidBase {

	String name;
	DamageSource source;
	int damage;

	//TODO When this displaces water, have it take that water's level
	public BlockAlgeaFluid(String name, Fluid fluid, Material material, DamageSource source, int damage) {
		super(name, fluid, material);
		this.name = name;
		this.source = source;
		this.damage = damage;
		this.displacements.put(Blocks.WATER, true);
		this.displacements.put(Blocks.FLOWING_WATER, true);
	}

	@Override
	public ResourceLocation getResourceLocation(IBlockState blockState) {
		return new ResourceLocation(Contamination.MODID, name);
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if(entityIn instanceof EntityLivingBase) {
			if(entityIn.isInsideOfMaterial(Material.WATER)) {
				entityIn.attackEntityFrom(source, damage);
			}
		}
	}
}
