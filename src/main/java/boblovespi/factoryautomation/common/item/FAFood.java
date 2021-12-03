package boblovespi.factoryautomation.common.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item.Properties;

/**
 * Created by Willi on 4/12/2017.
 * A class for foods that can give more than one potion effect
 *
 * Updated 15 April 2021 on 18:16 by Qboi123 (Conversion to 1.16.5)
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FAFood extends Item implements FAItem
{
	private String unName;

	private final int amountOfFood;
	private final int saturationAmount;
	private final boolean isWolfFood;
	private final List<MobEffectInstance> potionEffects;
	private final List<Float> potionEffectChances;
	private final int itemUseTime;
	private final boolean alwaysEdible;

	public FAFood(String unName, int amount, int saturation, int eatTime, boolean WolfFood, boolean canAlwaysEat,
			List<MobEffectInstance> potionEffects, List<Float> potionChances)
	{
		super(new Properties().tab(CreativeModeTab.TAB_FOOD));

		if (potionEffects.size() != potionChances.size())
			throw new IndexOutOfBoundsException("the potionEffects and potionEffectChances sizes are not the same");

		this.unName = unName;
		// setUnlocalizedName(UnlocalizedName()); TODO: localization!!!
		setRegistryName(RegistryName());
		//setHasSubtypes(true);

		this.amountOfFood = amount;
		this.saturationAmount = saturation;
		this.itemUseTime = eatTime;
		this.isWolfFood = WolfFood;
		this.potionEffects = potionEffects;
		this.potionEffectChances = potionChances;
		this.alwaysEdible = canAlwaysEat;

		FAItems.items.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return unName;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "foods/" + unName;
	}

	@Override
	public Item ToItem()
	{
		return this;
	}

	protected void applyPotionAffects(ItemStack stack, Level world, LivingEntity player)
	{
		if (!world.isClientSide)
		{
			for (int i = 0; i < potionEffects.size(); ++i)
			{
				if (world.random.nextFloat() <= potionEffectChances.get(i))
					player.addEffect(new MobEffectInstance(potionEffects.get(i)));
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entityLiving)
	{
		stack.shrink(1);

		if (entityLiving instanceof Player)
		{
			Player playerEntity = (Player) entityLiving;
			FoodData foodData = playerEntity.getFoodData();
			foodData.setFoodLevel(foodData.getFoodLevel() + amountOfFood);
			foodData.setSaturation(foodData.getSaturationLevel() + saturationAmount / (float) amountOfFood);
//			playerEntity.getFoodData().addStats(amountOfFood, saturationAmount / (float) amountOfFood);
			world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
					SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
					world.random.nextFloat() * 0.1F + 0.9F);

			// PlayerEntity.addStat(StatList.getObjectUseStats(this));
		}
		applyPotionAffects(stack, world, entityLiving);
		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return itemUseTime;
	}

	//	@Override
	//	public EnumAction getItemUseAction(ItemStack stack)
	//	{
	//		return EnumAction.EAT;
	//	}

	@Override
	public boolean isEdible()
	{
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand hand)
	{
		ItemStack stack = playerIn.getItemInHand(hand);
		if (playerIn.canEat(alwaysEdible))
		{
			playerIn.startUsingItem(hand);
			return InteractionResultHolder.success(stack);
		} else
		{
			return InteractionResultHolder.fail(stack);
		}
	}

}
