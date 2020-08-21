package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRCampfire extends TileEntityRenderer<TECampfire>
{
	public TESRCampfire(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TECampfire te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			matrix.push();
			{
				matrix.translate(0.5, 0.4, 0.5);
				matrix.scale(0.4f, 0.4f, 0.4f);
				matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
				Minecraft.getInstance().getItemRenderer()
						 .renderItem(item, ItemCameraTransforms.TransformType.NONE, combinedLight, combinedOverlay,
								 matrix, buffer);
			}
			matrix.pop();
		}
	}
}
