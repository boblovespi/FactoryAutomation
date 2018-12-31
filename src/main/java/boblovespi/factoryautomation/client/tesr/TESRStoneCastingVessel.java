package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TEStoneCastingVessel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRStoneCastingVessel extends TileEntitySpecialRenderer<TEStoneCastingVessel>
{
	private IBakedModel modelCache = null;
	private ItemStack itemCache = ItemStack.EMPTY;

	@Override
	public void render(TEStoneCastingVessel te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			if (!item.isItemEqual(itemCache)) // update the cache if outdated
			{
				modelCache = Minecraft.getMinecraft().getRenderItem()
									  .getItemModelWithOverrides(item.copy().splitStack(1), null, null);
				itemCache = item;
			}
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(x, y - 1.4 / 16d, z + 1);
				GlStateManager.scale(0.99, 1, 0.99);
				GlStateManager.rotate(-90, 1, 0, 0);
				float v = te.GetTemp();
				// TESRUtils.RenderBakedModel(modelCache, DefaultVertexFormats.ITEM,
				// 		TESRUtils.RGBAToHex(GetRed(v), GetGreen(v), GetBlue(v), 1f));
				TESRUtils.RenderItemWithColor(item, ItemCameraTransforms.TransformType.NONE, GetRed(v), GetGreen(v),
						GetBlue(v), 1);
			}
			GlStateManager.popMatrix();
		}
	}

	public float GetRed(float temp)
	{
		return 1f;
	}

	public float GetGreen(float temp)
	{
		if (temp > 900f)
			return (0.5f / 900f) * temp;
		else if (temp > 450f)
			return (0.4f / 450f) * temp - 0.3f;
		else
			return (-0.9f / 450f) * temp + 1f;
	}

	public float GetBlue(float temp)
	{
		if (temp > 1350f)
			return (0.8f / 450) * temp - 2.4f;
		else if (temp > 450f)
			return 0f;
		else
			return (-1f / 450f) * temp + 1f;
	}
}
