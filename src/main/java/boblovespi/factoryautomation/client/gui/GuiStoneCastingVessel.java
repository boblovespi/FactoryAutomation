package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.ContainerStoneCastingVessel;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.network.StoneCastingVesselMoldPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * Created by Willi on 12/30/2018.
 */
public class GuiStoneCastingVessel extends GuiContainer
{
	private ResourceLocation loc = new ResourceLocation(
			FactoryAutomation.MODID, "textures/gui/container/stone_casting_vessel.png");
	private GuiButtonImage ingot;
	private GuiButtonImage nugget;
	private GuiButtonImage rod;
	private GuiButtonImage sheet;
	private GuiButtonImage coin;
	private GuiButtonImage gear;
	private IInventory playerInv;
	private TileEntity te;

	public GuiStoneCastingVessel(IInventory playerInv, TileEntity te)
	{
		super(new ContainerStoneCastingVessel(playerInv, te));
		this.playerInv = playerInv;
		this.te = te;
		xSize = 176;
		ySize = 180;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		ingot = new GuiButtonImage(0, guiLeft + 33, guiTop + 20, 18, 18, 0, 185, 19, loc);
		buttonList.add(ingot);
		nugget = new GuiButtonImage(1, guiLeft + 33, guiTop + 39, 18, 18, 38, 185, 19, loc);
		buttonList.add(nugget);
		rod = new GuiButtonImage(4, guiLeft + 33, guiTop + 58, 18, 18, 76, 185, 19, loc);
		buttonList.add(rod);
		sheet = new GuiButtonImage(2, guiLeft + 52, guiTop + 20, 18, 18, 19, 185, 19, loc);
		buttonList.add(sheet);
		coin = new GuiButtonImage(3, guiLeft + 52, guiTop + 39, 18, 18, 57, 185, 19, loc);
		buttonList.add(coin);
		gear = new GuiButtonImage(5, guiLeft + 52, guiTop + 58, 18, 18, 95, 185, 19, loc);
		buttonList.add(gear);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1, 1, 1);
		mc.getTextureManager().bindTexture(loc);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(mc.fontRenderer, "Stone Casting Vessel", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 100, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(te.getPos(), (byte) button.id));
	}
}
