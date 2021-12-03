package boblovespi.factoryautomation.common.item.types;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Collections;
import java.util.List;

/**
 * Created by Willi on 5/15/2019.
 */
public enum IceCreams
{
	VANILLA(Collections.singletonList(new MobEffectInstance(MobEffects.REGENERATION, 100))),
	COFFEE(Collections.singletonList(new MobEffectInstance(MobEffects.DIG_SPEED, 300))),
	MINT(Collections.singletonList(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300))),
	CHOCOLATE(Collections.singletonList(new MobEffectInstance(MobEffects.SATURATION, 300))),
	COOKIES_N_CREAM(Collections.singletonList(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2))),
	SWEET_BERRY(Collections.singletonList(new MobEffectInstance(MobEffects.HEAL, 1)));
	private final List<MobEffectInstance> potionEffects;

	IceCreams(List<MobEffectInstance> potionEffects)
	{
		this.potionEffects = potionEffects;
	}

	public List<MobEffectInstance> GetPotionEffects()
	{
		return potionEffects;
	}
}
