package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerSteelmakingFurnace;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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

		this.imageWidth = 176;
		this.imageHeight = 180;

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
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager._blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bind(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/steelmaking_furnace.png"));
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		flameBar.Draw(this, matrix, menu.GetBar(0) / 100f);
		airTankBar.Draw(this, matrix, 1);
		fuelTankBar.Draw(this, matrix, 1);
		tempBar.Draw(this, matrix, menu.GetBar(1) / 100f);
		progressBar.Draw(this, matrix, menu.GetBar(2) / 100f);

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}

	@Override
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Steelmaking Furnace", 112, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, inventory.getDisplayName(), 100, this.imageHeight - 96 + 2, 4210752);

	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}

	@Override
	protected void renderTooltip(MatrixStack matrix, int mouseX, int mouseY)
	{
		super.renderTooltip(matrix, mouseX, mouseY);
		if (isHovering(48, 7, 6, 61, mouseX, mouseY))
		{
			ITextComponent text = new StringTextComponent(I18n.get("gui.misc.temperature") + ": " + String.format("%1$.1f\u00b0C", menu.GetBar(3) / 10f));
			renderTooltip(matrix, text, mouseX, mouseY);
		}
	}
}
