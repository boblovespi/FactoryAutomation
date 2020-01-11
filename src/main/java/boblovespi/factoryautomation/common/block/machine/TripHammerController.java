package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.mechanical.TETripHammerController;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.EnumProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 8/13/2018.
 */
public class TripHammerController extends FABaseBlock implements ITileEntityProvider
{
	public static final EnumProperty<Direction> FACING = BlockHorizontal.FACING;
	public static final EnumProperty<BlockstateEnum> MULTIBLOCK_COMPLETE = EnumProperty
			.create("multiblock_complete", BlockstateEnum.class);

	public TripHammerController()
	{
		super(Material.IRON, "trip_hammer", null);
		setHardness(3f);
		setResistance(20);
		setHarvestLevel("pickaxe", 0);
		TileEntityHandler.tiles.add(TETripHammerController.class);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, MULTIBLOCK_COMPLETE);
	}

	@Override
	public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing())
				   .withProperty(MULTIBLOCK_COMPLETE, BlockstateEnum.FALSE);
	}

	@Override
	public BlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, Direction.getHorizontal(meta / 2))
								.withProperty(MULTIBLOCK_COMPLETE,
										(meta & 1) == 1 ? BlockstateEnum.TRUE : BlockstateEnum.FALSE);
	}

	@Override
	public int getMetaFromState(BlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() * 2 | (
				state.getValue(MULTIBLOCK_COMPLETE) == BlockstateEnum.TRUE ? 1 : 0);
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, EnumHand hand,
			Direction facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;
		TileEntity te1 = world.getTileEntity(pos);
		if (te1 instanceof TETripHammerController)
		{
			TETripHammerController te = (TETripHammerController) te1;
			if (MultiblockHelper.IsStructureComplete(world, pos, TETripHammerController.MULTIBLOCK_ID,
					world.getBlockState(pos).getValue(FACING)))
			{
				System.out.println("complete!");
				te.CreateStructure();
				ItemStack item = player.getHeldItem(hand);
				if (item.isEmpty())
				{
					ItemStack item1 = te.TakeItem();
					ItemHelper.PutItemsInInventoryOrDrop(player, item1, world);
				} else
				{
					if (te.PutItem(item.copy().splitStack(1)))
						item.shrink(1);
				}
			}
		} else
			System.out.println("incomplete!");
		if (te1 == null)
			System.out.println("null!!!");
		return true;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TETripHammerController();
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	public enum BlockstateEnum implements IStringSerializable
	{
		FALSE, TRUE, TESR;

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}
