package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.misc.IBellowsable;
import boblovespi.factoryautomation.client.tesr.IBellowsTE;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.mechanical.LeatherBellows.FACING;
import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 5/11/2019.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TELeatherBellows extends TileEntity implements ITickableTileEntity, IBellowsTE
{
	private float counter = 0;
	private int c2 = 0;
	private final MechanicalUser mechanicalUser;
	private float lerp = 0;
	private boolean firstTick = true;

	public TELeatherBellows()
	{
		super(TileEntityHandler.teLeatherBellows);
		mechanicalUser = new MechanicalUser();
	}

	public void FirstLoad()
	{
		Direction dir = getBlockState().getValue(FACING);
		mechanicalUser.setSides(EnumSet.of(dir.getOpposite()));
		firstTick = false;
	}

	public void Blow()
	{
		Direction facing = getBlockState().getValue(PaperBellows.FACING);
		TileEntity te = Objects.requireNonNull(level).getBlockEntity(levelPosition.relative(facing));
		if (te == null)
			return;
		LazyOptional<IBellowsable> capability = te
				.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());
		capability.ifPresent(n -> n.blow(MathHelper.clamp(mechanicalUser.getTorque() / 30f, 0.5f, 1), 50));
		level.playSound(null, levelPosition, SoundEvents.ENDER_DRAGON_FLAP, SoundCategory.BLOCKS, 0.8f, 1.5f);
	}

	@Override
	public void tick()
	{
		if (level.isClientSide)
		{
			counter -= mechanicalUser.getSpeed() / 10f;
			if (counter <= 0)
			{
				counter = 100;
			}
			lerp = Math.abs(2 * (counter / 100f) - 1);
			return;
		}
		if (firstTick)
			FirstLoad();

		counter -= mechanicalUser.getSpeed() / 10f;
		if (counter <= 0)
		{
			counter = 100;
			Blow();
		}
		c2--;
		if (c2 <= 0)
		{
			Direction facing = getBlockState().getValue(FACING);
			TileEntity te = level.getBlockEntity(levelPosition.relative(facing.getOpposite()));
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.setSpeedOnFace(facing.getOpposite(), GetUser(te, facing).getSpeedOnFace(facing));
				mechanicalUser.setTorqueOnFace(facing.getOpposite(), GetUser(te, facing).getTorqueOnFace(facing));
			} else
			{
				mechanicalUser.setSpeedOnFace(facing.getOpposite(), 0);
				mechanicalUser.setTorqueOnFace(facing.getOpposite(), 0);
			}

			setChanged();
			level.sendBlockUpdated(levelPosition, getBlockState(), getBlockState(), 3);
			c2 = 4;
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		mechanicalUser.loadFromNBT(tag.getCompound("mechanicalUser"));
		counter = tag.getFloat("counter");
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("mechanicalUser", mechanicalUser.saveToNBT());
		tag.putFloat("counter", counter);
		return super.save(tag);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) mechanicalUser);
		return super.getCapability(capability, facing);
	}

	@Override
	public float getLerp()
	{
		return lerp;
	}

	@Override
	public float getLerpSpeed()
	{
		return (counter > 50 ? -1 : 1) * mechanicalUser.getSpeed() / 400f;
	}
}
