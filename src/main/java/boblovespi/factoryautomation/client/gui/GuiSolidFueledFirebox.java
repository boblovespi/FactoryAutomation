package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.component.GuiBar;
import boblovespi.factoryautomation.common.container.ContainerSolidFueledFirebox;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Solid-Fueled Firebox", 56, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, inventory.getDisplayName(), 100, this.imageHeight - 96 + 2, 4210752);
	}

	@Override
	protected void renderTooltip(MatrixStack matrix, int mouseX, int mouseY)
	{
		super.renderTooltip(matrix, mouseX, mouseY);
		if (isHovering(103, 9, 6, 61, mouseX, mouseY))
		{
			ITextComponent text = new StringTextComponent(I18n.get("gui.misc.temperature") + ": " + String
																											.format("%1$.1f\u00b0C", menu.GetBar(0) / 10f));
			renderTooltip(matrix, text, mouseX, mouseY);
		}
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager._blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bind(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/solid_fueled_firebox.png"));
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		flameBar.Draw(this, matrix, menu.GetBar(1) / 100f);
		tempBar.Draw(this, matrix, menu.GetBar(2) / 100f);

		// Log.LogInfo("tileentity nbt data", te.getTileData().toString());
	}
}
