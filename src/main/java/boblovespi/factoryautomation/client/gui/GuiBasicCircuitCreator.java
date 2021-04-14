package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiGridElement;
import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.network.BasicCircuitCreatorSyncPacket;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by Willi on 5/28/2018.
 */
public class GuiBasicCircuitCreator extends ContainerScreen<ContainerBasicCircuitCreator>
{
	private TEBasicCircuitCreator te;

	private ResourceLocation loc = new ResourceLocation(
			FactoryAutomation.MODID, "textures/gui/container/chip_creator.png");

	private int mode = -1;
	private GuiGridElement[][] elements;
	private ImageButton solderMode;
	private ImageButton wireMode;
	private ImageButton addThirdMode;
	private ImageButton craft;

	public GuiBasicCircuitCreator(ContainerBasicCircuitCreator container, PlayerInventory playerInv, ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.basic_circuit_creator"));
		this.te = (TEBasicCircuitCreator) playerInv.player.world.getTileEntity(container.GetPos());

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
	public void init()
	{
		super.init();
		solderMode = new ImageButton(guiLeft + 106, guiTop + 12, 20, 18, 1, 167, 19, loc, unused -> mode = 0);
		addButton(solderMode);
		wireMode = new ImageButton(guiLeft + 106, guiTop + 32, 20, 18, 22, 167, 19, loc, unused -> mode = 1);
		addButton(wireMode);
		addThirdMode = new ImageButton(guiLeft + 106, guiTop + 52, 20, 18, 43, 167, 19, loc, unused -> mode = 2);
		addButton(addThirdMode);
		craft = new ImageButton(
				guiLeft + 106, guiTop + 72, 20, 18, 64, 167, 19, loc, unused -> PacketHandler.INSTANCE
				.sendToServer(new BasicCircuitCreatorSyncPacket(te.getPos(), (byte) 4, (byte) 0, (byte) 0)));
		addButton(craft);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(loc);
		blit(matrix, guiLeft, guiTop, 0, 0, xSize, ySize);

		// drawTexturedModalRect(guiLeft + 106, guiTop, 1, 167, 20, 18);

		solderMode.renderButton(matrix, mouseX, mouseY, partialTicks);

		TEBasicCircuitCreator.Layout.Element[][] components = te.GetComponents();

		for (int x = 7; x >= 0; x--)
		{
			for (int y = 7; y >= 0; y--)
			{
				elements[x][y].SetElement(components[y][x]);
				elements[x][y].Draw(matrix, this);
			}
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.fontRenderer, "Circuit Creator", 104, 6, 180 + 100 * 256 + 100 * 256 * 256);
		// fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(matrix, mouseX, mouseY);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		System.out.println("mousePos: ( " + mouseX + " , " + mouseY + " )\nmouseButton: " + mouseButton);

		if (container.HasFrame())
		{
			int x = (int) ((mouseX - 36 - guiLeft) / 8);
			int y = (int) ((mouseY - 14 - guiTop) / 8);

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
		return true;
	}

	/**
	 * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
	 * lastButtonClicked & timeSinceMouseClick.
	 */
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int clickedMouseButton, double something,
			double somethingElse)
	{
		super.mouseDragged(mouseX, mouseY, clickedMouseButton, something, somethingElse);

		if (container.HasFrame())
		{
			int x = (int) ((mouseX - 36 - guiLeft) / 8);
			int y = (int) ((mouseY - 14 - guiTop) / 8);

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
		return true;
	}
}
