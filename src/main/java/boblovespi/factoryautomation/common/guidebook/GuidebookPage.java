package boblovespi.factoryautomation.common.guidebook;

import boblovespi.factoryautomation.client.gui.GuiGuidebook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Willi on 7/30/2018.
 * <p>
 * code is a modified variant of botania's lexicon, made by vaskii
 * botania github: https://github.com/Vazkii/Botania
 */
public abstract class GuidebookPage
{
	protected ResourceLocation id;
	protected String unlocalizedName;

	protected GuidebookPage(ResourceLocation id)
	{
		this.id = id;
	}

	/**
	 * Renders the contents of the page onto the gui
	 */
	public abstract void RenderPage(GuiGuidebook gui, int mouseX, int mouseY, float partialTicks);

	/**
	 * Called per update tick.
	 */
	public void UpdateScreen(GuiGuidebook gui)
	{
	}

	/**
	 * Called when this page is opened, be it via initGui() or when the player changes page.
	 * You can add buttons and whatever you'd do on initGui() here.
	 */
	public void OnOpened(GuiGuidebook gui)
	{
	}

	/**
	 * Called when this page is opened, be it via closing the gui or when the player changes page.
	 * Make sure to dispose of anything you don't use any more, such as buttons in the gui's buttonList.
	 */
	public void OnClosed(GuiGuidebook gui)
	{
	}

	/**
	 * Called when a button is pressed, equivalent to GuiScreen.actionPerformed.
	 */
	public void OnActionPerformed(GuiGuidebook gui, GuiButton button)
	{
	}

	/**
	 * Gets the unique id for the guidebook page
	 */
	public ResourceLocation GetId()
	{
		return id;
	}

	/**
	 * Gets the unlocalized name for the guidebook page
	 */
	public String GetUnlocalizedName()
	{
		return unlocalizedName;
	}
}
