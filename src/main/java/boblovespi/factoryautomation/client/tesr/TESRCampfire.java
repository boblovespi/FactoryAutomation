package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRCampfire implements BlockEntityRenderer<TECampfire>
{
	private BlockEntityRendererProvider.Context context;

	public TESRCampfire(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
	}

	@Override
	public void render(TECampfire te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			matrix.pushPose();
			{
				matrix.translate(0.5, 0.4, 0.5);
				matrix.scale(0.4f, 0.4f, 0.4f);
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
				Minecraft.getInstance().getItemRenderer()
						 .renderStatic(item, ItemTransforms.TransformType.NONE, combinedLight, combinedOverlay,
								 matrix, buffer, te.getBlockPos().hashCode());
			}
			matrix.popPose();
		}
	}
}
