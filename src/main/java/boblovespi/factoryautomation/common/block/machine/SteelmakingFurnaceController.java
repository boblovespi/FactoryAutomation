package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.multiblock.IMultiblockStructureController;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/23/2017.
 */
public class SteelmakingFurnaceController extends FABaseBlock
		implements ITileEntityProvider, IMultiblockStructureController
{
	public static final IProperty<Axis> AXIS = PropertyEnum.create("axis", Axis.class, Axis.X, Axis.Z);
	public static final PropertyBool MULTIBLOCK_COMPLETE = PropertyBool.create("multiblock_complete");

	public SteelmakingFurnaceController()
	{
		super(Material.DRAGON_EGG, "steelmaking_furnace_controller");
	}

	@Override
	public String GetPatternId()
	{
		return "steelmaking_furnace";
	}

	@Override
	public void CreateStructure(World world, BlockPos pos)
	{

	}

	@Override
	public void BreakStructure(World world, BlockPos pos)
	{

	}

	@Override
	public void SetStructureCompleted(World world, BlockPos pos, boolean completed)
	{

	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TESteelmakingFurnace();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if (MultiblockHelper.IsStructureComplete(worldIn, pos, GetPatternId(),
					worldIn.getBlockState(pos).getValue(AXIS) == Axis.X ? EnumFacing.WEST : EnumFacing.NORTH) /*|| MultiblockHelper
					.IsStructureComplete(worldIn, pos, GetPatternId(),
										 EnumFacing.NORTH)*/)
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TESteelmakingFurnace)
					((TESteelmakingFurnace) te).CreateStructure();
				playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.STEELMAKING_FURNACE.id, worldIn,
						pos.getX(), pos.getY(), pos.getZ());
			} else
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TESteelmakingFurnace)
					((TESteelmakingFurnace) te).SetStructureInvalid();
			}
		}
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis())
				   .withProperty(MULTIBLOCK_COMPLETE, false);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(AXIS, (meta & 1) == 1 ? Axis.X : Axis.Z)
								.withProperty(MULTIBLOCK_COMPLETE, (meta & 2) == 2);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(AXIS) == Axis.X ? 1 : 0) | (state.getValue(MULTIBLOCK_COMPLETE) ? 2 : 0);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AXIS, MULTIBLOCK_COMPLETE);
	}
}

/*
Recipes: (air) + (fuel)

*ALL RECIPES TAKE PIG IRON*

nothing -> slowest
air -> slower
preheated air -> slow
preheated air + fuel -> fast
preheated oxygen -> medium, better efficiency

preheated air + fuel + flux -> fast, better efficiency

*/