package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.workbench.ContainerWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEWorkbench;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by Willi on 4/9/2018.
 */
public class GuiWorkbench extends ContainerScreen<ContainerWorkbench>
{
	protected TEWorkbench te;

	public GuiWorkbench(PlayerInventory playerInv, TileEntity te)
	{
		super(new ContainerWorkbench(playerInv, te), playerInv, new TranslationTextComponent("gui.workbench"));
		this.te = (TEWorkbench) te;

		xSize = 234;
		ySize = 202;
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.blendColor(1, 1, 1, 1);
		if (container.is3x3)
			minecraft.getTextureManager().bindTexture(
					new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/stone_workbench.png"));
		else
			minecraft.getTextureManager().bindTexture(
					new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/workbench.png"));
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(minecraft.fontRenderer, "Workbench", 84, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
}
