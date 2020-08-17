package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.ContainerBrickFoundry;
import boblovespi.factoryautomation.common.container.ContainerStoneCastingVessel;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.network.StoneCastingVesselMoldPacket;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.IOException;

/**
 * Created by Willi on 12/30/2018.
 */
public class GuiStoneCastingVessel extends ContainerScreen<ContainerStoneCastingVessel>
{
	private ResourceLocation loc = new ResourceLocation(
			FactoryAutomation.MODID, "textures/gui/container/stone_casting_vessel.png");
	private ImageButton ingot;
	private ImageButton nugget;
	private ImageButton rod;
	private ImageButton sheet;
	private ImageButton coin;
	private ImageButton gear;

	public GuiStoneCastingVessel(ContainerStoneCastingVessel container, PlayerInventory playerInv, ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.stone_casting_vessel"));
		xSize = 176;
		ySize = 180;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void init()
	{
		super.init();
		ingot = new ImageButton(guiLeft + 33, guiTop + 20, 18, 18, 0, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(container.GetPos(), (byte) 0)));
		buttons.add(ingot);
		nugget = new ImageButton(guiLeft + 33, guiTop + 39, 18, 18, 38, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(container.GetPos(), (byte) 1)));
		buttons.add(nugget);
		rod = new ImageButton(
				guiLeft + 33, guiTop + 58, 18, 18, 76, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(container.GetPos(), (byte) 4)));
		buttons.add(rod);
		sheet = new ImageButton(guiLeft + 52, guiTop + 20, 18, 18, 19, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(container.GetPos(), (byte) 2)));
		buttons.add(sheet);
		coin = new ImageButton(guiLeft + 52, guiTop + 39, 18, 18, 57, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(container.GetPos(), (byte) 3)));
		buttons.add(coin);
		gear = new ImageButton(guiLeft + 52, guiTop + 58, 18, 18, 95, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(container.GetPos(), (byte) 5)));
		buttons.add(gear);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(loc);
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(minecraft.fontRenderer, "Stone Casting Vessel", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 100, this.ySize - 96 + 2, 4210752);
	}
}
