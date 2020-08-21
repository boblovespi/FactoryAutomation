package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

/**
 * Created by Willi on 8/6/2019.
 */
public class TESRWaterwheel extends TileEntityRenderer<TEWaterwheel>
{

	public TESRWaterwheel(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEWaterwheel te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{

	}
}
