package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TETripHammerController;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.ItemHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 8/13/2018.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TripHammerController extends FABaseBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<BlockstateEnum> MULTIBLOCK_COMPLETE = EnumProperty
			.create("multiblock_complete", BlockstateEnum.class);

	public TripHammerController()
	{
		super("trip_hammer", false, Properties.of(Material.METAL).strength(3, 20).harvestLevel(0)
											  .harvestTool(ToolType.PICKAXE),
				new Item.Properties().tab(FAItemGroups.metallurgy));
		TileEntityHandler.tiles.add(TETripHammerController.class);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, MULTIBLOCK_COMPLETE);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())
				   .setValue(MULTIBLOCK_COMPLETE, BlockstateEnum.FALSE);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the result type of using the block.
	 */
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (world.isClientSide)
			return ActionResultType.SUCCESS;
		TileEntity te1 = world.getBlockEntity(pos);
		if (te1 instanceof TETripHammerController)
		{
			TETripHammerController te = (TETripHammerController) te1;
			if (MultiblockHelper.IsStructureComplete(world, pos, TETripHammerController.MULTIBLOCK_ID,
					world.getBlockState(pos).getValue(FACING)))
			{
				System.out.println("complete!");
				te.CreateStructure();
				ItemStack item = player.getItemInHand(hand);
				if (item.isEmpty())
				{
					ItemStack item1 = te.TakeItem();
					ItemHelper.PutItemsInInventoryOrDrop(player, item1, world);
				} else
				{
					if (te.PutItem(item.copy().split(1)))
						item.shrink(1);
				}
			}
		} else
			System.out.println("incomplete!");
		if (te1 == null)
			System.out.println("null!!!");
		return ActionResultType.SUCCESS;
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
		return new TETripHammerController();
	}

	@SuppressWarnings("SpellCheckingInspection")
	public enum BlockstateEnum implements IStringSerializable {
		FALSE, TRUE, TESR;

		@Override
		public String getSerializedName() {
			return name().toLowerCase();
		}
	}
}
