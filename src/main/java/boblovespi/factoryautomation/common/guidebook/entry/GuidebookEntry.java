/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Jan 14, 2014, 6:17:06 PM (GMT)]
 */
package boblovespi.factoryautomation.common.guidebook.entry;

import boblovespi.factoryautomation.common.guidebook.GuidebookPage;
import boblovespi.factoryautomation.common.guidebook.category.GuidebookCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuidebookEntry implements Comparable<GuidebookEntry>
{

	public final String unlocalizedName;
	public final GuidebookCategory category;

	public final List<GuidebookPage> pages = new ArrayList<>();
	private final List<ItemStack> extraDisplayedRecipes = new ArrayList<>();
	private boolean priority = false;
	private ItemStack icon = ItemStack.EMPTY;

	/**
	 * @param unlocalizedName The unlocalized name of this entry. This will be localized by the client display.
	 */
	public GuidebookEntry(String unlocalizedName, GuidebookCategory category)
	{
		this.unlocalizedName = unlocalizedName;
		this.category = category;
	}

	/**
	 * Sets this page as prioritized, as in, will appear before others in the guidebook.
	 */
	public GuidebookEntry setPriority()
	{
		priority = true;
		return this;
	}

	public ItemStack getIcon()
	{
		return icon;
	}

	/**
	 * Sets the display icon for this entry. Overriding the one already there. When adding recipe pages to the
	 * entry, this will be called once for the result of the first found recipe.
	 */
	public void setIcon(ItemStack stack)
	{
		icon = stack;
	}

	public boolean isPriority()
	{
		return priority;
	}

	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	public String getTagline()
	{
		return ""; // Override this if you want a tagline. You probably do
	}

	@SideOnly(Side.CLIENT)
	public boolean isVisible()
	{
		return true;
	}

	/**
	 * Sets what pages you want this entry to have.
	 */
	public GuidebookEntry setGuidebookPages(GuidebookPage... pages)
	{
		this.pages.addAll(Arrays.asList(pages));

		for (int i = 0; i < this.pages.size(); i++)
		{
			GuidebookPage page = this.pages.get(i);
		}

		return this;
	}

	/**
	 * Returns the web link for this entry. If this isn't null, looking at this entry will
	 * show a "View Online" button in the book. The String returned should be the URL to
	 * open when the button is clicked.
	 */
	public String getWebLink()
	{
		return null;
	}

	/**
	 * Adds a page to the list of pages.
	 */
	public void addPage(GuidebookPage page)
	{
		pages.add(page);
	}

	public final String getNameForSorting()
	{
		return (priority ? 0 : 1) + I18n.translateToLocal(getUnlocalizedName());
	}

	//	public List<ItemStack> getDisplayedRecipes() {
	//		ArrayList<ItemStack> list = new ArrayList<>();
	//		for(GuidebookPage page : pages) {
	//			ArrayList<ItemStack> itemsAddedThisPage = new ArrayList<>();
	//
	//			for(ItemStack s : page.getDisplayedRecipes()) {
	//				addItem: {
	//					for(ItemStack s1 : itemsAddedThisPage)
	//						if(s1.getItem() == s.getItem())
	//							break addItem;
	//					for(ItemStack s1 : list)
	//						if(s1.isItemEqual(s) && ItemStack.areItemStackTagsEqual(s1, s))
	//							break addItem;
	//
	//					itemsAddedThisPage.add(s);
	//					list.add(s);
	//				}
	//			}
	//		}
	//
	//		list.addAll(extraDisplayedRecipes);
	//
	//		return list;
	//	}

	public void addExtraDisplayedRecipe(ItemStack stack)
	{
		extraDisplayedRecipes.add(stack);
	}

	@Override
	public int compareTo(@Nonnull GuidebookEntry o)
	{
		return getNameForSorting().compareTo(o.getNameForSorting());
	}

}