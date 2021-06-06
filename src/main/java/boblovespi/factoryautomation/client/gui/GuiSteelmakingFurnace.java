package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerSteelmakingFurnace;
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
 * Created by Willi on 12/24/2017.
 */
public class GuiSteelmakingFurnace extends ContainerScreen<ContainerSteelmakingFurnace>
{
	private GuiBar flameBar;
	private GuiBar airTankBar;
	private GuiBar fuelTankBar;
	private GuiBar tempBar;
	private GuiBar progressBar;

	public GuiSteelmakingFurnace(ContainerSteelmakingFurnace container, PlayerInventory playerInv,
			ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.steelmaking_furnace"));

		this.xSize = 176;
		this.ySize = 180;

		flameBar = new GuiBar(59, 57, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
		airTankBar = new GuiBar(8, 8, 182, 32, 16, 59, GuiBar.ProgressDirection.UP);
		fuelTankBar = new GuiBar(28, 8, 182, 32, 16, 59, GuiBar.ProgressDirection.UP);
		tempBar = new GuiBar(48, 7, 176, 31, 6, 61, GuiBar.ProgressDirection.UP);
		progressBar = new GuiBar(97, 29, 176, 14, 24, 17, GuiBar.ProgressDirection.RIGHT);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/steelmaking_furnace.png"));
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		flameBar.draw(this, container.GetBar(0) / 100f);
		airTankBar.draw(this, 1);
		fuelTankBar.draw(this, 1);
		tempBar.draw(this, container.GetBar(1) / 100f);
		progressBar.draw(this, container.GetBar(2) / 100f);

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(minecraft.fontRenderer, "Steelmaking Furnace", 112, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 100, this.ySize - 96 + 2, 4210752);

	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		super.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(48, 7, 6, 61, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format("gui.misc.temperature") + ": " + String.format("%1$.1f\u00b0C", container.GetBar(3) / 10f));
			renderTooltip(text, mouseX, mouseY);
		}
	}
}
