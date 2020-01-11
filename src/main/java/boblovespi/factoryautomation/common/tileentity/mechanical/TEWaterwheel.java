package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.machine.Waterwheel;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.machine.Waterwheel.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 6/23/2019.
 */
public class TEWaterwheel extends TileEntity implements IMultiblockControllerTE, ITickable
{
	public static final String MULTIBLOCK_ID = "waterwheel";
	private boolean structureIsValid = false;
	private MechanicalUser user;
	private int counter = -1;
	private Direction out;
	private ArrayList<BlockPos> waterLoc;

	public TEWaterwheel()
	{
		user = new MechanicalUser();
	}

	@Override
	public void onLoad()
	{
		out = Direction.getFacingFromAxis(Direction.AxisDirection.POSITIVE,
				world.getBlockState(pos).getValue(Waterwheel.AXIS));
		user.SetSides(EnumSet.of(out));
		waterLoc = new ArrayList<>(11);
		Direction front = out.rotateY();
		waterLoc.add(pos.offset(front, -1).up(3));
		waterLoc.add(pos.up(3));
		waterLoc.add(pos.offset(front).up(3));
		waterLoc.add(pos.offset(front, 2).up(2));
		waterLoc.add(pos.offset(front, 3).up());
		waterLoc.add(pos.offset(front, 3));
		waterLoc.add(pos.offset(front, 3).up(-1));
		waterLoc.add(pos.offset(front, 2).up(-2));
		waterLoc.add(pos.offset(front, 1).up(-3));
		waterLoc.add(pos.up(-3));
		waterLoc.add(pos.offset(front, -1).up(-3));
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
		MultiblockHelper.CreateStructure(world, pos, MULTIBLOCK_ID, out);
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, true));
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper.BreakStructure(world, pos, MULTIBLOCK_ID, out);
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, false));
	}

	/**
	 * Gets the capability, or null, of the block at offset for the given side
	 *
	 * @param capability the type of capability to get
	 * @param offset     the offset of the multiblock part
	 * @param side       the side which is accessed
	 * @return the capability implementation which to use
	 */
	@Override
	public <T> T GetCapability(Capability<T> capability, int[] offset, Direction side)
	{
		return null;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
		{
			return;
		}
		++counter;
		counter %= 10;

		if (counter == 0)
		{
			if (world.getBiome(pos) == Biomes.RIVER)
			{
				user.SetSpeedOnFace(out, 10);
				user.SetTorqueOnFace(out, 25);
			} else
			{
				float torque = 0;
				for (int i = 0; i < waterLoc.size(); i++)
				{
					BlockPos waterPos = waterLoc.get(i);
					BlockState state = world.getBlockState(waterPos);
					if (state.getMaterial() == Material.WATER)
					{
						if (state.getBlock() instanceof BlockLiquid)
						{
							Vec3d acc = Blocks.FLOWING_WATER
									.modifyAcceleration(world, waterPos, null, new Vec3d(0, 0, 0));
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

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
	{
		return capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return (T) user;
		return null;
	}
}
