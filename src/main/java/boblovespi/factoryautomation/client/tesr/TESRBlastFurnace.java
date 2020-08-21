package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

/**
 * Created by Willi on 11/21/2017.
 */
public class TESRBlastFurnace extends TileEntityRenderer<TEBlastFurnaceController>
{
	public TESRBlastFurnace(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(TEBlastFurnaceController tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{

	}
}
