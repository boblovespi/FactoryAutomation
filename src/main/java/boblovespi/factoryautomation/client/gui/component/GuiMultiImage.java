package boblovespi.factoryautomation.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiMultiImage
{
	private final int x;
	private final int y;
	private final float w;
	private final float h;
	private final int texX;
	private final int texY;
	private final int texW;
	private final int texH;
	private final List<ResourceLocation> texLocs;
	private int texture;

	public GuiMultiImage(int x, int y, int w, int h, int texX, int texY, int texW, int texH, List<ResourceLocation> texLocs)
	{
		this.x = x;
		this.y = y;
		this.w = (float) w / texW;
		this.h = (float) h / texH;
		this.texX = texX;
		this.texY = texY;
		this.texW = texW;
		this.texH = texH;
		this.texLocs = texLocs;
	}

	public void SetTexture(int number)
	{
		if (number < 0)
			number = 0;
		if (number >= texLocs.size())
			number = texLocs.size() - 1;
		texture = number;
	}

	public void Draw(ContainerScreen<?> gui, MatrixStack matrix)
	{
		int guiLeft = gui.getGuiLeft();
		int guiTop = gui.getGuiTop();
		gui.getMinecraft().getTextureManager().bind(texLocs.get(texture));
		matrix.pushPose();
		{
			matrix.translate(guiLeft + x, guiTop + y, 0);
			matrix.scale(w, h, 1);
			AbstractGui.blit(matrix, 0, 0, texX, texY, texW, texH, texW, texH);
		}
		matrix.popPose();
	}
}
