package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Willi on 11/12/2017.
 */
public class GuiBlastFurnace extends ContainerScreen<ContainerBlastFurnace>
{
	private TEBlastFurnaceController te;
	private PlayerInventory playerInv;

	public GuiBlastFurnace(PlayerInventory playerInv, TileEntity te)
	{
		super(new ContainerBlastFurnace(playerInv, te), playerInv, null);
		this.te = (TEBlastFurnaceController) te;
		this.playerInv = playerInv;

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.blendColor(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(
				new ResourceLocation(FactoryAutomation.MODID, "textures/gui/container/blast_furnace.png"));
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		int k = (int) (te.getRemainingBurnTime() / te.getLastBurnTime() * 14);
		int l = (int) (te.getCurrentSmeltTime() / te.getTotalSmeltTime() * 24);
		if (te.isBurningFuel())
		{

			this.blit(guiLeft + 56, guiTop + 50 - k, 176, 14 - k, 14, k);
		}
		if (te.isSmeltingItem())
		{

			this.blit(guiLeft + 79, guiTop + 34, 176, 14, 24 - l, 16);
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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		drawCenteredString(minecraft.fontRenderer, "Blast Furnace", 84, 6, 180 + 100 * 256 + 100 * 256 * 256);
		font.drawString(playerInv.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);

	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
}
