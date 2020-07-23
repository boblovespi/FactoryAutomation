package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Willi on 2/12/2019.
 */
public abstract class TEMachine extends TileEntity implements ITickableTileEntity
{
	protected ItemStackHandler processingInv;
	protected String recipe = "none";
	protected int maxProgress = 1;
	protected float currentProgress = 0;

	public TEMachine(int outputSize, TileEntityType<?> tileType)
	{
		super(tileType);
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
	public void tick()
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
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	protected void UpdateClient()
	{

	}

	protected abstract void ReadCustomNBT(CompoundNBT tag);

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		currentProgress = tag.getFloat("currentProgress");
		maxProgress = tag.getInt("maxProgress");
		recipe = tag.getString("recipe");
		ReadCustomNBT(tag);
	}

	protected abstract void WriteCustomNBT(CompoundNBT tag);

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		WriteCustomNBT(tag);
		tag.putFloat("currentProgress", currentProgress);
		tag.putInt("maxProgress", maxProgress);
		tag.putString("recipe", recipe);
		return super.write(tag);
	}

	@SuppressWarnings("MethodCallSideOnly")
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);

		return new SUpdateTileEntityPacket(pos, 0, nbt);
	}
}
