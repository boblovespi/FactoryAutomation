package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/22/2018.
 */
public class BrickCastingVessel extends FABaseBlock
{
	public static final PropertyEnum<CastingVesselStates> MOLD = PropertyEnum.create("mold", CastingVesselStates.class);

	public BrickCastingVessel()
	{
		super(Material.ROCK, "brick_casting_vessel", FACreativeTabs.metallurgy);
		setDefaultState(getDefaultState().withProperty(MOLD, CastingVesselStates.EMPTY));
		TileEntityHandler.tiles.add(TEStoneCastingVessel.class);
		setHardness(1.5f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, MOLD);
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TEStoneCastingVessel();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(MOLD, CastingVesselStates.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(MOLD).ordinal();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		//		if (!world.isRemote)
		//		{
		//			if (player.getHeldItem(hand).getItem() == Items.STICK)
		//			{
		//				player.openGui(FactoryAutomation.instance, GuiHandler.GuiID.STONE_CASTING_VESSEL.id, world, pos.getX(),
		//						pos.getY(), pos.getZ());
		//			} else
		//			{
		//				TileEntity te = world.getTileEntity(pos);
		//				if (te instanceof TEStoneCastingVessel)
		//					((TEStoneCastingVessel) te).TakeOrPlace(player.getHeldItem(hand), player);
		//			}
		//		}
		return true;
	}
}