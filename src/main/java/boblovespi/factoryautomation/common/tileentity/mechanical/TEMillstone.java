package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.common.tileentity.TEMachine;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.TEHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 2/12/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TEMillstone extends TEMachine<MillstoneRecipe>
{
	private final MechanicalUser mechanicalUser;
	public float rotation = 0;
	private MillstoneRecipe millstoneRecipe = null;
	private int counter = 0;

	public TEMillstone()
	{
		super(0, TileEntityHandler.teMillstone);
		mechanicalUser = new MechanicalUser(EnumSet.of(Direction.DOWN));
	}

	@Override
	protected void Update()
	{
		++counter;
		counter %= 4;

		if (counter == 0)
		{
			TileEntity te = Objects.requireNonNull(level).getBlockEntity(worldPosition.below());
			TileEntity te1 = level.getBlockEntity(worldPosition.above());
			Direction facing = Direction.UP;
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.SetSpeedOnFace(Direction.DOWN, GetUser(te, facing).GetSpeedOnFace(facing));
				mechanicalUser.SetTorqueOnFace(Direction.DOWN, GetUser(te, facing).GetTorqueOnFace(facing));
				mechanicalUser.SetSpeedOnFace(Direction.UP, 0);
				mechanicalUser.SetTorqueOnFace(Direction.UP, 0);
			} else if (TEHelper.IsMechanicalFace(te1, Direction.DOWN))
			{
				mechanicalUser.SetSpeedOnFace(Direction.UP, GetUser(te1, Direction.DOWN).GetSpeedOnFace(Direction.DOWN));
				mechanicalUser.SetTorqueOnFace(Direction.UP, GetUser(te1, Direction.DOWN).GetTorqueOnFace(Direction.DOWN));
				mechanicalUser.SetSpeedOnFace(Direction.DOWN, 0);
				mechanicalUser.SetTorqueOnFace(Direction.DOWN, 0);
			} else
			{
				mechanicalUser.SetSpeedOnFace(Direction.DOWN, 0);
				mechanicalUser.SetTorqueOnFace(Direction.DOWN, 0);
				mechanicalUser.SetSpeedOnFace(Direction.UP, 0);
				mechanicalUser.SetTorqueOnFace(Direction.UP, 0);
			}

			// markDirty();
			// BlockState state = level.getBlockState(worldPosition);
			// level.sendBlockUpdated(worldPosition, state, state, 3);
		}
	}

	@Override
	protected void UpdateClient()
	{
		rotation = (rotation + mechanicalUser.GetSpeed()) % 360;
	}

	@Nonnull
	@Override
	protected String FindRecipeName()
	{
		MillstoneRecipe recipe1 = MillstoneRecipe.FindRecipe(processingInv.getStackInSlot(0));
		if (recipe1 == null)
		{
			millstoneRecipe = null;
			return "none";
		} else
		{
			millstoneRecipe = recipe1;
			return recipe1.GetName();
		}
	}

	@Nullable
	@Override
	protected MillstoneRecipe FindRecipe(String recipeName)
	{
		return MillstoneRecipe.GetRecipe(recipeName);
	}

	@Override
	protected int GetMaxProgress()
	{
		return millstoneRecipe.GetTime();
	}

	@Override
	protected void OnRecipeComplete(String recipe)
	{
		for (ItemStack stack : millstoneRecipe.GetOutputs())
		{
			ItemEntity item = new ItemEntity(Objects.requireNonNull(level), worldPosition.getX() - 0.5 + Objects.requireNonNull(level).random.nextInt(3), worldPosition.getY() - 0.1,
					worldPosition.getZ() - 0.5 + level.random.nextInt(3), stack.copy());
			item.push(level.random.nextDouble() - 0.5, 0, level.random.nextDouble() - 0.5);
			item.hurtMarked = true;
			level.addFreshEntity(item);
		}
		processingInv.extractItem(0, 1, false);
	}

	@Override
	protected float GetProgressScalar()
	{
		if (mechanicalUser.GetTorque() >= millstoneRecipe.GetTorque())
			return MathHelper.clamp(mechanicalUser.GetSpeed() / 10f, 0, 10);
		else
			return 0;
	}

	@Override
	protected void ReadCustomNBT(CompoundNBT tag)
	{
		mechanicalUser.ReadFromNBT(tag.getCompound("mechanicalUser"));
		millstoneRecipe = MillstoneRecipe.GetRecipe(recipeName);
	}

	@Override
	protected void WriteCustomNBT(CompoundNBT tag)
	{
		tag.put("mechanicalUser", mechanicalUser.WriteToNBT());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of((() -> (T) processingInv));
		else if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of((() -> (T) mechanicalUser));
		return super.getCapability(capability, facing);
	}

	public float GetSpeed()
	{
		return mechanicalUser.GetSpeed();
	}

	public void TakeOrPlace(ItemStack item, PlayerEntity player)
	{
		if (!processingInv.getStackInSlot(0).isEmpty())
		{
			ItemStack taken = processingInv.extractItem(0, 64, false);
			ItemHelper.PutItemsInInventoryOrDrop(player, taken, level);
		} else
		{
			ItemStack stack = processingInv.insertItem(0, item.copy(), false);
			item.setCount(stack.getCount());
		}

		setChanged();
		BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, 3);
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
