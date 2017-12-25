package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

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
		super(new ContainerBlastFurnace(playerInv, te));
		this.te = (TESteelmakingFurnace) te;
		this.playerInv = playerInv;

		this.xSize = 176;
		this.ySize = 180;
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

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(mc.fontRenderer, "Steelmaking Furnace", 84, 6,
						   180 + 100 * 256 + 100 * 256 * 256);
		fontRenderer
				.drawString(playerInv.getDisplayName().getUnformattedText(), 8,
							this.ySize - 96 + 2, 4210752);

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
}
