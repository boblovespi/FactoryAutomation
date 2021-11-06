package boblovespi.factoryautomation.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by Willi on 12/24/2017.
 */
@OnlyIn(Dist.CLIENT)
public class GuiBar
{
	private final int x;
	private final int y;
	private final int barX;
	private final int barY;
	private final int w;
	private final int h;
	private ProgressDirection direction;

	public GuiBar(int x, int y, int barX, int barY, int l, int w, ProgressDirection direction)
	{
		this.x = x;
		this.y = y;
		this.barX = barX;
		this.barY = barY;
		this.w = l;
		this.h = w;
		this.direction = direction;
	}

	public void Draw(ContainerScreen<?> gui, MatrixStack matrix, float percentage)
	{
		int guiLeft = gui.getGuiLeft();
		int guiTop = gui.getGuiTop();
		float aPercentage = 1 - percentage;
		switch (direction)
		{
		case DOWN:
			gui.blit(matrix, guiLeft + x, guiTop + y, barX, barY, w, (int) (h * percentage));
			break;
		case UP:
			gui.blit(matrix, guiLeft + x, guiTop + y + (int) (h * aPercentage), barX, barY + (int) (h * aPercentage), w,
					(int) (h * percentage));
			break;
		case RIGHT:
			gui.blit(matrix, guiLeft + x, guiTop + y, barX, barY, (int) (w * percentage), h);
			break;
		case LEFT:
			gui.blit(matrix, guiLeft + x + (int) (w * aPercentage), guiTop + y, barX + (int) (w * aPercentage), barY,
					(int) (w * percentage), h);
			break;
		}
	}

	public enum ProgressDirection
	{
		LEFT, RIGHT, UP, DOWN
	}
}
