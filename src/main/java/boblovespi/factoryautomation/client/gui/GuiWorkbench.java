package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.workbench.ContainerWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEWorkbench;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Willi on 4/9/2018.
 */
public class GuiWorkbench extends GuiContainer
{
	protected TEWorkbench te;
	protected IItemHandler inv;
	protected ContainerWorkbench container;
	private IInventory playerInv;

	public GuiWorkbench(IInventory playerInv, TileEntity te)
	{
		super(new ContainerWorkbench(playerInv, te));
		this.playerInv = playerInv;
		this.te = (TEWorkbench) te;
		container = (ContainerWorkbench) this.inventorySlots;

		xSize = 234;
		ySize = 202;
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1, 1, 1);
		if (container.is3x3)
			mc.getTextureManager()
			  .bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/stone_workbench.png"));
		else
			mc.getTextureManager()
			  .bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/workbench.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(mc.fontRenderer, "Workbench", 84, 6, 180 + 100 * 256 + 100 * 256 * 256);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
}
