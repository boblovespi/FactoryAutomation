package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.mechanical.HandCrank.INVERTED;
import static boblovespi.factoryautomation.common.util.SetBlockStateFlags.FORCE_BLOCK_UPDATE;
import static boblovespi.factoryautomation.common.util.SetBlockStateFlags.SEND_TO_CLIENT;

/**
 * Created by Willi on 9/3/2018.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEHandCrank extends TileEntity implements ITickableTileEntity
{

	public static final float SPEED = 1;
	public float rotation = 0;
	private final MechanicalUser mechanicalUser;
	private boolean isRotating;
	public boolean inverted = false;
	private boolean firstTick = true;

	public TEHandCrank()
	{
		super(TileEntityHandler.teHandCrank);
		this.mechanicalUser = new MechanicalUser(EnumSet.of(Direction.DOWN));
		this.isRotating = false;
	}

	public void FirstLoad()
	{
		this.inverted = getBlockState().getValue(
				INVERTED);
		this.mechanicalUser.SetSides(EnumSet.of(inverted ? Direction.UP : Direction.DOWN));
		this.firstTick = false;
	}

	public void Rotate()
	{
		if (!this.isRotating)
		{
			this.isRotating = true;

			this.mechanicalUser.SetTorqueOnFace(inverted ? Direction.UP : Direction.DOWN, 1f);
			this.mechanicalUser.SetSpeedOnFace(inverted ? Direction.UP : Direction.DOWN, SPEED);

			setChanged();

			/* IMPORTANT */
			Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		mechanicalUser.ReadFromNBT(tag.getCompound("mechanicalUser"));
		rotation = tag.getFloat("rotation");
		isRotating = tag.getBoolean("isRotating");
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.putFloat("rotation", rotation);
		tag.putBoolean("isRotating", isRotating);
		return super.save(tag);
	}

	// Todo: update to use non-null
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) mechanicalUser);
		return null;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (firstTick)
			FirstLoad();
		if (isRotating)
		{
			rotation += EnergyConstants.RadiansSecondToDegreesTick(SPEED);

			if (rotation >= 360)
			{
				rotation = 0;
				isRotating = false;

				mechanicalUser.SetTorqueOnFace(inverted ? Direction.UP : Direction.DOWN, 0);
				mechanicalUser.SetSpeedOnFace(inverted ? Direction.UP : Direction.DOWN, 0);

				setChanged();

				/* IMPORTANT */
				Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
			}
		}
	}

	public boolean IsRotating()
	{
		return isRotating;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(Objects.requireNonNull(level).getBlockState(worldPosition), pkt.getTag());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(worldPosition, 0, nbt);
	}
}
