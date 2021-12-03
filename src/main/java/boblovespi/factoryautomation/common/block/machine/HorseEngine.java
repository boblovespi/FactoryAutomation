package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHorseEngine;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

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
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return hasTileEntity(state) ? new TEHorseEngine() : null;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(PART);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit)
	{
		if (state.getValue(PART) != Part.TOP)
			return InteractionResult.FAIL;
		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		for (Mob horse : world.getEntitiesOfClass(Mob.class,
				new AABB(pos.getX() - 7.0D, pos.getY() - 7.0D, pos.getZ() - 7.0D, pos.getX() + 7.0D,
						pos.getY() + 7.0D, pos.getZ() + 7.0D)))
		{
			if (horse.isLeashed() && horse.getLeashHolder() == player && horse instanceof AbstractHorse)
			{
				horse.dropLeash(true, true);
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TEHorseEngine)
					((TEHorseEngine) te).AttachHorse(horse);
			}
		}
		return InteractionResult.SUCCESS;
	}
	public enum Part implements StringRepresentable
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
