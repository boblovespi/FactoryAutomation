package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerSolidFueledFirebox;
import boblovespi.factoryautomation.common.tileentity.TESolidFueledFirebox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 10/28/2018.
 */
public class GuiSolidFueledFirebox extends GuiContainer
{
	private TESolidFueledFirebox te;
	private IInventory playerInv;
	private ContainerSolidFueledFirebox container;
	private GuiBar flameBar;
	private GuiBar tempBar;

	public GuiSolidFueledFirebox(IInventory playerInv, TileEntity te)
	{
		super(new ContainerSolidFueledFirebox(playerInv, te));
		this.playerInv = playerInv;
		this.te = (TESolidFueledFirebox) te;
		container = (ContainerSolidFueledFirebox) inventorySlots;
		tempBar = new GuiBar(103, 9, 176, 14, 6, 61, GuiBar.ProgressDirection.UP);
		flameBar = new GuiBar(80, 36, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(mc.fontRenderer, "Solid-Fueled Firebox", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 100, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		super.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(103, 9, 6, 61, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format("gui.misc.temperature") + ": " + String.format("%1$.1f\u00b0C", te.GetTemp()));
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
		mc.getTextureManager().bindTexture(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/solid_fueled_firebox.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		flameBar.Draw(this, te.GetBurnPercent());
		tempBar.Draw(this, te.GetTempPercent());

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}
}
