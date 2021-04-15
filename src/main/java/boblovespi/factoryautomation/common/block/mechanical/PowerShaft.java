package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 1/15/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault

public class PowerShaft extends FABaseBlock
{
	public static EnumProperty<Axis> AXIS = EnumProperty.create("facing", Axis.class);
	public static BooleanProperty IS_TESR = BooleanProperty.create("is_tesr");

	public static AxisAlignedBB[] AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(6.5 / 16d, 0 / 16d, 6.5 / 16d, 6.5 / 16d + 3 / 16d, 16 / 16d, 6.5 / 16d + 3 / 16d),
			new AxisAlignedBB(0 / 16d, 6.5 / 16d, 6.5 / 16d, 16 / 16d, 6.5 / 16d + 3 / 16d, 6.5 / 16d + 3 / 16d),
			new AxisAlignedBB(6.5 / 16d, 6.5 / 16d, 0 / 16d, 6.5 / 16d + 3 / 16d, 6.5 / 16d + 3 / 16d,
					16 / 16d) }; // 0: up; 1: n-s; 2: e-w
	public static VoxelShape[] VOXELS = new VoxelShape[3];

	public PowerShaft()
	{
		super(Material.METAL, "power_shaft", null);
		registerDefaultState(stateDefinition.any().setValue(AXIS, Axis.X).setValue(IS_TESR, false));
		if (VOXELS[0] == null)
			for (int i = 0; i < 3; i++)
			{
				VOXELS[i] = VoxelShapes.create(AABB[i]);
			}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return VOXELS[GetAxisId(state.getValue(AXIS))];
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
		return new TEPowerShaft();
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(AXIS, IS_TESR);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(AXIS, context.getHorizontalDirection().getAxis());
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "mechanical/" + RegistryName();
	}
}
