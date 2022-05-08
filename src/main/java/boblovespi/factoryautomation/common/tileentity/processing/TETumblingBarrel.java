package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.TumblingBarrelRecipe;
import boblovespi.factoryautomation.common.container.ContainerTumblingBarrel;
import boblovespi.factoryautomation.common.tileentity.TEMachine;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.RestrictedSlotItemHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

public class TETumblingBarrel extends TEMachine<TumblingBarrelRecipe> implements MenuProvider, IAnimatable
{
	public float rotation = 0;
	private final FluidTank input;
	private final FluidTank output;
	private final RestrictedSlotItemHandler inputWrapper;
	private final RestrictedSlotItemHandler outputWrapper;
	private final MechanicalUser user;
	private final ContainerData containerData = new ContainerData()
	{
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 -> maxProgress;
				case 1 -> (int) currentProgress;
				case 2 -> input.getFluidAmount();
				case 3 -> output.getFluidAmount();
				case 4 -> ((ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS).getID(input.getFluid().getFluid());
				case 5 -> ((ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS).getID(output.getFluid().getFluid());
				default -> 0;
			};
		}

		@Override
		public void set(int index, int val)
		{

		}

		@Override
		public int getCount()
		{
			return 6;
		}
	};
	private AnimationFactory factory;

	public TETumblingBarrel(BlockPos pos, BlockState state)
	{
		super(1, TileEntityHandler.teTumblingBarrel, pos, state);
		input = new FluidTank(2 * 1000)
		{
			@Override
			protected void onContentsChanged()
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
				setChanged();
				BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
				level.sendBlockUpdated(worldPosition, state, state, 7);
			}
		};
		output = new FluidTank(2 * 1000)
		{
			@Override
			protected void onContentsChanged()
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
				setChanged();
				BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
				level.sendBlockUpdated(worldPosition, state, state, 7);
			}
		};
		inputWrapper = new RestrictedSlotItemHandler(new BitSet(2)
		{{
			set(1);
		}}, processingInv);
		outputWrapper = new RestrictedSlotItemHandler(new BitSet(2)
		{{
			set(0);
		}}, processingInv);
		Direction.Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
		user = new MechanicalUser(EnumSet.of(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE),
											 Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE)));
		factory = new AnimationFactory(this);
	}

	@Nonnull
	@Override
	protected String FindRecipeName()
	{
		var recipe = FirstRecipeMatching(TumblingBarrelRecipe.TYPE, n ->
		{
			return n.GetInput().test(processingInv.getStackInSlot(0))
						   && MatchesOrEmpty(n.GetFluidInputs().get(0), input.getFluid())
						   && processingInv.insertItem(1, n.GetPrimaryItemOutputs().get(0), true).isEmpty()
						   && Fits(output, n.GetPrimaryFluidOutputs().get(0));
		});
		return recipe == null ? "none" : recipe.GetName();
	}

	@Nullable
	@Override
	protected TumblingBarrelRecipe FindRecipe(String recipeName)
	{
		return FirstRecipeMatching(TumblingBarrelRecipe.TYPE, n -> n.GetName().equals(recipeName));
	}

	@Override
	protected void Update()
	{
		Direction.Axis axis = getBlockState().getValue(BlockStateProperties.HORIZONTAL_AXIS);
		var te = level.getBlockEntity(worldPosition.relative(axis, 1));
		var te1 = level.getBlockEntity(worldPosition.relative(axis, -1));
		var facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
		var opposite = facing.getOpposite();
		var shouldSend = false;
		if (TEHelper.IsMechanicalFace(te, opposite))
		{
			var speed = GetUser(te, opposite).GetSpeedOnFace(opposite);
			var torque = GetUser(te, opposite).GetTorqueOnFace(opposite);
			if (Mth.abs((speed - user.GetSpeed()) / speed) > 0.1 || Mth.abs((torque - user.GetTorque()) / torque) > 0.1)
				shouldSend = true;
			user.SetSpeedOnFace(facing, speed);
			user.SetTorqueOnFace(facing, torque);
		/*	user.SetSpeedOnFace(opposite, 0);
			user.SetTorqueOnFace(opposite, 0);
		} else if (TEHelper.IsMechanicalFace(te1, facing))
		{
			user.SetSpeedOnFace(opposite, GetUser(te1, facing).GetSpeedOnFace(facing));
			user.SetTorqueOnFace(opposite, GetUser(te1, facing).GetTorqueOnFace(facing));
			user.SetSpeedOnFace(facing, 0);
			user.SetTorqueOnFace(facing, 0);*/
		} else
		{
			if (user.GetSpeed() != 0 || user.GetTorque() != 0)
				shouldSend = true;
			user.SetSpeedOnFace(facing, 0);
			user.SetTorqueOnFace(facing, 0);
		}
		if (shouldSend)
		{
			setChanged();
			BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 7);
		}
	}

	@Override
	protected void UpdateClient()
	{
		rotation += GetSpeed();
		rotation %= 360;
	}

	@Override
	protected float GetProgressScalar()
	{
		var max = recipeCache.GetMaxSpeed();
		var min = recipeCache.GetMinSpeed();
		if (user.GetSpeed() > max || user.GetSpeed() < min)
			return 0;
		return user.GetTorque() > 5f ? Mth.clamp(user.GetSpeed() / 20f, min / 20f, max / 20f) : 0;
	}

	@Override
	protected int GetMaxProgress()
	{
		return recipeCache.GetTime();
	}

	@Override
	protected void OnRecipeComplete(String recipe)
	{
		var recipe1 = FindRecipe(recipe);
		processingInv.insertItem(1, recipe1.GetPrimaryItemOutputs().get(0), false);
		input.drain(recipe1.GetFluidInputs().get(0), IFluidHandler.FluidAction.EXECUTE);
		output.fill(recipe1.GetPrimaryFluidOutputs().get(0), IFluidHandler.FluidAction.EXECUTE);
		processingInv.extractItem(0, 1, false);
	}

	@Override
	protected void ReadCustomNBT(CompoundTag tag)
	{
		input.readFromNBT(tag.getCompound("inputFluid"));
		output.readFromNBT(tag.getCompound("outputFluid"));
		user.ReadFromNBT(tag.getCompound("mechanicalUser"));
	}

	@Override
	protected void WriteCustomNBT(CompoundTag tag)
	{
		tag.put("inputFluid", input.writeToNBT(new CompoundTag()));
		tag.put("outputFluid", output.writeToNBT(new CompoundTag()));
		tag.put("mechanicalUser", user.WriteToNBT());
	}

	private boolean MatchesOrEmpty(FluidStack ingredient, FluidStack real)
	{
		if (ingredient.isEmpty()) return true;
		return ingredient.isFluidEqual(real) && ingredient.getAmount() <= real.getAmount();
	}

	private boolean Fits(FluidTank tank, FluidStack stack)
	{
		return tank.fill(stack, IFluidHandler.FluidAction.SIMULATE) >= stack.getAmount();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return side == Direction.DOWN ? LazyOptional.of(() -> outputWrapper).cast() : LazyOptional.of(() -> inputWrapper).cast();
		else if (cap == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> user).cast();
		else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return side == Direction.DOWN ? LazyOptional.of(() -> output).cast() : LazyOptional.of(() -> input).cast();
		return super.getCapability(cap, side);
	}

	@Override
	public Component getDisplayName()
	{
		return new TextComponent("");
	}

	public float GetSpeed()
	{
		return EnergyConstants.RadiansSecondToDegreesTick(user.GetSpeed());
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
	{
		return new ContainerTumblingBarrel(id, playerInv, processingInv, containerData, worldPosition);
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 0, event -> {
			if (user.GetSpeed() > 0)
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tumbling_barrel.active", true));
				return PlayState.CONTINUE;
			}
			else return PlayState.STOP;
		}));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
}
