package boblovespi.factoryautomation.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
	private List<PotionEffect> potionEffects;
	private List<Float> potionEffectChances;
	private int itemUseTime;
	private boolean alwaysEdible;

	public FAFood(String unName, int amount, int saturation, int eatTime, boolean WolfFood, boolean canAlwaysEat,
			List<PotionEffect> potionEffects, List<Float> potionChances)
	{
		super();

		if (potionEffects.size() != potionChances.size())
			throw new IndexOutOfBoundsException("the potionEffects and potionEffectChances sizes are not the same");

		this.unName = unName;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		//setHasSubtypes(true);

		amountOfFood = amount;
		saturationAmount = saturation;
		itemUseTime = eatTime;
		isWolfFood = WolfFood;
		this.potionEffects = potionEffects;
		potionEffectChances = potionChances;
		alwaysEdible = canAlwaysEat;

		setCreativeTab(CreativeTabs.FOOD);
		FAItems.items.add(this);

	}

	@Override
	public String getUnlocalizedName(ItemStack items)
	{
		return super.getUnlocalizedName();/* + "." + getType(
				items.getItemDamage());*/
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

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		if (!worldIn.isRemote)
		{
			for (int i = 0; i < potionEffects.size(); ++i)
			{
				if (worldIn.rand.nextFloat() <= potionEffectChances.get(i))
					player.addPotionEffect(new PotionEffect(potionEffects.get(i)));
			}
		}
	}

	@Override
	@Nullable
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		stack.shrink(1);

		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			entityplayer.getFoodStats().addStats(amountOfFood, saturationAmount / amountOfFood);

			worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
					SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F,
					worldIn.rand.nextFloat() * 0.1F + 0.9F);

			onFoodEaten(stack, worldIn, entityplayer);
			// entityplayer.addStat(StatList.getObjectUseStats(this));
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return itemUseTime;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.EAT;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (playerIn.canEat(alwaysEdible))
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		} else
		{
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		}
	}

}
