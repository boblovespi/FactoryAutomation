package boblovespi.factoryautomation.common.item.types;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

import java.util.Collections;
import java.util.List;

/**
 * Created by Willi on 5/15/2019.
 */
public enum IceCreams
{
	VANILLA(Collections.singletonList(new PotionEffect(MobEffects.REGENERATION, 100))),
	COFFEE(Collections.singletonList(new PotionEffect(MobEffects.HASTE, 300))),
	MINT(Collections.singletonList(new PotionEffect(MobEffects.SPEED, 300))),
	CHOCOLATE(Collections.singletonList(new PotionEffect(MobEffects.SATURATION, 300))),
	COOKIES_N_CREAM(Collections.singletonList(new PotionEffect(MobEffects.STRENGTH, 100, 2))),;
	private List<PotionEffect> potionEffects;

	IceCreams(List<PotionEffect> potionEffects)
	{
		this.potionEffects = potionEffects;
	}

	public List<PotionEffect> GetPotionEffects()
	{
		return potionEffects;
	}
}
