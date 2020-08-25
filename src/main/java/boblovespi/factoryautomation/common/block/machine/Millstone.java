package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMillstone;
import boblovespi.factoryautomation.common.util.FAItemGroups;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/12/2019.
 */
public class Millstone extends FABaseBlock
{
	public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");

	public Millstone()
	{
		super("millstone", false,
				Properties.create(Material.ROCK).hardnessAndResistance(2.5f).harvestTool(ToolType.PICKAXE)
						  .harvestLevel(0), new Item.Properties().group(FAItemGroups.mechanical));

		TileEntityHandler.tiles.add(TEMillstone.class);
		setDefaultState(stateContainer.getBaseState().with(IS_TOP, false));
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
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(IS_TOP);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult result)
	{
		if (world.isRemote)
			return ActionResultType.SUCCESS;
		ItemStack item = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);

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
}
