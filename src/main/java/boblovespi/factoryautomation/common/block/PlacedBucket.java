package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

/**
 * Created by Willi on 6/27/2018.
 */
public class PlacedBucket extends FABaseBlock
{
	public static final VoxelShape AXIS_ALIGNED_BB = Block
			.makeCuboidShape(4 / 16d, 0, 4 / 16d, 12 / 16d, 8 / 16d, 12 / 16d);

	public PlacedBucket()
	{
		super("placed_bucket", true, Properties.create(Material.IRON).hardnessAndResistance(0.2f), null);
		TileEntityHandler.tiles.add(TEPlacedBucket.class);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return AXIS_ALIGNED_BB;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TEPlacedBucket();
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
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
								FluidUtil.getFilledBucket(new FluidStack(fluid, 1000)));
						super.onReplaced(state, world, pos, newState, isMoving);
						return;
					}
				}
			}
			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BUCKET));
		}
		super.onReplaced(state, world, pos, newState, isMoving);
	}

}
