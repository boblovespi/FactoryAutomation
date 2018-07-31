/**
 * note: this class has been modified by boblovespi
 * <p>
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Jan 14, 2014, 6:45:33 PM (GMT)]
 */
package boblovespi.factoryautomation.common.guidebook.pages;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiGuidebook;
import boblovespi.factoryautomation.common.guidebook.GuidebookPage;
import com.google.common.base.Joiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "MethodCallSideOnly", "LocalVariableDeclarationSideOnly" })
public class PageText extends GuidebookPage
{
	private final String text;

	public PageText(String id, String unlocalizedName, String text)
	{
		super(new ResourceLocation(FactoryAutomation.MODID, id));
		this.text = text;
		this.unlocalizedName = unlocalizedName;
	}

	public static void renderText(int x, int y, int width, int height, String unlocalizedText)
	{
		renderText(x, y, width, height, 10, true, 0, unlocalizedText);
	}

	public static void renderText(int x, int y, int width, int height, int paragraphSize, boolean useUnicode, int color,
			String unlocalizedText)
	{
		x += 2;
		y += 10;
		width -= 4;

		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = font.getUnicodeFlag();
		if (useUnicode)
			font.setUnicodeFlag(true);
		String text = I18n.format(unlocalizedText).replaceAll("&", "\u00a7");
		String[] textEntries = text.split("<br>");

		List<List<String>> lines = new ArrayList<>();

		String controlCodes;
		for (String s : textEntries)
		{
			List<String> words = new ArrayList<>();
			String lineStr = "";
			String[] tokens = s.split(" ");
			for (String token : tokens)
			{
				String prev = lineStr;
				String spaced = token + " ";
				lineStr += spaced;

				controlCodes = toControlCodes(getControlCodes(prev));
				if (font.getStringWidth(lineStr) > width)
				{
					lines.add(words);
					lineStr = controlCodes + spaced;
					words = new ArrayList<>();
				}

				words.add(controlCodes + token);
			}

			if (!lineStr.isEmpty())
				lines.add(words);
			lines.add(new ArrayList<>());
		}

		int i = 0;
		for (List<String> words : lines)
		{
			words.size();
			int xi = x;
			int spacing = 4;
			int wcount = words.size();
			int compensationSpaces = 0;
			boolean justify = /*wcount > 0 && lines.size() > i && !lines.get(i + 1).isEmpty()*/false;

			if (justify)
			{
				String s = Joiner.on("").join(words);
				int swidth = font.getStringWidth(s);
				int space = width - swidth;

				spacing = wcount == 1 ? 0 : space / (wcount - 1);
				compensationSpaces = wcount == 1 ? 0 : space % (wcount - 1);
			}

			for (String s : words)
			{
				int extra = 0;
				if (compensationSpaces > 0)
				{
					compensationSpaces--;
					extra++;
				}
				font.drawString(s, xi, y, color);
				xi += font.getStringWidth(s) + spacing + extra;
			}

			y += words.isEmpty() ? paragraphSize : 10;
			i++;
		}

		font.setUnicodeFlag(unicode);
	}

	private static String getControlCodes(String s)
	{
		String controls = s.replaceAll("(?<!\u00a7)(.)", "");
		return controls.replaceAll(".*r", "r");
	}

	private static String toControlCodes(String s)
	{
		return s.replaceAll(".", "\u00a7$0");
	}

	@Override
	public void RenderPage(GuiGuidebook gui, int mx, int my, float partialTicks)
	{
		int width = gui.getWidth() - 30;
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + 2;

		renderText(x, y, width, gui.getHeight(), text);
	}

}
