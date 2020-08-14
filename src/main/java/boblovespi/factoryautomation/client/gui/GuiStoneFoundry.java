package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerBrickFoundry;
import boblovespi.factoryautomation.common.container.ContainerStoneFoundry;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 10/28/2018.
 */
public class GuiStoneFoundry extends ContainerScreen<ContainerStoneFoundry>
{
	private TEStoneCrucible te;
	private GuiBar flameBar;
	private GuiBar tempBar;
	private GuiBar progressBar;

	public GuiStoneFoundry(PlayerInventory playerInv, TileEntity te)
	{
		super(new ContainerStoneFoundry(playerInv, te), playerInv, new TranslationTextComponent("gui.stone_foundry"));
		this.te = (TEStoneCrucible) te;
		tempBar = new GuiBar(53, 16, 176, 16, 6, 61, GuiBar.ProgressDirection.UP);
		flameBar = new GuiBar(67, 40, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
		progressBar = new GuiBar(84, 21, 194, 2, 22, 10, GuiBar.ProgressDirection.RIGHT);
		ySize = 180;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(minecraft.fontRenderer, "Stone Foundry", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 100, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		super.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(53, 16, 6, 61, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format("gui.misc.temperature") + ": " + String.format("%1$.1f\u00b0C", te.GetTemp()));
			renderTooltip(text, mouseX, mouseY);
		}
		if (isPointInRegion(107, 17, 16, 59, mouseX, mouseY))
		{
			List<String> text = new ArrayList<>(1);
			text.add(I18n.format(te.GetMetalName()) + ": " + te.GetAmountMetal());
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
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/stone_foundry.png"));
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		flameBar.Draw(this, te.GetBurnPercent());
		tempBar.Draw(this, te.GetTempPercent());
		progressBar.Draw(this, te.GetMeltPercent());
		fill(guiLeft + 107, guiTop + (int) (76 - 59 * te.GetCapacityPercent()), guiLeft + 123, guiTop + 76,
				te.GetColor());

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}
}
