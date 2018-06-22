package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiGridElement;
import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.network.BasicCircuitCreatorSyncPacket;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

import java.io.IOException;

/**
 * Created by Willi on 5/28/2018.
 */
public class GuiBasicCircuitCreator extends GuiContainer
{
	private TEBasicCircuitCreator te;
	private IItemHandler inv;
	private ContainerBasicCircuitCreator container;
	private IInventory playerInv;

	private ResourceLocation loc = new ResourceLocation(
			FactoryAutomation.MODID, "textures/gui/container/chip_creator.png");

	private int mode = -1;
	private GuiGridElement[][] elements;
	private GuiButtonImage solderMode;
	private GuiButtonImage wireMode;
	private GuiButtonImage addThirdMode;
	private GuiButtonImage craft;

	public GuiBasicCircuitCreator(IInventory playerInv, TileEntity te)
	{
		super(new ContainerBasicCircuitCreator(playerInv, te));
		this.playerInv = playerInv;
		this.te = (TEBasicCircuitCreator) te;
		container = (ContainerBasicCircuitCreator) inventorySlots;

		xSize = 206;
		ySize = 166;

		elements = new GuiGridElement[8][8];

		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				elements[x][y] = new GuiGridElement(36 + 8 * x, 14 + 8 * y, 175, 191, 8, 8,
						TEBasicCircuitCreator.Layout.Element.EMPTY);
			}
		}

	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		solderMode = new GuiButtonImage(0, guiLeft + 106, guiTop + 12, 20, 18, 1, 167, 19, loc);
		addButton(solderMode);
		wireMode = new GuiButtonImage(1, guiLeft + 106, guiTop + 32, 20, 18, 22, 167, 19, loc);
		addButton(wireMode);
		addThirdMode = new GuiButtonImage(2, guiLeft + 106, guiTop + 52, 20, 18, 43, 167, 19, loc);
		addButton(addThirdMode);
		craft = new GuiButtonImage(3, guiLeft + 106, guiTop + 72, 20, 18, 64, 167, 19, loc);
		addButton(craft);
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

		// drawTexturedModalRect(guiLeft + 106, guiTop, 1, 167, 20, 18);

		solderMode.drawButton(mc, mouseX, mouseY, partialTicks);

		TEBasicCircuitCreator.Layout.Element[][] components = te.GetComponents();

		for (int x = 7; x >= 0; x--)
		{
			for (int y = 7; y >= 0; y--)
			{
				elements[x][y].SetElement(components[y][x]);
				elements[x][y].Draw(this);
			}
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(mc.fontRenderer, "Circuit Creator", 104, 6, 180 + 100 * 256 + 100 * 256 * 256);
		// fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.id == solderMode.id)
		{
			mode = solderMode.id;
		} else if (button.id == wireMode.id)
		{
			mode = wireMode.id;
		} else if (button.id == addThirdMode.id)
		{
			mode = addThirdMode.id;
		} else if (button.id == craft.id)
		{
			PacketHandler.INSTANCE
					.sendToServer(new BasicCircuitCreatorSyncPacket(te.getPos(), (byte) 4, (byte) 0, (byte) 0));
		} else if (button.id == -1)
		{

		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		System.out.println("mousePos: ( " + mouseX + " , " + mouseY + " )\nmouseButton: " + mouseButton);

		if (container.HasFrame())
		{
			int x = (mouseX - 36 - guiLeft) / 8;
			int y = (mouseY - 14 - guiTop) / 8;

			if (x >= 0 && x < 8 && y >= 0 && y < 8)
			{
				System.out.println(
						"\n\n --- sending packet! --- \npacket info:\n   pos: " + te.getPos().toString() + "\n   mode: "
								+ mode + "\n   x, y: ( " + x + " , " + y + " )\n");
				if (mouseButton == 0)
					PacketHandler.INSTANCE.sendToServer(
							new BasicCircuitCreatorSyncPacket(te.getPos(), (byte) mode, (byte) x, (byte) y));
				else if (mouseButton == 1)
					PacketHandler.INSTANCE
							.sendToServer(new BasicCircuitCreatorSyncPacket(te.getPos(), (byte) 3, (byte) x, (byte) y));
			}
		}
	}

	/**
	 * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
	 * lastButtonClicked & timeSinceMouseClick.
	 */
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

		if (container.HasFrame())
		{
			int x = (mouseX - 36 - guiLeft) / 8;
			int y = (mouseY - 14 - guiTop) / 8;

			if (x >= 0 && x < 8 && y >= 0 && y < 8)
			{
				System.out.println(
						"\n\n --- sending packet! --- \npacket info:\n   pos: " + te.getPos().toString() + "\n   mode: "
								+ mode + "\n   x, y: ( " + x + " , " + y + " )\n");
				if (clickedMouseButton == 0)
					PacketHandler.INSTANCE.sendToServer(
							new BasicCircuitCreatorSyncPacket(te.getPos(), (byte) mode, (byte) x, (byte) y));
				else if (clickedMouseButton == 1)
					PacketHandler.INSTANCE
							.sendToServer(new BasicCircuitCreatorSyncPacket(te.getPos(), (byte) 3, (byte) x, (byte) y));
			}
		}
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
	}
}
