package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Willi on 12/27/2018.
 */
public class Campfire extends FABaseBlock
{
	public static final PropertyBool LIT = PropertyBool.create("lit");
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(
			3 / 16d, 0, 3 / 16d, 13 / 16d, 6 / 16d, 13 / 16d);

	public Campfire()
	{
		super(Materials.WOOD_MACHINE, "campfire", FACreativeTabs.primitive);
		setDefaultState(getDefaultState().withProperty(LIT, false));
		TileEntityHandler.tiles.add(TECampfire.class);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getValue(LIT) ? 11 : 0;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TECampfire();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, LIT);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(LIT, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(LIT) ? 1 : 0;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TECampfire)
		{
			((TECampfire) te).DropItems();
		}

		super.breakBlock(worldIn, pos, state);
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			boolean canLight = !state.getValue(LIT);
			ItemStack stack = player.getHeldItem(hand);
			Item item = stack.getItem();
			if (canLight && (item == Item.getItemFromBlock(Blocks.TORCH) || item == Items.FLINT_AND_STEEL
					|| item == FAItems.advancedFlintAndSteel.ToItem()))
			{
				world.setBlockState(pos, state.withProperty(LIT, true));
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TECampfire)
					((TECampfire) te).SetLit(true);
			} else
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TECampfire)
					((TECampfire) te).TakeOrPlace(stack, player);
			}
		}
		return true;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if (!state.getValue(LIT))
			return;
		if (rand.nextDouble() < 0.1D)
		{
			world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
					SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.2;
		double z = pos.getZ() + 0.5;
		world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0.02, 0);
		world.spawnParticle(
				EnumParticleTypes.SMOKE_NORMAL, x, y, z, rand.nextDouble() / 20d, 0.15, rand.nextDouble() / 20d);
	}
}
