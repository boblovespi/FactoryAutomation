package boblovespi.factoryautomation.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
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

	public GuiMultiImage(int x, int y, int w, int h, int texX, int texY, int texW, int texH,
						 List<ResourceLocation> texLocs)
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

	public void Draw(AbstractContainerScreen<?> gui, PoseStack matrix)
	{
		int guiLeft = gui.getGuiLeft();
		int guiTop = gui.getGuiTop();
		RenderSystem.setShaderTexture(0, texLocs.get(texture));
		matrix.pushPose();
		{
			matrix.translate(guiLeft + x, guiTop + y, 0);
			matrix.scale(w, h, 1);
			GuiComponent.blit(matrix, 0, 0, texX, texY, texW, texH, texW, texH);
		}
		matrix.popPose();
	}
}
