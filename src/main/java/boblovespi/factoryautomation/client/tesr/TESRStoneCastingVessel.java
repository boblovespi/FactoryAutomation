package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRStoneCastingVessel extends BlockEntityRenderer<TEStoneCastingVessel>
{
	private BakedModel modelCache = null;
	private ItemStack itemCache = ItemStack.EMPTY;

	public TESRStoneCastingVessel(BlockEntityRenderDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEStoneCastingVessel te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			if (!item.sameItem(itemCache)) // update the cache if outdated
			{
				modelCache = Minecraft.getInstance().getItemRenderer()
									  .getModel(item.copy().split(1), null, null);
				itemCache = item;
			}
			matrix.pushPose();
			{
				// matrix.translate(0.5, 0.5, 0.5);
				matrix.translate(0, -1.4 / 16d, 1);
				matrix.scale(0.99f, 1, 0.99f);
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(-90, 1, 0, 0));
				float v = te.GetTemp();
				RenderSystem.color3f(GetRed(v), GetGreen(v), GetBlue(v));
				// TESRUtils.RenderBakedModel(modelCache, DefaultVertexFormats.ITEM,
				// 		TESRUtils.RGBAToHex(GetRed(v), GetGreen(v), GetBlue(v), 1f));
				// Minecraft.getInstance().getItemRenderer()
				//		 .renderItem(item, ItemCameraTransforms.TransformType.NONE, combinedLight, combinedOverlay,
				//				 matrix, buffer);
				TESRUtils.RenderItemWithColor(item, ItemTransforms.TransformType.NONE, false, matrix, buffer,
						combinedLight, combinedOverlay, modelCache,
						TESRUtils.RGBAToHex(GetRed(v), GetGreen(v), GetBlue(v), 1));
			}
			matrix.popPose();
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
