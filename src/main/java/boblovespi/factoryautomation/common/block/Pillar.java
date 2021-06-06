package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 8/4/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class Pillar extends FABaseBlock
{
	public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, 3);

	public Pillar(String name, Metals metal)
	{
		super(name, false, Properties.of(Material.METAL).strength(1, 10).harvestLevel(1)
									 .harvestTool(ToolType.PICKAXE),
				new Item.Properties().tab(ItemGroup.TAB_DECORATIONS));
		registerDefaultState(stateDefinition.any().setValue(HEIGHT, 1));
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "metals/" + registryName();
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 */
	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		int height = state.getValue(HEIGHT);

		BlockPos down1 = pos.below();
		BlockPos down2 = pos.below(2);
		BlockState dState2 = level.getBlockState(down2);
		BlockPos up1 = pos.above();
		BlockPos up2 = pos.above(2);
		BlockState uState1 = level.getBlockState(up1);
		BlockState uState2 = level.getBlockState(up2);
		// EffectivelyPlace(level, pos);
		UpdateState(level, pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(HEIGHT);
	}

	/**
	 * Gets the {@link BlockState} to place
	 */
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return defaultBlockState().setValue(HEIGHT, 1);
	}

	private void UpdateState(World level, BlockPos pos, BlockState state)
	{
		int height = state.getValue(HEIGHT);
		BlockPos down1 = pos.below();
		BlockPos down2 = pos.below(2);
		BlockState dState1 = level.getBlockState(down1);
		BlockState dState2 = level.getBlockState(down2);
		BlockPos up1 = pos.above();
		BlockPos up2 = pos.above(2);
		BlockState uState1 = level.getBlockState(up1);
		BlockState uState2 = level.getBlockState(up2);

		if (height == 0)
		{
			if (dState1.getBlock() == this)
			{
				UpdateState(level, down1, dState1);
			}
			if (dState2.getBlock() == this)
			{
				UpdateState(level, down2, dState2);
			}
		} else if (uState1.getBlock() == this)
		{
			if (uState1.getValue(HEIGHT) == 0)
			{
				if (uState2.getBlock() == this && uState2.getValue(HEIGHT) < 2)
				{
					level.setBlockAndUpdate(up1, uState2.setValue(HEIGHT, 0));
					level.setBlockAndUpdate(up2, uState1.setValue(HEIGHT, 0));
					level.setBlockAndUpdate(pos, defaultBlockState().setValue(HEIGHT, 3));
				} else
				{
					level.setBlockAndUpdate(pos, defaultBlockState().setValue(HEIGHT, 2));
					level.setBlockAndUpdate(up1, uState1.setValue(HEIGHT, 0));
				}
			} else if (uState1.getValue(HEIGHT) == 1)
			{
				if (uState2.getBlock() == this && uState2.getValue(HEIGHT) == 1)
				{
					level.setBlockAndUpdate(up1, uState2.setValue(HEIGHT, 0));
					level.setBlockAndUpdate(up2, uState1.setValue(HEIGHT, 0));
					level.setBlockAndUpdate(pos, defaultBlockState().setValue(HEIGHT, 3));
				} else
				{
					level.setBlockAndUpdate(pos, defaultBlockState().setValue(HEIGHT, 2));
					level.setBlockAndUpdate(up1, uState1.setValue(HEIGHT, 0));
				}
			} else if (uState1.getValue(HEIGHT) == 2 && uState2.getBlock() == this && uState2.getValue(HEIGHT) == 0)
			{
				level.setBlockAndUpdate(up1, uState2.setValue(HEIGHT, 0));
				level.setBlockAndUpdate(up2, uState1.setValue(HEIGHT, 0));
				level.setBlockAndUpdate(pos, defaultBlockState().setValue(HEIGHT, 3));
			}
		} else
		{
			if (height == 3 && uState2.getBlock() == this && uState2.getValue(HEIGHT) == 0)
			{
				UpdateState(level, up2, uState2.setValue(HEIGHT, 1));
			}
			level.setBlockAndUpdate(pos, state.setValue(HEIGHT, 1));
		}
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	@Override
	public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		// EffectivelyPlace(level, pos);
		UpdateState(level, pos, state);
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	@Override
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		BlockPos down1 = pos.below();
		BlockPos down2 = pos.below(2);
		BlockState dState1 = level.getBlockState(down1);
		BlockState dState2 = level.getBlockState(down2);
		BlockPos up1 = pos.above();
		BlockPos up2 = pos.above(2);
		BlockState uState1 = level.getBlockState(up1);
		BlockState uState2 = level.getBlockState(up2);
		if (state.getValue(HEIGHT) == 0)
		{
			if (dState1.getBlock() == this)
			{
				UpdateState(level, down1, dState1);
			}
			if (dState2.getBlock() == this)
			{
				UpdateState(level, down2, dState2);
			}
		} else if (state.getValue(HEIGHT) > 1)
		{
			if (uState1.getBlock() == this)
			{
				level.setBlockAndUpdate(up1, uState1.setValue(HEIGHT, 1));
				UpdateState(level, up1, uState1.setValue(HEIGHT, 1));
			}
			if (uState2.getBlock() == this)
			{
				level.setBlockAndUpdate(up1, uState1.setValue(HEIGHT, 1));
				UpdateState(level, up2, uState2.setValue(HEIGHT, 1));
			}
		}
	}
}
