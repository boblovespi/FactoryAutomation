package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.machine.Waterwheel;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.biome.Biomes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.machine.Waterwheel.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 6/23/2019.
 */
public class TEWaterwheel extends TileEntity implements IMultiblockControllerTE, ITickableTileEntity
{
	public static final String MULTIBLOCK_ID = "waterwheel";
	private boolean structureIsValid = false;
	private MechanicalUser user;
	private int counter = -1;
	private Direction out;
	private ArrayList<BlockPos> waterLoc;
	private boolean firstTick = true;

	public TEWaterwheel()
	{
		super(TileEntityHandler.teWaterwheel);
		user = new MechanicalUser();
	}

	public void FirstLoad()
	{
		out = Direction.get(Direction.AxisDirection.POSITIVE, getBlockState().getValue(Waterwheel.AXIS));
		user.SetSides(EnumSet.of(out));
		waterLoc = new ArrayList<>(11);
		Direction front = out.getClockWise();
		waterLoc.add(worldPosition.relative(front, -1).above(3));
		waterLoc.add(worldPosition.above(3));
		waterLoc.add(worldPosition.relative(front).above(3));
		waterLoc.add(worldPosition.relative(front, 2).above(2));
		waterLoc.add(worldPosition.relative(front, 3).above());
		waterLoc.add(worldPosition.relative(front, 3));
		waterLoc.add(worldPosition.relative(front, 3).above(-1));
		waterLoc.add(worldPosition.relative(front, 2).above(-2));
		waterLoc.add(worldPosition.relative(front, 1).above(-3));
		waterLoc.add(worldPosition.above(-3));
		waterLoc.add(worldPosition.relative(front, -1).above(-3));
		firstTick = false;
	}

	@Override
	public void SetStructureValid(boolean isValid)
	{
		structureIsValid = isValid;
	}

	@Override
	public boolean IsStructureValid()
	{
		return structureIsValid;
	}

	@Override
	public void CreateStructure()
	{
		MultiblockHelper.CreateStructure(level, worldPosition, MULTIBLOCK_ID, out);
		Objects.requireNonNull(level).setBlockAndUpdate(worldPosition, getBlockState().setValue(MULTIBLOCK_COMPLETE, true));
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper.BreakStructure(level, worldPosition, MULTIBLOCK_ID, out);
		Objects.requireNonNull(level).setBlockAndUpdate(worldPosition, getBlockState().setValue(MULTIBLOCK_COMPLETE, false));
	}

	/**
	 * Gets the capability, or null, of the block at offset for the given side
	 *
	 * @param capability the type of capability to get
	 * @param offset     the offset of the multiblock part
	 * @param side       the side which is accessed
	 * @return the capability implementation which to use
	 */
	@Nonnull
    @Override
	public <T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side)
	{
		return LazyOptional.empty();
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			return;
		}
		if (firstTick)
			FirstLoad();

		++counter;
		counter %= 10;

		if (counter == 0)
		{
			if (level.getBiome(worldPosition).getRegistryName() == Biomes.RIVER.location())
			{
				user.SetSpeedOnFace(out, 10);
				user.SetTorqueOnFace(out, 25);
			} else
			{
				float torque = 0;
				for (int i = 0; i < waterLoc.size(); i++)
				{
					BlockPos waterPos = waterLoc.get(i);
					BlockState state = level.getBlockState(waterPos);
					if (state.getMaterial() == Material.WATER)
					{
						if (state.getBlock() instanceof FlowingFluidBlock)
						{
							Vector3d acc = state.getFluidState().getFlow(level, waterPos);
							if (out.getAxis() == Direction.Axis.X) // water flowing along z axis
							{
								if (i < 3 || i > 6)
									torque += Math.abs(acc.z) * 2;
								else
									torque += Math.abs(acc.y) * 2;
							} else
							{
								if (i < 3 || i > 6)
									torque += Math.abs(acc.x) * 2;
								else
									torque += Math.abs(acc.y) * 2;
							}
						}
					}
				}
				user.SetSpeedOnFace(out, torque < 1 ? 0 : 10);
				user.SetTorqueOnFace(out, torque);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) user);
		return super.getCapability(capability, facing);
	}
}
