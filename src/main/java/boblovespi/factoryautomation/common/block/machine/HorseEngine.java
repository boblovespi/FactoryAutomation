package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHorseEngine;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 7/3/2019.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HorseEngine extends FABaseBlock
{
	public static EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

	public HorseEngine()
	{
		super(Materials.WOOD_MACHINE, "horse_engine", FAItemGroups.mechanical);
		registerDefaultState(defaultBlockState().setValue(PART, Part.TOP));
		TileEntityHandler.tiles.add(TEHorseEngine.class);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return state.getValue(PART) == Part.TOP;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return hasTileEntity(state) ? new TEHorseEngine() : null;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(PART);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (state.getValue(PART) != Part.TOP)
			return ActionResultType.FAIL;
		if (world.isClientSide)
			return ActionResultType.SUCCESS;

		for (MobEntity horse : world.getEntitiesOfClass(MobEntity.class,
				new AxisAlignedBB(pos.getX() - 7.0D, pos.getY() - 7.0D, pos.getZ() - 7.0D, pos.getX() + 7.0D,
						pos.getY() + 7.0D, pos.getZ() + 7.0D)))
		{
			if (horse.isLeashed() && horse.getLeashHolder() == player && horse instanceof AbstractHorseEntity)
			{
				horse.dropLeash(true, true);
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TEHorseEngine)
					((TEHorseEngine) te).AttachHorse(horse);
			}
		}
		return ActionResultType.SUCCESS;
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
		public String getSerializedName()
		{
			return name;
		}
	}
}
