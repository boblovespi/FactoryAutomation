package boblovespi.factoryautomation.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.SoundEvent;

/**
 * Created by Willi on 8/1/2018.
 */
public class GuiButtonLink extends GuiButtonSound
{
	public GuiButtonLink(int id, int x, int y, int width, int height, String text, SoundEvent sound)
	{
		super(id, x, y, width, height, text, sound);
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (!visible)
			return;
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

		FontRenderer fontrenderer = mc.fontRenderer;
		int color = 14737632;

		if (isMouseOver())
			color = 16777120;

		this.drawString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2,
				color);
	}
}
