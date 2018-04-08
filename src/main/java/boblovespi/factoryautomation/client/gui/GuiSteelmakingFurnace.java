package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerSteelmakingFurnace;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/24/2017.
 */
public class GuiSteelmakingFurnace extends GuiContainer
{
	private TESteelmakingFurnace te;
	private IInventory playerInv;

	private GuiBar flameBar;
	private GuiBar airTankBar;
	private GuiBar fuelTankBar;
	private GuiBar tempBar;
	private GuiBar progressBar;

	public GuiSteelmakingFurnace(IInventory playerInv, TileEntity te)
	{
		super(new ContainerSteelmakingFurnace(playerInv, te));
		this.te = (TESteelmakingFurnace) te;
		this.playerInv = playerInv;

		this.xSize = 176;
		this.ySize = 180;

		flameBar = new GuiBar(
				59, 57, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
		airTankBar = new GuiBar(
				8, 8, 182, 32, 16, 59, GuiBar.ProgressDirection.UP);
		fuelTankBar = new GuiBar(
				28, 8, 182, 32, 16, 59, GuiBar.ProgressDirection.UP);
		tempBar = new GuiBar(
				48, 7, 176, 31, 6, 61, GuiBar.ProgressDirection.UP);
		progressBar = new GuiBar(
				97, 29, 176, 14, 24, 17, GuiBar.ProgressDirection.RIGHT);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY)
	{
		GlStateManager.color(1, 1, 1);
		mc.getTextureManager().bindTexture(new ResourceLocation(
				FactoryAutomation.MODID,
				"textures/gui/container/steelmaking_furnace.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		flameBar.Draw(this, te.GetBurnPercent());
		airTankBar.Draw(this, 1);
		fuelTankBar.Draw(this, 1);
		tempBar.Draw(this, te.GetTempPercent());
		progressBar.Draw(this, te.GetSmeltPercent());

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(mc.fontRenderer, "Steelmaking Furnace", 112, 6,
						   180 + 100 * 256 + 100 * 256 * 256);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(),
								100, this.ySize - 96 + 2, 4210752);

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		super.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(48, 7, 6, 61, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format("gui.misc.temperature") + ": " + String
					.format("%1$.1f\u00b0C", te.GetTemp()));
			drawHoveringText(text, mouseX, mouseY);
		}
	}
}
