package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.common.fluid.Fluids;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import static boblovespi.factoryautomation.common.block.processing.Treetap.FACING;

/**
 * Created by Willi on 6/26/2018.
 */
public class TETreetap extends TileEntity implements ITickable
{
	private static final int AMOUNT_PER_UPDATE = 1;
	private static final int AMOUNT_UNTIL_UPDATE = 24000 / (1000 / AMOUNT_PER_UPDATE); // TODO: move to config
	private int counter = -1;

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		++counter;
		counter %= AMOUNT_UNTIL_UPDATE;

		if (counter == 0)
		{
			TileEntity te = world.getTileEntity(pos.down());
			if (te != null)
			{
				if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
				{
					IFluidHandler handler = te
							.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);

					if (handler != null)
					{
						BlockPos offset = pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite());
						IBlockState block = world.getBlockState(offset);
						BlockPos leafPos = offset;
						if (block.getBlock() == Blocks.LOG
								&& block.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE)
						{
							while (true)
							{
								IBlockState state = world.getBlockState(leafPos);
								if (!(state.getBlock() == Blocks.LOG || state.getBlock() == Blocks.LEAVES))
									return;
								if (state.getBlock() == Blocks.LEAVES && state.getValue(BlockLeaves.DECAYABLE))
									break;
								leafPos = leafPos.up();
							}
							handler.fill(new FluidStack(Fluids.rubberSap, AMOUNT_PER_UPDATE), true);
						}
					}
				}
			}
		}
	}
}
