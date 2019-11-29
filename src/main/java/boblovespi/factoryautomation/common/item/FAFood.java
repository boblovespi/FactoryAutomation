package boblovespi.factoryautomation.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Willi on 4/12/2017.
 * A class for foods that can give more than one potion effect
 */
public class FAFood extends Item implements FAItem
{
	private String unName;

	private int amountOfFood;
	private int saturationAmount;
	private boolean isWolfFood;
	private List<EffectInstance> potionEffects;
	private List<Float> potionEffectChances;
	private int itemUseTime;
	private boolean alwaysEdible;

	public FAFood(String unName, int amount, int saturation, int eatTime, boolean WolfFood, boolean canAlwaysEat,
			List<EffectInstance> potionEffects, List<Float> potionChances)
	{
		super(new Properties().group(ItemGroup.FOOD));

		if (potionEffects.size() != potionChances.size())
			throw new IndexOutOfBoundsException("the potionEffects and potionEffectChances sizes are not the same");

		this.unName = unName;
		// setUnlocalizedName(UnlocalizedName()); TODO: localization!!!
		setRegistryName(RegistryName());
		//setHasSubtypes(true);

		amountOfFood = amount;
		saturationAmount = saturation;
		itemUseTime = eatTime;
		isWolfFood = WolfFood;
		this.potionEffects = potionEffects;
		potionEffectChances = potionChances;
		alwaysEdible = canAlwaysEat;

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

	protected void applyPotionAffects(ItemStack stack, World worldIn, LivingEntity player)
	{
		if (!worldIn.isRemote)
		{
			for (int i = 0; i < potionEffects.size(); ++i)
			{
				if (worldIn.rand.nextFloat() <= potionEffectChances.get(i))
					player.addPotionEffect(new EffectInstance(potionEffects.get(i)));
			}
		}
	}

	@Override
	@Nullable
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
	{
		stack.shrink(1);

		if (entityLiving instanceof PlayerEntity)
		{
			PlayerEntity playerEntity = (PlayerEntity) entityLiving;
			playerEntity.getFoodStats().addStats(amountOfFood, saturationAmount / (float) amountOfFood);
			worldIn.playSound(null, playerEntity.posX, playerEntity.posY, playerEntity.posZ,
					SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F,
					worldIn.rand.nextFloat() * 0.1F + 0.9F);

			// entityplayer.addStat(StatList.getObjectUseStats(this));
		}
		applyPotionAffects(stack, worldIn, entityLiving);
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
	public boolean isFood()
	{
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (playerIn.canEat(alwaysEdible))
		{
			playerIn.setActiveHand(hand);
			return ActionResult.newResult(ActionResultType.SUCCESS, stack);
		} else
		{
			return ActionResult.newResult(ActionResultType.FAIL, stack);
		}
	}

}
