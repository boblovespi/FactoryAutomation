package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEMachine;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 2/12/2019.
 */
public class TEMillstone extends TEMachine<MillstoneRecipe>
{
	public float rotation = 0;
	private MechanicalUser mechanicalUser;
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
			TileEntity te = world.getTileEntity(pos.down());
			Direction facing = Direction.UP;
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.SetSpeedOnFace(Direction.DOWN, GetUser(te, facing).GetSpeedOnFace(facing));
				mechanicalUser.SetTorqueOnFace(Direction.DOWN, GetUser(te, facing).GetTorqueOnFace(facing));
			} else
			{
				mechanicalUser.SetSpeedOnFace(Direction.DOWN, 0);
				mechanicalUser.SetTorqueOnFace(Direction.DOWN, 0);
			}

			// markDirty();
			// BlockState state = world.getBlockState(pos);
			// world.notifyBlockUpdate(pos, state, state, 3);
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
			ItemEntity item = new ItemEntity(world, pos.getX() - 0.5 + world.rand.nextInt(3), pos.getY() - 0.1,
					pos.getZ() - 0.5 + world.rand.nextInt(3), stack.copy());
			item.addVelocity(world.rand.nextDouble() - 0.5, 0, world.rand.nextDouble() - 0.5);
			item.velocityChanged = true;
			world.addEntity(item);
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

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
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
			ItemHelper.PutItemsInInventoryOrDrop(player, taken, world);
		} else
		{
			ItemStack stack = processingInv.insertItem(0, item.copy(), false);
			item.setCount(stack.getCount());
		}

		markDirty();
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}
}
