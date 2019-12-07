package boblovespi.factoryautomation.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConcreteSlab extends SlabBlock implements FABlock
{
	//TODO Finish
	public ConcreteSlab()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(10, 1000));
		//		super(Material.ROCK);
		//		isDouble = isDoubleSlab;
		//		IBlockState state = blockState.getBaseState();
		//		if (!isDouble())
		//			state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
		//
		//		setDefaultState(state.withProperty(VARIANT, Variant.DEFAULT));
		//
		//		setUnlocalizedName(UnlocalizedName());
		//		setRegistryName(RegistryName());
		//		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		//		setHardness(10);
		//		setResistance(10000);

		FABlocks.blocks.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return "concrete_slab";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			((PlayerEntity) entity).addPotionEffect(new EffectInstance(Effects.SPEED, 10, 1));
		}
	}
}
