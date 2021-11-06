package boblovespi.factoryautomation.client.gui.component;

import boblovespi.factoryautomation.common.util.IGuiElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by Willi on 6/9/2018.
 */
@OnlyIn(Dist.CLIENT)
public class GuiGridElement
{
	private final int x;
	private final int y;
	private final int offsetX;
	private final int offsetY;
	private final int w;
	private final int h;
	private IGuiElement element;

	public GuiGridElement(int x, int y, int offsetX, int offsetY, int w, int h, IGuiElement element)
	{
		this.x = x;
		this.y = y;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.w = w;
		this.h = h;
		this.element = element;
	}

	public void SetElement(IGuiElement element)
	{
		this.element = element;
	}

	public void Draw(ContainerScreen<?> gui, MatrixStack matrix)
	{
		int guiLeft = gui.getGuiLeft();
		int guiTop = gui.getGuiTop();

		gui.blit(matrix, guiLeft + x, guiTop + y, offsetX + element.GetX() * w, offsetY + element.GetY() * h,
				w * element.GetU(), h * element.GetV());
	}
}
