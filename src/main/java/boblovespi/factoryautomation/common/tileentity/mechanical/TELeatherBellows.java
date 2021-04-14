package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.misc.IBellowsable;
import boblovespi.factoryautomation.client.tesr.IBellowsTE;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
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
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.mechanical.LeatherBellows.FACING;
import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 5/11/2019.
 */
public class TELeatherBellows extends TileEntity implements ITickableTileEntity, IBellowsTE
{
	private float counter = 0;
	private int c2 = 0;
	private MechanicalUser mechanicalUser;
	private float lerp = 0;
	private boolean firstTick = true;

	public TELeatherBellows()
	{
		super(TileEntityHandler.teLeatherBellows);
		mechanicalUser = new MechanicalUser();
	}

	public void FirstLoad()
	{
		Direction dir = getBlockState().get(FACING);
		mechanicalUser.SetSides(EnumSet.of(dir.getOpposite()));
		firstTick = false;
	}

	public void Blow()
	{
		Direction facing = getBlockState().get(PaperBellows.FACING);
		TileEntity te = world.getTileEntity(pos.offset(facing));
		if (te == null)
			return;
		LazyOptional<IBellowsable> capability = te
				.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());
		capability.ifPresent(n -> n.Blow(MathHelper.clamp(mechanicalUser.GetTorque() / 30f, 0.5f, 1), 50));
		world.playSound(null, pos, SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.BLOCKS, 0.8f, 1.5f);
	}

	@Override
	public void tick()
	{
		if (world.isRemote)
		{
			counter -= mechanicalUser.GetSpeed() / 10f;
			if (counter <= 0)
			{
				counter = 100;
			}
			lerp = Math.abs(2 * (counter / 100f) - 1);
			return;
		}
		if (firstTick)
			FirstLoad();

		counter -= mechanicalUser.GetSpeed() / 10f;
		if (counter <= 0)
		{
			counter = 100;
			Blow();
		}
		c2--;
		if (c2 <= 0)
		{
			Direction facing = getBlockState().get(FACING);
			TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.SetSpeedOnFace(facing.getOpposite(), GetUser(te, facing).GetSpeedOnFace(facing));
				mechanicalUser.SetTorqueOnFace(facing.getOpposite(), GetUser(te, facing).GetTorqueOnFace(facing));
			} else
			{
				mechanicalUser.SetSpeedOnFace(facing.getOpposite(), 0);
				mechanicalUser.SetTorqueOnFace(facing.getOpposite(), 0);
			}

			markDirty();
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
			c2 = 4;
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT tag)
	{
		super.read(state, tag);
		mechanicalUser.ReadFromNBT(tag.getCompound("mechanicalUser"));
		counter = tag.getFloat("counter");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.putFloat("counter", counter);
		return super.write(tag);
	}

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) mechanicalUser);
		return super.getCapability(capability, facing);
	}

	@Override
	public float GetLerp()
	{
		return lerp;
	}

	@Override
	public float GetLerpSpeed()
	{
		return (counter > 50 ? -1 : 1) * mechanicalUser.GetSpeed() / 400f;
	}
}
