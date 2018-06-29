package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

/**
 * Created by Willi on 6/27/2018.
 */
public class PlacedBucket extends Block implements ITileEntityProvider, FABlock
{
	public static final AxisAlignedBB AXIS_ALIGNED_BB = new AxisAlignedBB(
			4 / 16d, 0, 4 / 16d, 10 / 16d, 8 / 16d, 10 / 16d);

	public PlacedBucket()
	{
		super(Material.IRON);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setResistance(10000);
		FABlocks.blocks.add(this);
		TileEntityHandler.tiles.add(TEPlacedBucket.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEPlacedBucket();
	}

	/**
	 * This gets a complete list of items dropped from this block.
	 */
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te != null)
		{
			if (te instanceof TEPlacedBucket)
			{
				Fluid fluid = ((TEPlacedBucket) te).GetFluid();
				if (fluid != null)
				{
					drops.add(FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME)));
					return;
				}
			}
		}
		drops.add(new ItemStack(Items.BUCKET));
	}

	@Override
	public String UnlocalizedName()
	{
		return "bucket";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AXIS_ALIGNED_BB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
