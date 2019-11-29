package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible.MetalForms;

/**
 * Created by Willi on 12/22/2018.
 */
public class StoneCastingVessel extends FABaseBlock
{
	public static final PropertyEnum<CastingVesselStates> MOLD = PropertyEnum.create("mold", CastingVesselStates.class);
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.5d, 1);

	public StoneCastingVessel()
	{
		super(Material.ROCK, "stone_casting_vessel", FAItemGroups.metallurgy);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
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
		if (!world.isRemote)
		{
			if (player.getHeldItem(hand).getItem() == Items.STICK)
			{
				player.openGui(FactoryAutomation.instance, GuiHandler.GuiID.STONE_CASTING_VESSEL.id, world, pos.getX(),
						pos.getY(), pos.getZ());
			} else
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TEStoneCastingVessel)
					((TEStoneCastingVessel) te).TakeOrPlace(player.getHeldItem(hand), player);
			}
		}
		return true;
	}

	public enum CastingVesselStates implements IStringSerializable
	{
		EMPTY(MetalForms.NONE),
		SAND(MetalForms.NONE),
		INGOT(MetalForms.INGOT),
		NUGGET(MetalForms.NUGGET),
		SHEET(MetalForms.SHEET),
		COIN(MetalForms.COIN),
		ROD(MetalForms.ROD),
		GEAR(MetalForms.GEAR);
		public final MetalForms metalForm;

		CastingVesselStates(MetalForms metalForm)
		{
			this.metalForm = metalForm;
		}

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}

		@Override
		public String toString()
		{
			return getName();
		}
	}
}