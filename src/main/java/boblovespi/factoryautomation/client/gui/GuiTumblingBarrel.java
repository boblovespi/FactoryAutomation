package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.client.gui.component.GuiFluidTank;
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
	private static final ResourceLocation guiImage = new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/tumbling_barrel.png");
	private GuiBar progressBar;
	private GuiFluidTank input;
	private GuiFluidTank output;

	public GuiTumblingBarrel(ContainerTumblingBarrel container, Inventory playerInv, Component unused)
	{
		super(container, playerInv, new TranslatableComponent("gui.tumbling_barrel"));
		progressBar = new GuiBar(80, 34, 177, 14, 22, 16, GuiBar.ProgressDirection.RIGHT);
		input = new GuiFluidTank(8, 8, 182, 32, 16, 59, guiImage);
		output = new GuiFluidTank(152, 8, 182, 32, 16, 59, guiImage);
		imageHeight = 180;
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}

	@Override
	protected void renderTooltip(PoseStack matrix, int mouseX, int mouseY)
	{
		super.renderTooltip(matrix, mouseX, mouseY);
		input.RenderTooltip(this, matrix, mouseX, mouseY, menu.GetFluidIn(), menu.GetBar(2));
		output.RenderTooltip(this, matrix, mouseX, mouseY, menu.GetFluidOut(), menu.GetBar(3));
	}

	@Override
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Tumbling Barrel", 76, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, playerInventoryTitle, 100, this.imageHeight - 96 + 2, 4210752);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, guiImage);
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		progressBar.Draw(this, matrix, menu.GetBar(1) / (float) menu.GetBar(0));
		input.Draw(this, matrix, menu.GetBar(2) / 2000f, menu.GetFluidIn());
		output.Draw(this, matrix, menu.GetBar(3) / 2000f, menu.GetFluidOut());
	}
}
