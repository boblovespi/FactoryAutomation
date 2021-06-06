package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEMachine;
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
	public float rotation = 0;
	private final MechanicalUser mechanicalUser;
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
			TileEntity te = Objects.requireNonNull(world).getTileEntity(levelPosition.below());
			Direction facing = Direction.UP;
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.setSpeedOnFace(Direction.DOWN, GetUser(te, facing).getSpeedOnFace(facing));
				mechanicalUser.setTorqueOnFace(Direction.DOWN, GetUser(te, facing).getTorqueOnFace(facing));
			} else
			{
				mechanicalUser.setSpeedOnFace(Direction.DOWN, 0);
				mechanicalUser.setTorqueOnFace(Direction.DOWN, 0);
			}

			// markDirty();
			// BlockState state = world.getBlockState(levelPosition);
			// world.sendBlockUpdated(levelPosition, state, state, 3);
		}
	}

	@Override
	protected void UpdateClient()
	{
		rotation = (rotation + mechanicalUser.getSpeed()) % 360;
	}

	@Nonnull
	@Override
	protected String FindRecipeName()
	{
		MillstoneRecipe recipe1 = MillstoneRecipe.findRecipe(processingInv.getStackInSlot(0));
		if (recipe1 == null)
		{
			millstoneRecipe = null;
			return "none";
		} else
		{
			millstoneRecipe = recipe1;
			return recipe1.getName();
		}
	}

	@Nullable
	@Override
	protected MillstoneRecipe FindRecipe(String recipeName)
	{
		return MillstoneRecipe.getRecipe(recipeName);
	}

	@Override
	protected int GetMaxProgress()
	{
		return millstoneRecipe.getTime();
	}

	@Override
	protected void OnRecipeComplete(String recipe)
	{
		for (ItemStack stack : millstoneRecipe.getOutputs())
		{
			ItemEntity item = new ItemEntity(Objects.requireNonNull(world), levelPosition.getX() - 0.5 + Objects.requireNonNull(world).random.nextInt(3), levelPosition.getY() - 0.1,
					levelPosition.getZ() - 0.5 + world.random.nextInt(3), stack.copy());
			item.push(world.random.nextDouble() - 0.5, 0, world.random.nextDouble() - 0.5);
			item.hurtMarked = true;
			world.addFreshEntity(item);
		}
		processingInv.extractItem(0, 1, false);
	}

	@Override
	protected float GetProgressScalar()
	{
		if (mechanicalUser.getTorque() >= millstoneRecipe.getTorque())
			return MathHelper.clamp(mechanicalUser.getSpeed() / 10f, 0, 10);
		else
			return 0;
	}

	@Override
	protected void ReadCustomNBT(CompoundNBT tag)
	{
		mechanicalUser.loadFromNBT(tag.getCompound("mechanicalUser"));
		millstoneRecipe = MillstoneRecipe.getRecipe(recipeName);
	}

	@Override
	protected void WriteCustomNBT(CompoundNBT tag)
	{
		tag.put("mechanicalUser", mechanicalUser.saveToNBT());
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
		return mechanicalUser.getSpeed();
	}

	public void TakeOrPlace(ItemStack item, PlayerEntity player)
	{
		if (!processingInv.getStackInSlot(0).isEmpty())
		{
			ItemStack taken = processingInv.extractItem(0, 64, false);
			ItemHelper.putItemsInInventoryOrDrop(player, taken, world);
		} else
		{
			ItemStack stack = processingInv.insertItem(0, item.copy(), false);
			item.setCount(stack.getCount());
		}

		setChanged();
		BlockState state = Objects.requireNonNull(world).getBlockState(levelPosition);
		world.sendBlockUpdated(levelPosition, state, state, 3);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(Objects.requireNonNull(world).getBlockState(levelPosition), pkt.getTag());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(levelPosition, 0, nbt);
	}
}
