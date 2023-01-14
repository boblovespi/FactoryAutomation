package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import boblovespi.factoryautomation.common.tileentity.processing.TEBrickMaker;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/2/2019.
 */
public class BrickMaker extends FABaseBlock implements EntityBlock
{
	public static EnumProperty<Contents> CONTENTS = EnumProperty.create("contents", Contents.class);
	public static EnumProperty<Render> RENDER = EnumProperty.create("render", Render.class);
	public static VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 16, 6, 16);

	public BrickMaker()
	{
		super("brick_maker_frame", false, Properties.of(Material.WOOD).strength(2), new Item.Properties().tab(FAItemGroups.primitive));
		registerDefaultState(stateDefinition.any().setValue(CONTENTS, Contents.EMPTY).setValue(RENDER, Render.FRAME));
	}

	public int tickRate(LevelReader levelIn)
	{
		return 3000;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand)
	{
		Contents value = state.getValue(CONTENTS);
		if (value.CanDry())
		{
			world.setBlockAndUpdate(pos, state.setValue(CONTENTS, value.GetDried()));
			if (value.GetDried().CanDry())
				world.scheduleTick(pos, this, tickRate(world));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		if (state.getValue(CONTENTS).CanAddClay())
			return Shapes.empty();
		return BOUNDING_BOX;
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		Contents value = state.getValue(CONTENTS);
		if (value.CanAddClay() && player.getItemInHand(hand).getItem() == FAItems.terraclay)
		{
			world.setBlockAndUpdate(pos, state.setValue(CONTENTS, value.AddClay()));
			player.getItemInHand(hand).shrink(1);
			world.scheduleTick(pos, this, tickRate(world));
		} else if (value.CanRemove())
		{
			if (value.CanRemoveBrick())
			{
				world.setBlockAndUpdate(pos, state.setValue(CONTENTS, value.RemoveClay()));
				ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FAItems.terraclayBrick), world);

			} else if (value.CanRemoveClay())
			{
				world.setBlockAndUpdate(pos, state.setValue(CONTENTS, value.RemoveClay()));
				ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FAItems.terraclay), world);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(CONTENTS);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEBrickMaker(pos, state);
	}

	public enum Contents implements StringRepresentable
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
		public String getSerializedName()
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

	public enum Render implements StringRepresentable
	{
		FRAME("frame"), LEFT("left"), RIGHT("right");

		private String name;

		Render(String name)
		{
			this.name = name;
		}

		@Override
		public String getSerializedName()
		{
			return name;
		}
	}
}
