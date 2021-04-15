package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.common.fluid.Fluids;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import static boblovespi.factoryautomation.common.block.processing.Treetap.FACING;

/**
 * Created by Willi on 6/26/2018.
 */
public class TETreetap extends TileEntity implements ITickableTileEntity
{
	private static final int AMOUNT_PER_UPDATE = 1;
	private static final int AMOUNT_UNTIL_UPDATE = 24000 / (1000 / AMOUNT_PER_UPDATE); // TODO: move to config
	private int counter = -1;

	public TETreetap()
	{
		super(TileEntityHandler.teTreetap);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (world.isClientSide)
			return;
		++counter;
		counter %= AMOUNT_UNTIL_UPDATE;

		if (counter == 0)
		{
			TileEntity te = world.getBlockEntity(pos.down());
			if (te != null)
			{
				LazyOptional<IFluidHandler> handler = te
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP);
				handler.ifPresent(n -> {
					BlockPos offset = pos.offset(world.getBlockState(pos).get(FACING).getOpposite());
					BlockState block = world.getBlockState(offset);
					BlockPos leafPos = offset;
					if (block.getBlock() == Blocks.JUNGLE_LOG)
					{
						while (true)
						{
							BlockState state = world.getBlockState(leafPos);
							if (state.getBlock() != Blocks.JUNGLE_LOG && state.getBlock() != Blocks.JUNGLE_LEAVES)
								return;
							if (state.getBlock() == Blocks.JUNGLE_LEAVES && state.getValue(LeavesBlock.PERSISTENT))
								break;
							leafPos = leafPos.up();
						}
						n.fill(new FluidStack(Fluids.rubberSap.still.get(), AMOUNT_PER_UPDATE), IFluidHandler.FluidAction.EXECUTE);
					}
				});
			}
		}
	}
}
