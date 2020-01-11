package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
			4 / 16d, 0, 4 / 16d, 12 / 16d, 8 / 16d, 12 / 16d);

	public PlacedBucket()
	{
		super(Material.IRON);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setHardness(0.2f);
		setHarvestLevel("pickaxe", 0);
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
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
	{
		return AXIS_ALIGNED_BB;
	}

	@Override
	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	/**
	 * @return true if the state occupies all of its 1x1x1 cube
	 */
	@Override
	public boolean isFullBlock(BlockState state)
	{
		return false;
	}

	/**
	 * Return true if the block is a normal, solid cube.  This
	 * determines indirect power state, entity ejection from blocks, and a few
	 * others.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return True if the block is a full cube
	 */
	@Override
	public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	@Override
	public void breakBlock(World world, BlockPos pos, BlockState state)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te != null)
		{
			if (te instanceof TEPlacedBucket)
			{
				Fluid fluid = ((TEPlacedBucket) te).GetFluid();
				if (fluid != null)
				{
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(),
							FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME)));
					super.breakBlock(world, pos, state);
					return;
				}
			}
		}
		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BUCKET));
		super.breakBlock(world, pos, state);
	}
}
