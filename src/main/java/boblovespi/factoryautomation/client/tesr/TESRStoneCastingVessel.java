package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TEStoneCastingVessel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRStoneCastingVessel extends TileEntitySpecialRenderer<TEStoneCastingVessel>
{
	@Override
	public void render(TEStoneCastingVessel te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(x + 0.5, y + 6.501 / 16d, z + 0.5);
				GlStateManager.scale(1, 1, 1);
				GlStateManager.rotate(-90, 1, 0, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
			}
			GlStateManager.popMatrix();
		}
	}
}
