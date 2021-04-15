package boblovespi.factoryautomation.common.item.types;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.Collections;
import java.util.List;

/**
 * Created by Willi on 5/15/2019.
 */
public enum IceCreams
{
	VANILLA(Collections.singletonList(new EffectInstance(Effects.REGENERATION, 100))),
	COFFEE(Collections.singletonList(new EffectInstance(Effects.DIG_SPEED, 300))),
	MINT(Collections.singletonList(new EffectInstance(Effects.MOVEMENT_SPEED, 300))),
	CHOCOLATE(Collections.singletonList(new EffectInstance(Effects.SATURATION, 300))),
	COOKIES_N_CREAM(Collections.singletonList(new EffectInstance(Effects.DAMAGE_RESISTANCE, 100, 2))),
	SWEET_BERRY(Collections.singletonList(new EffectInstance(Effects.HEAL, 1)));
	private final List<EffectInstance> potionEffects;

	IceCreams(List<EffectInstance> potionEffects)
	{
		this.potionEffects = potionEffects;
	}

	public List<EffectInstance> GetPotionEffects()
	{
		return potionEffects;
	}
}
