package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.Wrench;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 5/6/2018.
 */
public class Gearbox extends FABaseBlock
{
	public static DirectionProperty FACING = BlockStateProperties.FACING;

	public Gearbox()
	{
		super(Material.IRON, "gearbox", FAItemGroups.mechanical);
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.WEST));
		TileEntityHandler.tiles.add(TEGearbox.class);
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
		return new TEGearbox();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(FACING, context.getFace());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (stack.getItem() instanceof Wrench)
			return ActionResultType.PASS;

		if (world.isRemote)
			return ActionResultType.SUCCESS;

		TileEntity tileEntity = world.getTileEntity(pos);
		Item item = stack.getItem();
		GearType gear = GetGear(item);

		if (gear != null)
		{
			if (tileEntity instanceof TEGearbox)
			{
				TEGearbox te = (TEGearbox) tileEntity;
				if (te.AddGear(gear, gear.durability - stack.getDamage()))
					stack.shrink(1);
			}
		} else if (stack.isEmpty())
		{
			if (tileEntity instanceof TEGearbox)
			{
				((TEGearbox) tileEntity).RemoveGear();
			}
		}

		return ActionResultType.SUCCESS;
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
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return VoxelShapes.empty();
	}

	public enum GearType implements IStringSerializable, IMultiTypeEnum
	{
		WOOD(1, 40, "wood"),
		STONE(2, 50, "stone"),
		COPPER(1, 200, "copper"),
		IRON(2, 600, "iron"),
		GOLD(3, 400, "gold"),
		BRONZE(5, 600, "bronze"),
		DIAMOND(4, 900, "diamond"),
		MAGMATIC_BRASS(8, 900, "magmatic_brass"),
		STEEL(16, 1200, "steel");

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
		public String getName()
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
