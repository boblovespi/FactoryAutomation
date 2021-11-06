package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.ContainerStoneFoundry;
import boblovespi.factoryautomation.common.container.workbench.ContainerWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEWorkbench;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by Willi on 4/9/2018.
 */
public class GuiWorkbench extends ContainerScreen<ContainerWorkbench>
{
	public GuiWorkbench(ContainerWorkbench container, PlayerInventory playerInv, ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.workbench"));
		imageWidth = 234;
		imageHeight = 202;
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager._blendColor(1, 1, 1, 1);
		if (menu.is3x3)
			minecraft.getTextureManager().bind(
					new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/stone_workbench.png"));
		else
			minecraft.getTextureManager().bind(
					new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/workbench.png"));
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}

	@Override
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Workbench", 84, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, inventory.getDisplayName(), 8, this.imageHeight - 96 + 2, 4210752);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}
}
