package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHorseEngine;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 7/3/2019.
 */
public class HorseEngine extends FABaseBlock
{
	public static PropertyEnum<Part> PART = PropertyEnum.create("part", Part.class);

	public HorseEngine()
	{
		super(Materials.WOOD_MACHINE, "horse_engine", FACreativeTabs.mechanical);
		setDefaultState(getDefaultState().withProperty(PART, Part.TOP));
		TileEntityHandler.tiles.add(TEHorseEngine.class);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART) == Part.TOP;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return hasTileEntity(state) ? new TEHorseEngine() : null;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (state.getValue(PART) != Part.TOP)
			return false;
		if (world.isRemote)
			return true;

		for (EntityLiving horse : world.getEntitiesWithinAABB(EntityLiving.class,
				new AxisAlignedBB(pos.getX() - 7.0D, pos.getY() - 7.0D, pos.getZ() - 7.0D, pos.getX() + 7.0D,
						pos.getY() + 7.0D, pos.getZ() + 7.0D)))
		{
			if (horse.getLeashed() && horse.getLeashHolder() == player && horse instanceof AbstractHorse)
			{
				horse.clearLeashed(true, true);
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TEHorseEngine)
					((TEHorseEngine) te).AttachHorse(horse);
			}
		}
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(PART, Part.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PART).ordinal();
	}

	public enum Part implements IStringSerializable
	{
		TOP("top"), MIDDLE("middle"), BOTTOM("bottom");

		private final String name;

		Part(String name)
		{
			this.name = name;
		}

		@Override
		public String getName()
		{
			return name;
		}
	}
}
