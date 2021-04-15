package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMillstone;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 2/12/2019.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Millstone extends FABaseBlock
{
	public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");
	public static final VoxelShape BOUNDING_BOX = VoxelShapes
			.or(Block.box(0, 0, 0, 16, 8, 16), Block.box(2, 8, 2, 14, 12, 14),
					Block.box(7.5, 12, 7.5, 8.5, 16, 8.5)).optimize();

	public Millstone()
	{
		super("millstone", false,
				Properties.of(Material.STONE).strength(2.5f).harvestTool(ToolType.PICKAXE)
						  .harvestLevel(0), new Item.Properties().tab(FAItemGroups.mechanical));

		TileEntityHandler.tiles.add(TEMillstone.class);
		registerDefaultState(stateDefinition.any().setValue(IS_TOP, false));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "mechanical/" + RegistryName();
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
		return new TEMillstone();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(IS_TOP);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult result)
	{
		if (world.isClientSide)
			return ActionResultType.SUCCESS;
		ItemStack item = player.getItemInHand(hand);
		TileEntity te = world.getBlockEntity(pos);

		if (te instanceof TEMillstone)
		{
			((TEMillstone) te).TakeOrPlace(item, player);
		}
		return ActionResultType.SUCCESS;
	}

	//	@Override
	//	public boolean isFullCube(BlockState state)
	//	{
	//		return false;
	//	}
	//
	//	/**
	//	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	//	 */
	//	@Override
	//	public boolean isOpaqueCube(BlockState state)
	//	{
	//		return false;
	//	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return VoxelShapes.empty();
	}
}
