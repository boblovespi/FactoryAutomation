package boblovespi.factoryautomation.common.tileentity.smelting;

import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 4/9/2019.
 */
public interface ICastingVessel
{
	void castInto(ItemStack stack, int temp);

	float getLoss();

	boolean hasSpace();

	TEStoneCrucible.MetalForms getForm();
}
