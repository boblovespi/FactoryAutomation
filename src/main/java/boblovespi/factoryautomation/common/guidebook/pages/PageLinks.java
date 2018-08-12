package boblovespi.factoryautomation.common.guidebook.pages;

import boblovespi.factoryautomation.client.gui.GuiGuidebook;
import boblovespi.factoryautomation.client.gui.component.GuiButtonLink;
import boblovespi.factoryautomation.common.guidebook.GuidebookPage;
import boblovespi.factoryautomation.common.guidebook.entry.GuidebookEntry;
import boblovespi.factoryautomation.common.util.Sounds;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Willi on 8/1/2018.
 */
public class PageLinks extends GuidebookPage
{
	private final GuidebookEntry[] entries;

	public PageLinks(ResourceLocation id, GuidebookEntry... entries)
	{
		super(id);
		this.entries = entries;
	}

	/**
	 * Renders the contents of the page onto the gui
	 */
	@Override
	public void RenderPage(GuiGuidebook gui, int mouseX, int mouseY, float partialTicks)
	{

	}

	/**
	 * Called when this page is opened, be it via initGui() or when the player changes page.
	 * You can add buttons and whatever you'd do on initGui() here.
	 */
	@SuppressWarnings({ "MethodCallSideOnly", "NewExpressionSideOnly" })
	@Override
	public void OnOpened(GuiGuidebook gui)
	{
		int width = gui.getWidth() - 30;
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + 2;

		for (int i = 0, entriesLength = entries.length; i < entriesLength; i++)
		{
			GuidebookEntry entry = entries[i];
			gui.AddButton(
					new GuiButtonLink(-1 - i, x, y + 10 * i, width, 10, entry.getUnlocalizedName(), Sounds.guidebook));
		}
	}

	/**
	 * Called when a button is pressed, equivalent to GuiScreen.actionPerformed.
	 */
	@Override
	public void OnActionPerformed(GuiGuidebook gui, GuiButton button)
	{
		int id = button.id;

		if (id < 0)
		{
			GuidebookEntry entry = entries[-1 - id];

			GuiGuidebook.SetPage(entry, 0);
		}
	}
}
