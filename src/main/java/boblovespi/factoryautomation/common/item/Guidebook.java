package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
		super("guidebook", FAItemGroups.tools);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		FactoryAutomation.proxy.OpenGuidebook(world, player, null, ExtraInfo.NONE);
		return ActionResult.newResult(ActionResultType.SUCCESS, player.getHeldItem(hand));
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
