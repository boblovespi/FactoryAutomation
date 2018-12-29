package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRCampfire extends TileEntitySpecialRenderer<TECampfire>
{
	@Override
	public void render(TECampfire te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(x + 0.5, y + 0.4, z + 0.5);
				GlStateManager.scale(0.4, 0.4, 0.4);
				GlStateManager.rotate(90, 1, 0, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
			}
			GlStateManager.popMatrix();
		}
	}
}
