package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerSolidFueledFirebox;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 10/28/2018.
 */
public class GuiSolidFueledFirebox extends ContainerScreen<ContainerSolidFueledFirebox>
{
	private GuiBar flameBar;
	private GuiBar tempBar;

	public GuiSolidFueledFirebox(ContainerSolidFueledFirebox container, PlayerInventory playerInv,
			ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.solid_fueled_firebox"));
		tempBar = new GuiBar(103, 9, 176, 14, 6, 61, GuiBar.ProgressDirection.UP);
		flameBar = new GuiBar(80, 36, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(minecraft.fontRenderer, "Solid-Fueled Firebox", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 100, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		super.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(103, 9, 6, 61, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format("gui.misc.temperature") + ": " + String
					.format("%1$.1f\u00b0C", container.GetBar(0) / 10f));
			renderTooltip(text, mouseX, mouseY);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/solid_fueled_firebox.png"));
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		flameBar.draw(this, container.GetBar(1) / 100f);
		tempBar.draw(this, container.GetBar(2) / 100f);

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}
}
