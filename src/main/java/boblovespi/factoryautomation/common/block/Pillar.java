package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
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

/**
 * Created by Willi on 8/4/2018.
 */
public class Pillar extends FABaseBlock
{
	public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, 3);

	public Pillar(String name, Metals metal)
	{
		super(name, false, Properties.create(Material.IRON).hardnessAndResistance(1, 10).harvestLevel(1)
									 .harvestTool(ToolType.PICKAXE),
				new Item.Properties().group(ItemGroup.DECORATIONS));
		setDefaultState(stateContainer.getBaseState().with(HEIGHT, 1));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "metals/" + RegistryName();
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 */
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		int height = state.get(HEIGHT);

		BlockPos down1 = pos.down();
		BlockPos down2 = pos.down(2);
		BlockState dState2 = world.getBlockState(down2);
		BlockPos up1 = pos.up();
		BlockPos up2 = pos.up(2);
		BlockState uState1 = world.getBlockState(up1);
		BlockState uState2 = world.getBlockState(up2);
		// EffectivelyPlace(world, pos);
		UpdateState(world, pos, state);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(HEIGHT);
	}

	/**
	 * Gets the {@link BlockState} to place
	 */
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(HEIGHT, 1);
	}

	private void UpdateState(World world, BlockPos pos, BlockState state)
	{
		int height = state.get(HEIGHT);
		BlockPos down1 = pos.down();
		BlockPos down2 = pos.down(2);
		BlockState dState1 = world.getBlockState(down1);
		BlockState dState2 = world.getBlockState(down2);
		BlockPos up1 = pos.up();
		BlockPos up2 = pos.up(2);
		BlockState uState1 = world.getBlockState(up1);
		BlockState uState2 = world.getBlockState(up2);

		if (height == 0)
		{
			if (dState1.getBlock() == this)
			{
				UpdateState(world, down1, dState1);
			}
			if (dState2.getBlock() == this)
			{
				UpdateState(world, down2, dState2);
			}
		} else if (uState1.getBlock() == this)
		{
			if (uState1.get(HEIGHT) == 0)
			{
				if (uState2.getBlock() == this && uState2.get(HEIGHT) < 2)
				{
					world.setBlockState(up1, uState2.with(HEIGHT, 0));
					world.setBlockState(up2, uState1.with(HEIGHT, 0));
					world.setBlockState(pos, getDefaultState().with(HEIGHT, 3));
				} else
				{
					world.setBlockState(pos, getDefaultState().with(HEIGHT, 2));
					world.setBlockState(up1, uState1.with(HEIGHT, 0));
				}
			} else if (uState1.get(HEIGHT) == 1)
			{
				if (uState2.getBlock() == this && uState2.get(HEIGHT) == 1)
				{
					world.setBlockState(up1, uState2.with(HEIGHT, 0));
					world.setBlockState(up2, uState1.with(HEIGHT, 0));
					world.setBlockState(pos, getDefaultState().with(HEIGHT, 3));
				} else
				{
					world.setBlockState(pos, getDefaultState().with(HEIGHT, 2));
					world.setBlockState(up1, uState1.with(HEIGHT, 0));
				}
			} else if (uState1.get(HEIGHT) == 2 && uState2.getBlock() == this && uState2.get(HEIGHT) == 0)
			{
				world.setBlockState(up1, uState2.with(HEIGHT, 0));
				world.setBlockState(up2, uState1.with(HEIGHT, 0));
				world.setBlockState(pos, getDefaultState().with(HEIGHT, 3));
			}
		} else
		{
			if (height == 3 && uState2.getBlock() == this && uState2.get(HEIGHT) == 0)
			{
				UpdateState(world, up2, uState2.with(HEIGHT, 1));
			}
			world.setBlockState(pos, state.with(HEIGHT, 1));
		}
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		// EffectivelyPlace(world, pos);
		UpdateState(world, pos, state);
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		BlockPos down1 = pos.down();
		BlockPos down2 = pos.down(2);
		BlockState dState1 = world.getBlockState(down1);
		BlockState dState2 = world.getBlockState(down2);
		BlockPos up1 = pos.up();
		BlockPos up2 = pos.up(2);
		BlockState uState1 = world.getBlockState(up1);
		BlockState uState2 = world.getBlockState(up2);
		if (state.get(HEIGHT) == 0)
		{
			if (dState1.getBlock() == this)
			{
				UpdateState(world, down1, dState1);
			}
			if (dState2.getBlock() == this)
			{
				UpdateState(world, down2, dState2);
			}
		} else if (state.get(HEIGHT) > 1)
		{
			if (uState1.getBlock() == this)
			{
				world.setBlockState(up1, uState1.with(HEIGHT, 1));
				UpdateState(world, up1, uState1.with(HEIGHT, 1));
			}
			if (uState2.getBlock() == this)
			{
				world.setBlockState(up1, uState1.with(HEIGHT, 1));
				UpdateState(world, up2, uState2.with(HEIGHT, 1));
			}
		}
	}
}
