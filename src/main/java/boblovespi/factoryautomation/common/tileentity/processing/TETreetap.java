package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.common.fluid.Fluids;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Objects;

import static boblovespi.factoryautomation.common.block.processing.Treetap.FACING;

/**
 * Created by Willi on 6/26/2018.
 */
public class TETreetap extends BlockEntity implements ITickable
{
	private static final int AMOUNT_PER_UPDATE = 1;
	private static final int AMOUNT_UNTIL_UPDATE = 24000 / (1000 / AMOUNT_PER_UPDATE); // TODO: move to config
	private int counter = -1;

	public TETreetap(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teTreetap, pos, state);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide) {
			return;
		}
		++counter;
		counter %= AMOUNT_UNTIL_UPDATE;

		if (counter == 0)
		{
			BlockEntity te = level.getBlockEntity(worldPosition.below());
			if (te != null)
			{
				LazyOptional<IFluidHandler> handler = te
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP);
				handler.ifPresent(n -> {
					BlockPos offset = worldPosition.relative(level.getBlockState(worldPosition).getValue(FACING).getOpposite());
					BlockState block = level.getBlockState(offset);
					BlockPos leafPos = offset;
					if (block.getBlock() == Blocks.JUNGLE_LOG)
					{
						while (true)
						{
							BlockState state = level.getBlockState(leafPos);
							if (state.getBlock() != Blocks.JUNGLE_LOG && state.getBlock() != Blocks.JUNGLE_LEAVES)
								return;
							if (state.getBlock() == Blocks.JUNGLE_LEAVES && state.getValue(LeavesBlock.PERSISTENT))
								break;
							leafPos = leafPos.above();
						}
						n.fill(new FluidStack(Fluids.rubberSap.still.get(), AMOUNT_PER_UPDATE), IFluidHandler.FluidAction.EXECUTE);
					}
				});
			}
		}
	}
}
