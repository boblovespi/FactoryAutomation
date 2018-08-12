package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiButtonBackWithShift;
import boblovespi.factoryautomation.client.gui.component.GuiButtonPage;
import boblovespi.factoryautomation.common.guidebook.GuidebookEntries;
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
import java.util.Stack;

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

	private static GuidebookEntry currentEntry = GuidebookEntries.mainEntry;
	private static GuidebookPage currentPage = currentEntry.pages.get(0);
	private static int currentPageNum = 0;
	private static Stack<GuidebookEntry> lastEntries = new Stack<>();
	private static Stack<GuidebookPage> lastPages = new Stack<>();
	private static Stack<Integer> lastPageNums = new Stack<>();

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
		if (currentEntry != entry)
		{
			lastEntries.push(currentEntry);
			lastPages.push(currentPage);
			lastPageNums.push(currentPageNum);
		}

		currentEntry = entry;
		currentPageNum = pageNum;
		currentPage = entry.pages.get(pageNum);
		instance.InitNewPage();
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

		left = width / 2 - guiWidth / 2;
		top = height / 2 - guiHeight / 2;

		InitNewPage();
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
		drawModalRectWithCustomSizedTexture(left, top, 0, 0, guiWidth, guiHeight, 285, 256);

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
				currentPage.OnClosed(this);
				currentPageNum++;
				currentPage = currentEntry.pages.get(currentPageNum);
				InitNewPage();
			}
		} else if (button.id == leftButton.id)
		{
			if (currentPageNum > 0)
			{
				currentPage.OnClosed(this);
				currentPageNum--;
				currentPage = currentEntry.pages.get(currentPageNum);
				InitNewPage();
			}
		} else if (button.id == backButton.id)
		{
			currentEntry = lastEntries.pop();
			currentPageNum = lastPageNums.pop();
			currentPage = lastPages.pop();
			instance.InitNewPage();
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

	public void AddButton(GuiButton button) throws RuntimeException
	{
		if (button.id == rightButton.id || button.id == leftButton.id || button.id == backButton.id)
			throw new RuntimeException("Tried to register a button with a reserved id! button id: " + button.id);
		addButton(button);
	}

	private void InitNewPage()
	{
		buttonList.clear();

		buttonList.add(backButton = new GuiButtonBackWithShift(0, left + guiWidth / 2 - 8, top + guiHeight + 2));

		if (lastEntries.empty())
			backButton.enabled = false;

		buttonList.add(leftButton = new GuiButtonPage(1, left, top + guiHeight - 10, false));
		buttonList.add(rightButton = new GuiButtonPage(2, left + guiWidth - 18, top + guiHeight - 10, true));
		// buttonList.add(new GuiButtonShare(3, left + guiWidth - 6, top - 2));
		// if(entry.getWebLink() != null)
		// 	buttonList.add(new GuiButtonViewOnline(4, left - 8, top + 12));

		currentPage.OnOpened(this);
	}
}