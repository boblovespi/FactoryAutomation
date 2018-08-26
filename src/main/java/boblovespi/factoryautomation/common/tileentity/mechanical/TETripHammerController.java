package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.TripHammerRecipe;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.machine.TripHammerController.*;

/**
 * Created by Willi on 8/13/2018.
 */
public class TETripHammerController extends TileEntity implements IMultiblockControllerTE, ITickable
{
	public static final String MULTIBLOCK_ID = "trip_hammer";
	private ItemStackHandler itemHandler;
	private MechanicalUser mechanicalUser;
	private boolean isValid;
	private TripHammerRecipe currentRecipe = null;
	private String currentRecipeString = "none";
	private float timeLeftInRecipe = -1;

	public TETripHammerController()
	{
		itemHandler = new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();
			}
		};
		mechanicalUser = new MechanicalUser();
	}

	@Override
	public void onLoad()
	{
		EnumFacing dir = world.getBlockState(pos).getValue(FACING);
		mechanicalUser.SetSides(EnumSet.of(dir.rotateY(), dir.rotateYCCW()));
	}

	@Override
	public void SetStructureValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	@Override
	public boolean IsStructureValid()
	{
		return isValid;
	}

	@Override
	public void CreateStructure()
	{
		SetStructureValid();
		MultiblockHelper.CreateStructure(world, pos, MULTIBLOCK_ID, world.getBlockState(pos).getValue(FACING));
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, BlockstateEnum.TRUE));
	}

	@Override
	public void BreakStructure()
	{
		SetStructureInvalid();
		MultiblockHelper.BreakStructure(world, pos, MULTIBLOCK_ID, world.getBlockState(pos).getValue(FACING));
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, BlockstateEnum.FALSE));
	}

	public boolean PutItem(ItemStack item)
	{
		if (itemHandler.getStackInSlot(1).isEmpty() && itemHandler.getStackInSlot(0).isEmpty())
		{
			if (itemHandler.insertItem(0, item.splitStack(1), false).isEmpty())
				return true;
		}
		return false;
	}

	public ItemStack TakeItem()
	{
		if (itemHandler.getStackInSlot(0).isEmpty())
			return itemHandler.extractItem(1, 64, false);
		currentRecipe = null;
		currentRecipeString = "none";
		return itemHandler.extractItem(0, 64, false);
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
	public <T> T GetCapability(Capability<T> capability, int[] offset, EnumFacing side)
	{
		if (offset[0] == 5 && offset[1] == 1 && offset[2] == 0 && side.getAxis() == world.getBlockState(pos)
																						 .getValue(FACING).rotateY()
																						 .getAxis()
				&& capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return (T) mechanicalUser;
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
			// do rendering calculations
		} else
		{
			ItemStack stack = itemHandler.getStackInSlot(0);
			if (stack.isEmpty() || !itemHandler.getStackInSlot(1).isEmpty())
				return;
			if (currentRecipe == null)
			{
				if ("none".equals(currentRecipeString))
				{
					TripHammerRecipe recipe = TripHammerRecipe.FindRecipe(stack);
					currentRecipe = recipe;
					if (recipe != null)
					{
						currentRecipeString = recipe.name;
						markDirty();
						timeLeftInRecipe = recipe.time;
					}
				} else
				{
					currentRecipe = TripHammerRecipe.STRING_TRIP_HAMMER_RECIPE_MAP.get(currentRecipeString);
				}
			} else
			{
				if (mechanicalUser.GetTorque() >= currentRecipe.torque)
				{
					timeLeftInRecipe -= 1; // TODO: calculate actual time changing scale
				}
				if (timeLeftInRecipe < 0)
				{
					itemHandler.extractItem(0, 1, false);
					itemHandler.insertItem(1, currentRecipe.itemOutput.copy(), false);
					currentRecipe = null;
					currentRecipeString = "none";

				}
				markDirty();
			}
			IBlockState state = world.getBlockState(pos);
			EnumFacing facing = state.getValue(FACING);

			BlockPos pos2 = MultiblockHelper.AddWithRotation(pos, 5, 1, 0, facing);
			EnumFacing rotateY = facing.rotateY();
			EnumFacing rotateYCCW = facing.rotateYCCW();
			TileEntity teCW = world.getTileEntity(pos2.offset(rotateY, 1));
			TileEntity teCCW = world.getTileEntity(pos2.offset(rotateYCCW, 1));

			if (TEHelper.IsMechanicalFace(teCW, rotateYCCW))
			{
				mechanicalUser.SetSpeedOnFace(rotateY, TEHelper.GetUser(teCW, rotateYCCW).GetSpeedOnFace(rotateYCCW));
				mechanicalUser.SetTorqueOnFace(rotateY, TEHelper.GetUser(teCW, rotateYCCW).GetTorqueOnFace(rotateYCCW));
			} else if (TEHelper.IsMechanicalFace(teCCW, rotateY))
			{
				mechanicalUser.SetSpeedOnFace(rotateYCCW, TEHelper.GetUser(teCCW, rotateY).GetSpeedOnFace(rotateY));
				mechanicalUser.SetTorqueOnFace(rotateYCCW, TEHelper.GetUser(teCCW, rotateY).GetTorqueOnFace(rotateY));
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		itemHandler.deserializeNBT(nbt.getCompoundTag("inventory"));
		mechanicalUser.ReadFromNBT(nbt.getCompoundTag("mechanicalUser"));
		currentRecipeString = nbt.getString("recipe");
		timeLeftInRecipe = nbt.getFloat("timeLeftInRecipe");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("inventory", itemHandler.serializeNBT());
		nbt.setTag("mechanicalUser", mechanicalUser.WriteToNBT());
		nbt.setString("recipe", currentRecipeString);
		nbt.setFloat("timeLeftInRecipe", timeLeftInRecipe);

		return super.writeToNBT(nbt);
	}

	@SuppressWarnings("MethodCallSideOnly")
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int meta = getBlockMetadata();

		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}
}
