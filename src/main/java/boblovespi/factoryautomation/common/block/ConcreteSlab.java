package boblovespi.factoryautomation.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class ConcreteSlab extends SlabBlock implements FABlock
{
	public static final PropertyEnum<Variant> VARIANT = PropertyEnum
			.create("variant", Variant.class);

	private boolean isDouble;

	//TODO Finish
	public ConcreteSlab(boolean isDoubleSlab)
	{
		super(Material.ROCK);
		isDouble = isDoubleSlab;
		IBlockState state = blockState.getBaseState();
		if (!isDouble())
			state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);

		setDefaultState(state.withProperty(VARIANT, Variant.DEFAULT));

		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(10);
		setResistance(10000);

		FABlocks.blocks.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		if (isDouble())
			return "concrete_double_slab";
		else
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
		if (entity instanceof EntityPlayer)
		{
			((EntityPlayer) entity)
					.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 1));
		}
	}

	@Override
	public String getUnlocalizedName(int meta)
	{
		return super.getUnlocalizedName();
	}

	@Override
	public boolean isDouble()
	{
		return isDouble;
	}

	@Override
	public IProperty<?> getVariantProperty()
	{
		return VARIANT;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return !isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState()
				.withProperty(VARIANT, Variant.DEFAULT);

		if (!isDouble())
			state = state.withProperty(HALF,
					(meta & 1) == 0 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);

		return state;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack)
	{
		return Variant.DEFAULT;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(FABlocks.concreteSlab.ToBlock());
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return isDouble() ?
				new BlockStateContainer(this, VARIANT) :
				new BlockStateContainer(this, HALF, VARIANT);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(FABlocks.concreteSlab.ToBlock());
	}

	public BlockSlab ToBlockSlab()
	{
		return this;
	}

	public enum Variant implements IStringSerializable
	{
		DEFAULT;

		@Override
		public String getName()
		{
			return "default";
		}
	}

	public static class Half extends ConcreteSlab
	{

		public Half()
		{
			super(false);
		}
	}

	public static class Double extends ConcreteSlab
	{

		public Double()
		{
			super(true);
		}
	}
}
