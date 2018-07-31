package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.guidebook.GuidebookEntries;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by Willi on 7/30/2018.
 * <p>
 * code is a modified variant of botania's lexicon, made by vaskii
 * botania github: https://github.com/Vazkii/Botania
 */
public class Guidebook extends FABaseItem
{
	public Guidebook()
	{
		super("guidebook", FACreativeTabs.tools);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		FactoryAutomation.proxy.OpenGuidebook(world, player, GuidebookEntries.testEntry, ExtraInfo.NONE);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	public static class ExtraInfo
	{
		public static final ExtraInfo NONE = new ExtraInfo(0);
		public final int pageNum;

		public ExtraInfo(int pageNum)
		{
			this.pageNum = pageNum;
		}
	}
}
