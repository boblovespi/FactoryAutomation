package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerBrickFoundry;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Willi on 4/11/2019.
 */
public class GuiBrickFoundry extends ContainerScreen<ContainerBrickFoundry>
{
	private final GuiBar flameBar;
	private final GuiBar tempBar;
	private final GuiBar progressBar;
	private final GuiBar bellowsBar;

	public GuiBrickFoundry(ContainerBrickFoundry container, PlayerInventory playerInv, ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.brick_foundry"));
		tempBar = new GuiBar(53, 16, 176, 17, 6, 61, GuiBar.ProgressDirection.UP);
		flameBar = new GuiBar(67, 40, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
		progressBar = new GuiBar(84, 21, 194, 2, 22, 10, GuiBar.ProgressDirection.RIGHT);
		bellowsBar = new GuiBar(86, 61, 190, 14, 16, 14, GuiBar.ProgressDirection.LEFT);
		ySize = 180;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(minecraft.fontRenderer, "Brick Foundry", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 100, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		super.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(53, 16, 6, 61, mouseX, mouseY))
		{
			List<String> text = Collections.singletonList(I18n.format("gui.misc.temperature") + ": " + String
					.format("%1$.1f\u00b0C", container.GetBar(6) / 10f));
			renderTooltip(text, mouseX, mouseY);
		}
		if (isPointInRegion(107, 17, 16, 59, mouseX, mouseY))
		{
			List<String> text = Collections.singletonList(I18n.format(container.GetMetalName()) + ": " + container.GetBar(7));
			renderTooltip(text, mouseX, mouseY);
		}
		if (isPointInRegion(87, 61, 16, 16, mouseX, mouseY))
		{
			List<String> text = Collections.singletonList(
					I18n.format("gui.misc.efficiency") + ": " + String.format("%1$.0f", container.GetBar(8) / 100f)
							+ "%");
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
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/brick_foundry.png"));
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		flameBar.Draw(this, container.GetBar(0) / 100f);
		tempBar.Draw(this, container.GetBar(1) / 100f);
		progressBar.Draw(this, container.GetBar(2) / 100f);
		bellowsBar.Draw(this, container.GetBar(3) / 100f);
		fill(guiLeft + 107, guiTop + (int) (76 - 59 * container.GetBar(4) / 100f), guiLeft + 123, guiTop + 76,
				container.GetBar(5));

	}
}