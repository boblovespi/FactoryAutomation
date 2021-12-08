package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.recipe.IMachineRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 2/12/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class TEMachine<T extends IMachineRecipe> extends BlockEntity implements TickableBlockEntity
{
	protected ItemStackHandler processingInv;
	protected String recipeName = "none";
	protected T recipeCache = null;
	protected int maxProgress = 1;
	protected float currentProgress = 0;
	private boolean firstTick = true;

	public TEMachine(int outputSize, BlockEntityType<?> tileType)
	{
		super(tileType);
		processingInv = new ItemStackHandler(outputSize + 1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				if (slot == 0)
				{
					String newRecipe = FindRecipeName();
					if (!recipeName.equals(newRecipe))
					{
						if (newRecipe.equals("none"))
						{
							recipeName = "none";
							recipeCache = null;
							maxProgress = 1;
							currentProgress = 0;
						} else
						{
							recipeName = newRecipe;
							recipeCache = FindRecipe(recipeName);
							maxProgress = GetMaxProgress();
							currentProgress = 0;
						}
					}
				}
			}
		};
	}

	@Nonnull
	protected abstract String FindRecipeName();

	@Nullable
	protected abstract T FindRecipe(String recipeName);

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
		if (Objects.requireNonNull(level).isClientSide)
		{
			UpdateClient();
			return;
		}
		if (firstTick)
			FirstLoad();

		Update();
		if (recipeName.equals("none"))
			return;

		if (currentProgress < maxProgress)
			currentProgress += GetProgressScalar();
		if (currentProgress >= maxProgress)
		{
			OnRecipeComplete(recipeName);
			recipeName = FindRecipeName();
			if (!recipeName.equals("none"))
			{
				recipeCache = FindRecipe(recipeName);
				maxProgress = GetMaxProgress();
				currentProgress = 0;
			} else
			{
				recipeCache = null;
				maxProgress = 1;
				currentProgress = 0;
			}
		}

		/* IMPORTANT */
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	public void FirstLoad()
	{
		if (!recipeName.equals("none"))
			recipeCache = FindRecipe(recipeName);
		firstTick = false;
	}

	protected void UpdateClient()
	{

	}

	protected abstract void ReadCustomNBT(CompoundTag tag);

	@Override
	public void load(BlockState state, CompoundTag tag)
	{
		super.load(state, tag);
		currentProgress = tag.getFloat("currentProgress");
		maxProgress = tag.getInt("maxProgress");
		recipeName = tag.getString("recipe");
		processingInv.deserializeNBT(tag.getCompound("processingInv"));
		ReadCustomNBT(tag);
	}

	protected abstract void WriteCustomNBT(CompoundTag tag);

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		WriteCustomNBT(tag);
		tag.putFloat("currentProgress", currentProgress);
		tag.putInt("maxProgress", maxProgress);
		tag.putString("recipe", recipeName);
		tag.put("processingInv", processingInv.serializeNBT());
		return super.save(tag);
	}

	@SuppressWarnings("MethodCallSideOnly")
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		this.load(Objects.requireNonNull(level).getBlockState(worldPosition), pkt.getTag());
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		CompoundTag nbt = new CompoundTag();
		save(nbt);

		return new ClientboundBlockEntityDataPacket(worldPosition, 0, nbt);
	}
}
