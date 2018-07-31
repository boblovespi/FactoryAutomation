package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiButtonBackWithShift;
import boblovespi.factoryautomation.client.gui.component.GuiButtonPage;
import boblovespi.factoryautomation.common.guidebook.GuidebookPage;
import boblovespi.factoryautomation.common.guidebook.entry.GuidebookEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by Willi on 7/30/2018.
 * <p>
 * code is a modified variant of botania's lexicon, made by vaskii
 * botania github: https://github.com/Vazkii/Botania
 */
public class GuiGuidebook extends GuiScreen
{
	public static final ResourceLocation texture = new ResourceLocation(
			FactoryAutomation.MODID, "textures/gui/guidebook.png");
	public static GuiGuidebook instance = new GuiGuidebook();

	public static GuidebookEntry currentEntry;
	public static GuidebookPage currentPage;
	public static int currentPageNum = 0;

	final int guiWidth = 146;
	final int guiHeight = 180;
	private int lastScale = 20;
	private int left;
	private int top;
	private GuiButtonBackWithShift backButton;
	private GuiButtonPage leftButton;
	private GuiButtonPage rightButton;

	public static int getMaxAllowedScale()
	{
		Minecraft mc = Minecraft.getMinecraft();
		int scale = mc.gameSettings.guiScale;
		mc.gameSettings.guiScale = 0;
		ScaledResolution res = new ScaledResolution(mc);
		mc.gameSettings.guiScale = scale;

		return res.getScaleFactor();
	}

	public static void SetPage(GuidebookEntry entry, int pageNum)
	{
		currentEntry = entry;
		currentPageNum = pageNum;
		currentPage = entry.pages.get(pageNum);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void initGui()
	{
		super.initGui();

		int guiScale = mc.gameSettings.guiScale;
		int persistentScale = Math.min(lastScale, getMaxAllowedScale());

		if (persistentScale > 0 && persistentScale != guiScale)
		{
			mc.gameSettings.guiScale = persistentScale;
			ScaledResolution res = new ScaledResolution(mc);
			width = res.getScaledWidth();
			height = res.getScaledHeight();
			mc.gameSettings.guiScale = guiScale;
		}

		System.out.println("guiWidth = " + guiWidth);
		System.out.println("guiHeight = " + guiHeight);
		System.out.println("width = " + width);
		System.out.println("height = " + height);
		System.out.println("texture = " + texture);

		left = width / 2 - guiWidth / 2;
		top = height / 2 - guiHeight / 2;

		buttonList.add(backButton = new GuiButtonBackWithShift(0, left + guiWidth / 2 - 8, top + guiHeight + 2));
		buttonList.add(leftButton = new GuiButtonPage(1, left, top + guiHeight - 10, false));
		buttonList.add(rightButton = new GuiButtonPage(2, left + guiWidth - 18, top + guiHeight - 10, true));
		// buttonList.add(new GuiButtonShare(3, left + guiWidth - 6, top - 2));
		// if(entry.getWebLink() != null)
		// 	buttonList.add(new GuiButtonViewOnline(4, left - 8, top + 12));

		currentPage.OnOpened(this);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		ScaledResolution res = new ScaledResolution(mc);
		int guiScale = mc.gameSettings.guiScale;

		GlStateManager.pushMatrix();
		int persistentScale = Math.min(lastScale, getMaxAllowedScale());

		if (persistentScale > 0 && persistentScale != guiScale)
		{
			mc.gameSettings.guiScale = persistentScale;
			float s = (float) persistentScale / (float) res.getScaleFactor();

			GlStateManager.scale(s, s, s);

			res = new ScaledResolution(mc);
			int sw = res.getScaledWidth();
			int sh = res.getScaledHeight();
			mouseX = Mouse.getX() * sw / mc.displayWidth;
			mouseY = sh - Mouse.getY() * sh / mc.displayHeight - 1;
		}

		drawScreenAfterScale(mouseX, mouseY, partialTicks);

		mc.gameSettings.guiScale = guiScale;
		GlStateManager.popMatrix();
	}

	public void drawScreenAfterScale(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(texture);

		// drawNotes(partialTicks);

		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

		// String subtitle = getSubtitle();

		// if (subtitle != null)
		// drawBookmark(left + guiWidth / 2, top - getTitleHeight() + 10, subtitle, true, 191);
		// drawBookmark(left + guiWidth / 2, top - getTitleHeight(), getTitle(), true);

		// if (isMainPage())
		// drawHeader();

		//		if (bookmarksNeedPopulation)
		//		{
		//			populateBookmarks();
		//			bookmarksNeedPopulation = false;
		//		}
		super.drawScreen(mouseX, mouseY, partialTicks);

		//		if (hasTutorialArrow)
		//		{
		//			mc.renderEngine.bindTexture(texture);
		//			GlStateManager.enableBlend();
		//			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//			GlStateManager.color(1F, 1F, 1F,
		//					0.7F + (float) (Math.sin((ClientTickHandler.ticksInGame + newPartialTicks) * 0.3F) + 1) * 0.15F);
		//			drawTexturedModalRect(tutorialArrowX, tutorialArrowY, 20, 200, TUTORIAL_ARROW_WIDTH, TUTORIAL_ARROW_HEIGHT);
		//			GlStateManager.disableBlend();
		//		}

		currentPage.RenderPage(this, mouseX, mouseY, partialTicks);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		currentPage.UpdateScreen(this);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		currentPage.OnActionPerformed(this, button);

		int pageSizes = currentEntry.pages.size();

		if (button.id == rightButton.id)
		{
			if (currentPageNum < pageSizes - 1)
			{
				currentPageNum++;
				currentPage = currentEntry.pages.get(currentPageNum);
			}
		} else if (button.id == leftButton.id)
		{
			if (currentPageNum > 0)
			{
				currentPageNum--;
				currentPage = currentEntry.pages.get(currentPageNum);
			}
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		currentPage.OnClosed(this);
	}

	public int getWidth()
	{
		return guiWidth;
	}

	public int getLeft()
	{
		return left;
	}

	public int getTop()
	{
		return top;
	}

	public int getHeight()
	{
		return guiHeight;
	}
}