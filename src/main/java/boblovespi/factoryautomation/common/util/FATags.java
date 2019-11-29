package boblovespi.factoryautomation.common.util;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Willi on 11/29/2019.
 */
public class FATags
{
	// public static final Tag<Item> INGOT_BRONZE = itemTag("ingots/bronze");

	public static Tag<Item> itemTag(String name)
	{
		return ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", name));
	}
}
