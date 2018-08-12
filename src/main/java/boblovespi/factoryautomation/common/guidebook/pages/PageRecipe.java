/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Feb 8, 2014, 2:46:36 PM (GMT)]
 */
package boblovespi.factoryautomation.common.guidebook.pages;

import boblovespi.factoryautomation.client.gui.GuiGuidebook;
import boblovespi.factoryautomation.common.guidebook.GuidebookPage;
import boblovespi.factoryautomation.common.guidebook.GuidebookRecipeMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class PageRecipe extends GuidebookPage
{
	static boolean mouseDownLastTick = false;
	int relativeMouseX, relativeMouseY;
	ItemStack tooltipStack = ItemStack.EMPTY, tooltipContainerStack = ItemStack.EMPTY;
	boolean tooltipEntry;

	public PageRecipe(ResourceLocation id)
	{
		super(id);
	}

	public PageRecipe SetItem(ItemStack item)
	{
		tooltipStack = item.copy();
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void RenderPage(GuiGuidebook gui, int mx, int my, float partialTicks)
	{
		relativeMouseX = mx;
		relativeMouseY = my;

		renderRecipe(gui, mx, my);

		int width = gui.getWidth() - 30;
		int height = gui.getHeight();
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + height - 40;
		PageText.renderText(x, y, width, height, GetUnlocalizedName());

		if (!tooltipStack.isEmpty())
		{
			List<String> tooltipData = tooltipStack
					.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
			List<String> parsedTooltip = new ArrayList<>();
			boolean first = true;

			for (String s : tooltipData)
			{
				String s_ = s;
				if (!first)
					s_ = TextFormatting.GRAY + s;
				parsedTooltip.add(s_);
				first = false;
			}

			FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			int tooltipHeight = tooltipData.size() * 10 + 2;
			int tooltipWidth = parsedTooltip.stream().map(font::getStringWidth).max(Comparator.comparingInt(a -> a))
											.orElse(0);
			int rmx = mx + 12;
			int rmy = my - 12;

			boblovespi.factoryautomation.client.gui.helper.RenderHelper.renderTooltip(mx, my, parsedTooltip);
			GlStateManager.disableDepth();
			MinecraftForge.EVENT_BUS
					.post(new RenderTooltipEvent.PostBackground(tooltipStack, parsedTooltip, rmx, rmy, font,
							tooltipWidth, tooltipHeight));
			MinecraftForge.EVENT_BUS
					.post(new RenderTooltipEvent.PostText(tooltipStack, parsedTooltip, rmx, rmy, font, tooltipWidth,
							tooltipHeight));
			GlStateManager.enableDepth();

			int tooltipY = 8 + tooltipData.size() * 11;

			if (tooltipEntry)
			{
				boblovespi.factoryautomation.client.gui.helper.RenderHelper.renderTooltipOrange(mx, my + tooltipY,
						Collections.singletonList(TextFormatting.GRAY + I18n.format("botaniamisc.clickToRecipe")));
				tooltipY += 18;
			}

			if (!tooltipContainerStack.isEmpty())
				boblovespi.factoryautomation.client.gui.helper.RenderHelper
						.renderTooltipGreen(mx, my + tooltipY, Arrays.asList(
								TextFormatting.AQUA + I18n.format("botaniamisc.craftingContainer"),
								tooltipContainerStack.getDisplayName()));
		}

		tooltipStack = tooltipContainerStack = ItemStack.EMPTY;
		tooltipEntry = false;
		GlStateManager.disableBlend();
		mouseDownLastTick = Mouse.isButtonDown(0);
	}

	@SideOnly(Side.CLIENT)
	public void renderRecipe(GuiGuidebook gui, int mx, int my)
	{
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtAngle(GuiGuidebook gui, float angle, ItemStack stack)
	{
		if (stack.isEmpty())
			return;

		ItemStack workStack = stack.copy();

		if (workStack.getItemDamage() == Short.MAX_VALUE || workStack.getItemDamage() == -1)
			workStack.setItemDamage(0);

		angle -= 90;
		int radius = 32;
		double xPos = gui.getLeft() + Math.cos(angle * Math.PI / 180D) * radius + gui.getWidth() / 2 - 8;
		double yPos = gui.getTop() + Math.sin(angle * Math.PI / 180D) * radius + 53;

		renderItem(gui, xPos, yPos, workStack, false);
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtGridPos(GuiGuidebook gui, int x, int y, ItemStack stack, boolean accountForContainer)
	{
		if (stack.isEmpty())
			return;
		stack = stack.copy();

		if (stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		int xPos = gui.getLeft() + x * 29 + 7 + (y == 0 && x == 3 ? 10 : 0);
		int yPos = gui.getTop() + y * 29 + 24 - (y == 0 ? 7 : 0);
		ItemStack stack1 = stack.copy();
		if (stack1.getItemDamage() == -1)
			stack1.setItemDamage(0);

		renderItem(gui, xPos, yPos, stack1, accountForContainer);
	}

	@SideOnly(Side.CLIENT)
	public void renderItem(GuiGuidebook gui, double xPos, double yPos, ItemStack stack, boolean accountForContainer)
	{
		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		boolean mouseDown = Mouse.isButtonDown(0);

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableDepth();
		GlStateManager.pushMatrix();
		GlStateManager.translate(xPos, yPos, 0);
		render.renderItemAndEffectIntoGUI(stack, 0, 0);
		render.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, stack, 0, 0);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();

		int xpi = (int) xPos;
		int ypi = (int) yPos;
		if (relativeMouseX >= xpi && relativeMouseY >= ypi && relativeMouseX <= xpi + 16 && relativeMouseY <= ypi + 16)
		{
			tooltipStack = stack;

			GuidebookRecipeMappings.EntryData data = GuidebookRecipeMappings.getDataForStack(tooltipStack);
			/*ItemStack book = PlayerHelper.getFirstHeldItemClass(Minecraft.getMinecraft().player, IGuidebook.class);*/

			if (data != null/* && (data.entry != gui.getEntry() || data.page != gui.getPageOn()) && book != null
					&& ((IGuidebook) book.getItem()).isKnowledgeUnlocked(book, data.entry.getKnowledgeType())*/)
			{
				tooltipEntry = true;

				if (!mouseDownLastTick && mouseDown && GuiScreen.isShiftKeyDown())
				{
					GuiGuidebook.SetPage(data.entry, data.page);
				}
			} else
				tooltipEntry = false;

			if (accountForContainer)
			{
				ItemStack containerStack = stack.getItem().getContainerItem(stack);
				if (!containerStack.isEmpty())
					tooltipContainerStack = containerStack;
			}
		}

		GlStateManager.disableLighting();
	}

}
