package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
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

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/blast_furnace.png"));
		blit(matrix, guiLeft, guiTop, 0, 0, xSize, ySize);

		int k = container.GetProgressBars().get(0);
		int l = container.GetProgressBars().get(1);
		if (container.GetProgressBars().get(2) > 0)
		{
			this.blit(matrix, guiLeft + 56, guiTop + 50 - k, 176, 14 - k, 14, k);
		}
		if (container.GetProgressBars().get(3) > 0)
		{
			this.blit(matrix, guiLeft + 79, guiTop + 34, 176, 14, 24 - l, 16);
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
	protected void drawGuiContainerForegroundLayer(MatrixStack matrix, int mouseX, int mouseY)
	{
		drawCenteredString(matrix, minecraft.fontRenderer, "Blast Furnace", 84, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.func_243246_a(matrix, playerInventory.getDisplayName(), 8, this.ySize - 96 + 2, 4210752);

	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(matrix, mouseX, mouseY);
	}
}
