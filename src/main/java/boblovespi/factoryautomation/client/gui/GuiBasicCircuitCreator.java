package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiGridElement;
import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.network.BasicCircuitCreatorSyncPacket;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Created by Willi on 5/28/2018.
 */
public class GuiBasicCircuitCreator extends AbstractContainerScreen<ContainerBasicCircuitCreator>
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

	public GuiBasicCircuitCreator(ContainerBasicCircuitCreator container, Inventory playerInv, Component unused)
	{
		super(container, playerInv, new TranslatableComponent("gui.basic_circuit_creator"));
		this.te = (TEBasicCircuitCreator) playerInv.player.level.getBlockEntity(container.GetPos());

		imageWidth = 206;
		imageHeight = 166;

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
		solderMode = new ImageButton(leftPos + 106, topPos + 12, 20, 18, 1, 167, 19, loc, unused -> mode = 0);
		addButton(solderMode);
		wireMode = new ImageButton(leftPos + 106, topPos + 32, 20, 18, 22, 167, 19, loc, unused -> mode = 1);
		addButton(wireMode);
		addThirdMode = new ImageButton(leftPos + 106, topPos + 52, 20, 18, 43, 167, 19, loc, unused -> mode = 2);
		addButton(addThirdMode);
		craft = new ImageButton(
				leftPos + 106, topPos + 72, 20, 18, 64, 167, 19, loc, unused -> PacketHandler.INSTANCE
				.sendToServer(new BasicCircuitCreatorSyncPacket(te.getBlockPos(), (byte) 4, (byte) 0, (byte) 0)));
		addButton(craft);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager._blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bind(loc);
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		// drawTexturedModalRect(leftPos + 106, topPos, 1, 167, 20, 18);

		solderMode.renderButton(matrix, mouseX, mouseY, partialTicks);

		TEBasicCircuitCreator.Layout.Element[][] components = te.GetComponents();

		for (int x = 7; x >= 0; x--)
		{
			for (int y = 7; y >= 0; y--)
			{
				elements[x][y].SetElement(components[y][x]);
				elements[x][y].Draw(this, matrix);
			}
		}

	}

	@Override
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY)
	{
		super.renderLabels(matrix, mouseX, mouseY);
		drawCenteredString(matrix, minecraft.font, "Circuit Creator", 104, 6, 180 + 100 * 256 + 100 * 256 * 256);
		// fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.imageHeight - 96 + 2, 4210752);
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		// renderLabels(matrix, mouseX, mouseY);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		System.out.println("mousePos: ( " + mouseX + " , " + mouseY + " )\nmouseButton: " + mouseButton);

		if (menu.HasFrame())
		{
			int x = (int) ((mouseX - 36 - leftPos) / 8);
			int y = (int) ((mouseY - 14 - topPos) / 8);

			if (x >= 0 && x < 8 && y >= 0 && y < 8)
			{
				System.out.println(
						"\n\n --- sending packet! --- \npacket info:\n   pos: " + te.getBlockPos().toString() + "\n   mode: "
								+ mode + "\n   x, y: ( " + x + " , " + y + " )\n");
				if (mouseButton == 0)
					PacketHandler.INSTANCE.sendToServer(
							new BasicCircuitCreatorSyncPacket(te.getBlockPos(), (byte) mode, (byte) x, (byte) y));
				else if (mouseButton == 1)
					PacketHandler.INSTANCE
							.sendToServer(new BasicCircuitCreatorSyncPacket(te.getBlockPos(), (byte) 3, (byte) x, (byte) y));
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

		if (menu.HasFrame())
		{
			int x = (int) ((mouseX - 36 - leftPos) / 8);
			int y = (int) ((mouseY - 14 - topPos) / 8);

			if (x >= 0 && x < 8 && y >= 0 && y < 8)
			{
				System.out.println(
						"\n\n --- sending packet! --- \npacket info:\n   pos: " + te.getBlockPos().toString() + "\n   mode: "
								+ mode + "\n   x, y: ( " + x + " , " + y + " )\n");
				if (clickedMouseButton == 0)
					PacketHandler.INSTANCE.sendToServer(
							new BasicCircuitCreatorSyncPacket(te.getBlockPos(), (byte) mode, (byte) x, (byte) y));
				else if (clickedMouseButton == 1)
					PacketHandler.INSTANCE
							.sendToServer(new BasicCircuitCreatorSyncPacket(te.getBlockPos(), (byte) 3, (byte) x, (byte) y));
			}
		}
		return true;
	}
}
