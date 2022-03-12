package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 1/15/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault

public class PowerShaft extends FABaseBlock implements EntityBlock
{
	public static EnumProperty<Axis> AXIS = EnumProperty.create("facing", Axis.class);
	public static BooleanProperty IS_TESR = BooleanProperty.create("is_tesr");

	public static AABB[] AABB = new AABB[] {
			new AABB(6.5 / 16d, 0 / 16d, 6.5 / 16d, 6.5 / 16d + 3 / 16d, 16 / 16d, 6.5 / 16d + 3 / 16d),
			new AABB(0 / 16d, 6.5 / 16d, 6.5 / 16d, 16 / 16d, 6.5 / 16d + 3 / 16d, 6.5 / 16d + 3 / 16d),
			new AABB(6.5 / 16d, 6.5 / 16d, 0 / 16d, 6.5 / 16d + 3 / 16d, 6.5 / 16d + 3 / 16d,
					16 / 16d) }; // 0: up; 1: n-s; 2: e-w
	public static VoxelShape[] VOXELS = new VoxelShape[3];
	public final float maxSpeed;
	public final float maxTorque;

	public PowerShaft(String name, float maxSpeed, float maxTorque)
	{
		super(name, false, Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.5f),
			  new Item.Properties().tab(FAItemGroups.mechanical));
		this.maxSpeed = maxSpeed;
		this.maxTorque = maxTorque;
		registerDefaultState(stateDefinition.any().setValue(AXIS, Axis.X).setValue(IS_TESR, false));
		if (VOXELS[0] == null)
			for (int i = 0; i < 3; i++)
			{
				VOXELS[i] = Shapes.create(AABB[i]);
			}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return VOXELS[GetAxisId(state.getValue(AXIS))];
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEPowerShaft(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	private int GetAxisId(Axis a)
	{
		switch (a)
		{
		case Y:
			return 0;
		case X:
			return 1;
		case Z:
			return 2;
		default:
			return 0;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(AXIS, IS_TESR);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "mechanical/" + RegistryName();
	}
}
