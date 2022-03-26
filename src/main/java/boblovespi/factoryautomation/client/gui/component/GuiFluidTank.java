package boblovespi.factoryautomation.client.gui.component;

import boblovespi.factoryautomation.client.gui.GuiUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;

public class GuiFluidTank
{
	private GuiBar gauge;
	private int x;
	private int y;
	private int gaugeX;
	private int gaugeY;
	private int w;
	private int h;
	private ResourceLocation guiTexture;

	public GuiFluidTank(int x, int y, int gaugeX, int gaugeY, int w, int h, ResourceLocation guiTexture)
	{
		this.x = x;
		this.y = y;
		this.gaugeX = gaugeX;
		this.gaugeY = gaugeY;
		this.w = w;
		this.h = h;
		this.guiTexture = guiTexture;
		gauge = new GuiBar(x, y, gaugeX, gaugeY, w, h, GuiBar.ProgressDirection.UP);
	}

	public void Draw(AbstractContainerScreen<?> gui, PoseStack matrix, float percentage, Fluid fluid)
	{
		if (fluid == null || fluid instanceof EmptyFluid)
			return;
		int guiLeft = gui.getGuiLeft();
		int guiTop = gui.getGuiTop();
		float aPercentage = 1 - percentage;
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		var sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
				.apply(fluid.getAttributes().getStillTexture());
		//		gui.blit(matrix, guiLeft + x, guiTop + y + (int) (h * aPercentage), sprite., 0, 16,
		//				 (int) (16 * percentage));
		var color = fluid.getAttributes().getColor();
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		float alpha = (color >> 24 & 255) / 255.0F;
		RenderSystem.setShaderColor(red, green, blue, alpha);
		int curX = guiLeft + x, curY = guiTop + y + Mth.ceil(h * aPercentage), leftX = w, leftY = (int) (h * percentage);
		while (leftX > 0)
		{
			while (leftY > 0)
			{
				GuiUtils.Blit(matrix, curX, curY, 0, sprite, Mth.clamp(leftX, 0, 16), Mth.clamp(leftY, 0, 16));
				leftY -= sprite.getHeight();
				curY += sprite.getHeight();
			}
			leftX -= sprite.getWidth();
			curX += sprite.getWidth();
		}
		RenderSystem.setShaderTexture(0, guiTexture);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		gauge.Draw(gui, matrix, percentage);
	}
}
