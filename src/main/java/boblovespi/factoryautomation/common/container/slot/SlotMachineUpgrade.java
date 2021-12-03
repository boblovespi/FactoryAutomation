package boblovespi.factoryautomation.common.container.slot;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 4/21/2017.
 */
public class SlotMachineUpgrade extends SlotRestrictedPredicate
{
	public static final List<Item> UPGRADES = new ArrayList<>();


	public SlotMachineUpgrade(IItemHandler handler, int slotIndex,
			int xPosition, int yPosition)
	{
		super(handler, slotIndex, xPosition, yPosition, Ingredient.of(UPGRADES.toArray(new Item[]{})));
	}
}
