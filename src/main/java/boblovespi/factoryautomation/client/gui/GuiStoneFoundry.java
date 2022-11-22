package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerStoneFoundry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Created by Willi on 10/28/2018.
 */
public class GuiStoneFoundry extends AbstractContainerScreen<ContainerStoneFoundry>
{
	private GuiBar flameBar;
	private GuiBar tempBar;
	private GuiBar progressBar;

	public GuiStoneFoundry(ContainerStoneFoundry container, Inventory playerInv, Component unused)
	{
		super(container, playerInv, Component.translatable("gui.stone_foundry"));
		tempBar = new GuiBar(53, 16, 176, 17, 6, 61, GuiBar.ProgressDirection.UP);
		flameBar = new GuiBar(67, 40, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
		progressBar = new GuiBar(84, 21, 194, 2, 22, 10, GuiBar.ProgressDirection.RIGHT);
		imageHeight = 180;
	}

	@Override
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Stone Foundry", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, playerInventoryTitle, 100, this.imageHeight - 96 + 2, 4210752);
	}

	@Override
	protected void renderTooltip(PoseStack matrix, int mouseX, int mouseY)
	{
		super.renderTooltip(matrix, mouseX, mouseY);
		if (isHovering(53, 16, 6, 61, mouseX, mouseY))
		{
			Component text = Component.literal(I18n.get("gui.misc.temperature") + ": " + String.format("%1$.1f\u00b0C", menu.GetBar(5) / 10f));
			renderTooltip(matrix, text, mouseX, mouseY);
		}
		if (isHovering(107, 17, 16, 59, mouseX, mouseY))
		{
			Component text = Component.literal(I18n.get(menu.GetMetalName()) + ": " + menu.GetBar(6));
			renderTooltip(matrix, text, mouseX, mouseY);
		}
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
		RenderSystem.setShaderTexture(0,
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/stone_foundry.png"));
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		flameBar.Draw(this, matrix, menu.GetBar(0) / 100f);
		tempBar.Draw(this, matrix, menu.GetBar(1) / 100f);
		progressBar.Draw(this, matrix, menu.GetBar(2) / 100f);
		fill(matrix, leftPos + 107, topPos + (int) (76 - 59 * menu.GetBar(3) / 100f), leftPos + 123, topPos + 76,
				menu.GetBar(4));

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}
}
