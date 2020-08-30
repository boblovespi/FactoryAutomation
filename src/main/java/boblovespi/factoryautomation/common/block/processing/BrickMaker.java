package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

/**
 * Created by Willi on 2/2/2019.
 */
public class BrickMaker extends FABaseBlock
{
	public static EnumProperty<Contents> CONTENTS = EnumProperty.create("contents", Contents.class);
	public static VoxelShape BOUNDING_BOX = Block.makeCuboidShape(0, 0, 0, 16, 6, 16);

	public BrickMaker()
	{
		super("brick_maker_frame", false, Properties.create(Material.WOOD).hardnessAndResistance(2).harvestLevel(0).harvestTool(
				ToolType.AXE), new Item.Properties().group(FAItemGroups.primitive));
		setDefaultState(stateContainer.getBaseState().with(CONTENTS, Contents.EMPTY));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	public int tickRate(IWorldReader worldIn)
	{
		return 3000;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		Contents value = state.get(CONTENTS);
		if (value.CanDry())
		{
			world.setBlockState(pos, state.with(CONTENTS, value.GetDried()));
			if (value.GetDried().CanDry())
				world.getPendingBlockTicks().scheduleTick(pos, this, tickRate(world));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		if (state.get(CONTENTS).CanAddClay())
			return VoxelShapes.empty();
		return BOUNDING_BOX;
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (world.isRemote)
			return ActionResultType.SUCCESS;

		Contents value = state.get(CONTENTS);
		if (value.CanAddClay() && player.getHeldItem(hand).getItem() == FAItems.terraclay)
		{
			world.setBlockState(pos, state.with(CONTENTS, value.AddClay()));
			player.getHeldItem(hand).shrink(1);
			world.getPendingBlockTicks().scheduleTick(pos, this, tickRate(world));
		} else if (value.CanRemove())
		{
			if (value.CanRemoveBrick())
			{
				world.setBlockState(pos, state.with(CONTENTS, value.RemoveClay()));
				ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FAItems.terraclayBrick.ToItem()), world);

			} else if (value.CanRemoveClay())
			{
				world.setBlockState(pos, state.with(CONTENTS, value.RemoveClay()));
				ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FAItems.terraclay.ToItem()), world);
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(CONTENTS);
	}

	public enum Contents implements IStringSerializable
	{
		EMPTY("empty"),
		HALF("half"),
		HALF_DRIED("half_dried"),
		FULL("full"),
		FULL_HALF_DRIED("full_half_dried"),
		FULL_DRIED("full_dried");

		private String name;

		Contents(String name)
		{
			this.name = name;
		}

		@Override
		public String getName()
		{
			return name;
		}

		public Contents GetDried()
		{
			switch (this)
			{
			case EMPTY:
				return EMPTY;
			case HALF:
				return HALF_DRIED;
			case HALF_DRIED:
				return HALF_DRIED;
			case FULL:
				return FULL_HALF_DRIED;
			case FULL_HALF_DRIED:
				return FULL_DRIED;
			case FULL_DRIED:
				return FULL_DRIED;
			default:
				return this;
			}
		}

		public Contents AddClay()
		{
			switch (this)
			{
			case EMPTY:
				return HALF;
			case HALF:
				return FULL;
			case HALF_DRIED:
				return FULL_HALF_DRIED;
			case FULL:
				return this;
			case FULL_HALF_DRIED:
				return this;
			case FULL_DRIED:
				return this;
			default:
				return this;
			}
		}

		public boolean CanDry()
		{
			return this != EMPTY && this != HALF_DRIED && this != FULL_DRIED;
		}

		public boolean CanAddClay()
		{
			return this == EMPTY || this == HALF_DRIED || this == HALF;
		}

		public boolean CanRemoveClay()
		{
			return CanRemove() && this == HALF || this == FULL;
		}

		public boolean CanRemoveBrick()
		{
			return CanRemove() && !CanRemoveClay();
		}

		public boolean CanRemove()
		{
			return this != EMPTY;
		}

		public Contents RemoveClay()
		{
			switch (this)
			{
			case EMPTY:
				return EMPTY;
			case HALF:
				return EMPTY;
			case HALF_DRIED:
				return EMPTY;
			case FULL:
				return HALF;
			case FULL_HALF_DRIED:
				return HALF;
			case FULL_DRIED:
				return HALF_DRIED;
			default:
				return this;
			}
		}
	}
}
