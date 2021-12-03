package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

/**
 * Created by Willi on 11/21/2017.
 */
public class TESRBlastFurnace extends BlockEntityRenderer<TEBlastFurnaceController>
{
	public TESRBlastFurnace(BlockEntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(TEBlastFurnaceController tileEntityIn, float partialTicks, PoseStack matrixStackIn,
			MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
	{

	}
}
