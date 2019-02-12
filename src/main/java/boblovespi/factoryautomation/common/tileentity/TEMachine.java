package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Willi on 2/12/2019.
 */
public abstract class TEMachine extends TileEntity implements ITickable
{
	protected ItemStackHandler processingInv;
	protected String recipe = "none";
	protected int maxProgress = 1;
	protected float currentProgress = 0;

	public TEMachine(int outputSize)
	{
		processingInv = new ItemStackHandler(outputSize + 1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				if (slot == 0)
				{
					String newRecipe = FindRecipe();
					if (!recipe.equals(newRecipe))
					{
						if (newRecipe.equals("none"))
						{
							recipe = "none";
							maxProgress = 1;
							currentProgress = 0;
						} else
						{
							recipe = newRecipe;
							maxProgress = GetMaxProgress();
							currentProgress = 0;
						}
					}
				}
			}
		};
	}

	@Nonnull
	protected abstract String FindRecipe();

	protected void Update()
	{

	}

	protected float GetProgressScalar()
	{
		return 1;
	}

	protected abstract int GetMaxProgress();

	protected abstract void OnRecipeComplete(String recipe);

	@Override
	public void update()
	{
		if (world.isRemote)
		{
			UpdateClient();
			return;
		}
		Update();
		if (recipe.equals("none"))
			return;

		if (currentProgress < maxProgress)
			currentProgress += GetProgressScalar();
		if (currentProgress >= maxProgress)
		{
			OnRecipeComplete(recipe);
			recipe = FindRecipe();
			if (!recipe.equals("none"))
			{
				maxProgress = GetMaxProgress();
				currentProgress = 0;
			} else
			{
				maxProgress = 1;
				currentProgress = 0;
			}
		}

		/* IMPORTANT */
		markDirty();
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	protected void UpdateClient()
	{

	}

	protected abstract void ReadCustomNBT(NBTTagCompound tag);

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		currentProgress = tag.getFloat("currentProgress");
		maxProgress = tag.getInteger("maxProgress");
		recipe = tag.getString("recipe");
		ReadCustomNBT(tag);
	}

	protected abstract void WriteCustomNBT(NBTTagCompound tag);

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		WriteCustomNBT(tag);
		tag.setFloat("currentProgress", currentProgress);
		tag.setInteger("maxProgress", maxProgress);
		tag.setString("recipe", recipe);
		return super.writeToNBT(tag);
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
