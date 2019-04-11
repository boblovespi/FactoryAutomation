package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerBrickFoundry;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 4/11/2019.
 */
public class GuiBrickFoundry extends GuiContainer
{
	private TEBrickCrucible te;
	private IInventory playerInv;
	private ContainerBrickFoundry container;
	private GuiBar flameBar;
	private GuiBar tempBar;
	private GuiBar progressBar;

	public GuiBrickFoundry(IInventory playerInv, TileEntity te)
	{
		super(new ContainerBrickFoundry(playerInv, te));
		this.playerInv = playerInv;
		this.te = (TEBrickCrucible) te;
		container = (ContainerBrickFoundry) inventorySlots;
		tempBar = new GuiBar(53, 16, 176, 16, 6, 61, GuiBar.ProgressDirection.UP);
		flameBar = new GuiBar(67, 40, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
		progressBar = new GuiBar(84, 21, 194, 2, 22, 10, GuiBar.ProgressDirection.RIGHT);
		ySize = 180;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(mc.fontRenderer, "Brick Foundry", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 100, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		super.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(53, 16, 6, 61, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format("gui.misc.temperature") + ": " + String.format("%1$.1f\u00b0C", te.GetTemp()));
			drawHoveringText(text, mouseX, mouseY);
		}
		if (isPointInRegion(107, 17, 16, 59, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format(te.GetMetalName()) + ": " + te.GetAmountMetal());
			drawHoveringText(text, mouseX, mouseY);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1, 1, 1);
		mc.getTextureManager()
		  .bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/brick_foundry.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		flameBar.Draw(this, te.GetBurnPercent());
		tempBar.Draw(this, te.GetTempPercent());
		progressBar.Draw(this, te.GetMeltPercent());
		drawRect(guiLeft + 107, guiTop + (int) (76 - 59 * te.GetCapacityPercent()), guiLeft + 123, guiTop + 76,
				te.GetColor());

	}
}