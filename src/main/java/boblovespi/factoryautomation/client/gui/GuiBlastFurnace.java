package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by Willi on 11/12/2017.
 */
public class GuiBlastFurnace extends ContainerScreen<ContainerBlastFurnace>
{
	public GuiBlastFurnace(ContainerBlastFurnace container, PlayerInventory playerInv, ITextComponent unused)
	{
		super(container, playerInv, new TranslationTextComponent("gui.blast_furnace"));

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager._blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bind(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/blast_furnace.png"));
		blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		int k = menu.GetProgressBars().get(0);
		int l = menu.GetProgressBars().get(1);
		if (menu.GetProgressBars().get(2) > 0)
		{
			this.blit(matrix, leftPos + 56, topPos + 50 - k, 176, 14 - k, 14, k);
		}
		if (menu.GetProgressBars().get(3) > 0)
		{
			this.blit(matrix, leftPos + 79, topPos + 34, 176, 14, 24 - l, 16);
		}

		//		Debug.DebugLog()
		//				.debug("\n\n\tD E B U G\n-------------------------------------------------------\n");
		//
		//		Debug.DebugLog().debug("Is it burning?: " + te.isBurningFuel());
		//
		//		Debug.DebugLog().debug("smelt progress: " + (
		//				te.getCurrentSmeltTime() / te.getTotalSmeltTime() * 24));
		//		Debug.DebugLog().debug("burn time: " + (
		//				te.getRemainingBurnTime() / te.getLastBurnTime() * 14) + "\n");
		//		Debug.DebugLog().debug("total smelt time: " + (te.getTotalSmeltTime()));
		//		Debug.DebugLog()
		//				.debug("current smelt time: " + (te.getCurrentSmeltTime())
		//						+ "\n");
		//		Debug.DebugLog().debug("last burn time: " + (te.getLastBurnTime()));
		//		Debug.DebugLog()
		//				.debug("remaining burn time: " + (te.getRemainingBurnTime()));

	}

	@Override
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.font, "Blast Furnace", 84, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.draw(matrix, inventory.getDisplayName(), 8, this.imageHeight - 96 + 2, 4210752);

	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, mouseX, mouseY);
	}
}
