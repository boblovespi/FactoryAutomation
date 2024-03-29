package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 6/27/2018.
 */
public class PlacedBucket extends FABaseBlock implements EntityBlock
{
	public static final VoxelShape AXIS_ALIGNED_BB = Block
			.box(4, 0, 4, 12, 8, 12);

	public PlacedBucket()
	{
		super("placed_bucket", true, Properties.of(Material.METAL).strength(0.2f), null);
		TileEntityHandler.tiles.add(TEPlacedBucket.class);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return AXIS_ALIGNED_BB;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEPlacedBucket(pos, state);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			BlockEntity te = world.getBlockEntity(pos);
			if (te != null)
			{
				if (te instanceof TEPlacedBucket)
				{
					Fluid fluid = ((TEPlacedBucket) te).GetFluid();
					if (fluid != null)
					{
						Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(),
								FluidUtil.getFilledBucket(new FluidStack(fluid, 1000)));
						super.onRemove(state, world, pos, newState, isMoving);
						return;
					}
				}
			}
			Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BUCKET));
		}
		super.onRemove(state, world, pos, newState, isMoving);
	}

}
