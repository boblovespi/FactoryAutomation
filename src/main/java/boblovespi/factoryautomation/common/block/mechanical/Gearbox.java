package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.Wrench;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 5/6/2018.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Gearbox extends FABaseBlock implements EntityBlock
{
	public static DirectionProperty FACING = BlockStateProperties.FACING;

	public Gearbox()
	{
		super(Material.METAL, "gearbox", FAItemGroups.mechanical);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.WEST));
		TileEntityHandler.tiles.add(TEGearbox.class);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEGearbox(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getItem() instanceof Wrench)
			return InteractionResult.PASS;

		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		BlockEntity tileEntity = world.getBlockEntity(pos);
		Item item = stack.getItem();
		GearType gear = GetGear(item);

		if (gear != null)
		{
			if (tileEntity instanceof TEGearbox)
			{
				TEGearbox te = (TEGearbox) tileEntity;
				if (te.AddGear(gear, gear.durability - stack.getDamageValue()))
					stack.shrink(1);
			}
		} else if (stack.isEmpty())
		{
			if (tileEntity instanceof TEGearbox)
			{
				((TEGearbox) tileEntity).RemoveGear();
			}
		}

		return InteractionResult.SUCCESS;
	}

	private boolean IsGear(Item item)
	{
		for (GearType gearType : GearType.values())
		{
			if (item == FAItems.gear.GetItem(gearType))
				return true;
		}
		return false;
	}

	@Nullable
	private GearType GetGear(Item item)
	{
		for (GearType gearType : GearType.values())
		{
			if (item == FAItems.gear.GetItem(gearType))
				return gearType;
		}
		return null;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "mechanical/" + RegistryName();
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return Shapes.empty();
	}

	public enum GearType implements StringRepresentable, IMultiTypeEnum
	{
		WOOD(1, 40, "wood"),
		STONE(2, 50, "stone"),
		COPPER(1, 200, "copper"),
		IRON(2, 600, "iron"),
		GOLD(3, 400, "gold"),
		BRONZE(5, 600, "bronze"),
		DIAMOND(4, 900, "diamond"),
		MAGMATIC_BRASS(8, 900, "magmatic_brass"),
		STEEL(16, 1200, "steel"),
		ALUMINUM(6, 900, "aluminum"),
		ALUMINUM_BRONZE(7, 900, "aluminum_bronze");

		public final int scaleFactor;
		public final int durability;
		private final String name;

		GearType(int scaleFactor, int durability, String name)
		{
			this.scaleFactor = scaleFactor;
			this.durability = durability;
			this.name = name;
		}

		@Override
		public int GetId()
		{
			return ordinal();
		}

		@Override
		public String getSerializedName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
