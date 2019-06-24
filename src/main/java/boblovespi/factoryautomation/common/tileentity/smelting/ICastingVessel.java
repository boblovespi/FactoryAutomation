package boblovespi.factoryautomation.common.tileentity.smelting;

import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 4/9/2019.
 */
public interface ICastingVessel
{
	void CastInto(ItemStack stack, int temp);

	float GetLoss();

	boolean HasSpace();

	TEStoneCrucible.MetalForms GetForm();
}
