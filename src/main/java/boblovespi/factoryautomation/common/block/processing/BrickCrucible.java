package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.EnumProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/22/2018.
 */
public class BrickCrucible extends FABaseBlock
{
	public static final PropertyBool MULTIBLOCK_COMPLETE = PropertyBool.create("multiblock_complete");
	public static final EnumProperty<Direction> FACING = BlockHorizontal.FACING;
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(2 / 16d, 0, 2 / 16d, 14 / 16d, 1, 14 / 16d);

	public BrickCrucible()
	{
		super(Material.ROCK, "brick_crucible", FAItemGroups.metallurgy);
		setDefaultState(getStateFromMeta(0));
		TileEntityHandler.tiles.add(TEBrickCrucible.class);
		setHardness(1.5f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	public boolean isFullCube(BlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, BlockState state)
	{
		return new TEBrickCrucible();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, MULTIBLOCK_COMPLETE);
	}

	@Override
	public BlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, Direction.getHorizontal(meta & 3))
								.withProperty(MULTIBLOCK_COMPLETE, (meta & 4) == 4);
	}

	@Override
	public int getMetaFromState(BlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() | (state.getValue(MULTIBLOCK_COMPLETE) ? 4 : 0);
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn,
			EnumHand hand, Direction facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if (MultiblockHelper
					.IsStructureComplete(worldIn, pos, TEBrickCrucible.MULTIBLOCK_ID, state.getValue(FACING)))
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TEBrickCrucible)
				{
					TEBrickCrucible foundry = (TEBrickCrucible) te;
					if (!foundry.IsStructureValid())
						foundry.CreateStructure();

					if (facing == state.getValue(FACING).rotateYCCW())
						foundry.PourInto(facing);
					else
						playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.BRICK_FOUNDRY.id, worldIn,
								pos.getX(), pos.getY(), pos.getZ());
				}
			} else
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TEBrickCrucible)
					((TEBrickCrucible) te).SetStructureInvalid();
			}
		}
		return true;
	}

	/**
	 * Gets the {@link BlockState} to place
	 */
	@Override
	public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().rotateYCCW());
	}
}
