package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerSteelmakingFurnace;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/24/2017.
 */
public class GuiSteelmakingFurnace extends AbstractContainerScreen<ContainerSteelmakingFurnace>
{
	private GuiBar flameBar;
	private GuiBar airTankBar;
	private GuiBar fuelTankBar;
	private GuiBar tempBar;
	private GuiBar progressBar;

	public GuiSteelmakingFurnace(ContainerSteelmakingFurnace container, Inventory playerInv,
								 Component unused)
	{
		super(container, playerInv, new TranslatableComponent("gui.steelmaking_furnace"));

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
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY)
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
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Steelmaking Furnace", 112, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, inventory.getDisplayName(), 100, this.imageHeight - 96 + 2, 4210752);

	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}

	@Override
	protected void renderTooltip(PoseStack matrix, int mouseX, int mouseY)
	{
		super.renderTooltip(matrix, mouseX, mouseY);
		if (isHovering(48, 7, 6, 61, mouseX, mouseY))
		{
			Component text = new TextComponent(I18n.get("gui.misc.temperature") + ": " + String.format("%1$.1f\u00b0C", menu.GetBar(3) / 10f));
			renderTooltip(matrix, text, mouseX, mouseY);
		}
	}
}
