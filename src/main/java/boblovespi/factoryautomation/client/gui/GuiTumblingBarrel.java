package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerTumblingBarrel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiTumblingBarrel extends AbstractContainerScreen<ContainerTumblingBarrel>
{
	private final GuiBar progressBar;

	public GuiTumblingBarrel(ContainerTumblingBarrel container, Inventory playerInv, Component unused)
	{
		super(container, playerInv, new TranslatableComponent("gui.tumbling_barrel"));
		progressBar = new GuiBar(80, 34, 177, 14, 22, 16, GuiBar.ProgressDirection.RIGHT);
		imageHeight = 180;
	}

	@Override
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Tumbling Barrel", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, playerInventoryTitle, 100, this.imageHeight - 96 + 2, 4210752);
	}

	@Override
	protected void renderTooltip(PoseStack matrix, int mouseX, int mouseY)
	{
		super.renderTooltip(matrix, mouseX, mouseY);
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/tumbling_barrel.png"));
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		progressBar.Draw(this, matrix, menu.GetBar(1) / (float) menu.GetBar(0));
		fill(matrix, leftPos + 8, topPos + 67 - 59 * menu.GetBar(2) / 2000, leftPos + 24, topPos + 67,
			 0xffffaa00);
		fill(matrix, leftPos + 152, topPos + 67 - 59 * menu.GetBar(3) / 2000, leftPos + 168, topPos + 67,
			 0xff5555ff);

	}
}
