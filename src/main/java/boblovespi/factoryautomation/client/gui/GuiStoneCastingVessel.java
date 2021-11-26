package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiMultiImage;
import boblovespi.factoryautomation.common.container.ContainerStoneCastingVessel;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.network.StoneCastingVesselMoldPacket;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
	private GuiMultiImage image;

	public GuiStoneCastingVessel(ContainerStoneCastingVessel container, PlayerInventory playerInv, ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.stone_casting_vessel"));
		imageWidth = 176;
		imageHeight = 180;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void init()
	{
		super.init();
		ingot = new ImageButton(leftPos + 33, topPos + 20, 18, 18, 0, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(menu.GetPos(), (byte) 0)));
		addButton(ingot);
		nugget = new ImageButton(leftPos + 33, topPos + 39, 18, 18, 38, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(menu.GetPos(), (byte) 1)));
		addButton(nugget);
		rod = new ImageButton(
				leftPos + 33, topPos + 58, 18, 18, 76, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(menu.GetPos(), (byte) 4)));
		addButton(rod);
		sheet = new ImageButton(leftPos + 52, topPos + 20, 18, 18, 19, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(menu.GetPos(), (byte) 2)));
		addButton(sheet);
		coin = new ImageButton(leftPos + 52, topPos + 39, 18, 18, 57, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(menu.GetPos(), (byte) 3)));
		addButton(coin);
		gear = new ImageButton(leftPos + 52, topPos + 58, 18, 18, 95, 185, 19, loc,
				unused -> PacketHandler.INSTANCE.sendToServer(new StoneCastingVesselMoldPacket(menu.GetPos(), (byte) 5)));
		addButton(gear);
		image = new GuiMultiImage(85, 16, 64, 64, 0, 0, 16, 16, Lists.newArrayList(
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/resources/green_sand.png"),
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/processing/casting_sand_ingot_pattern.png"),
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/processing/casting_sand_nugget_pattern.png"),
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/processing/casting_sand_sheet_pattern.png"),
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/processing/casting_sand_rod_pattern.png"),
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/processing/casting_sand_gear_pattern.png"),
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/processing/casting_sand_coin_pattern.png")));
	}

	/**
	 * Draws the background layer of this menu (behind the items).
	 */
	@Override
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager._blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bind(loc);
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		image.SetTexture(menu.GetForm());
		image.Draw(this, matrix);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY)
	{
		font.draw(matrix, "Stone Casting Vessel", 8, 4, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, inventory.getDisplayName(), 8, this.imageHeight - 96 + 2, 4210752);
		// super.renderLabels(matrix, mouseX, mouseY);
	}
}
