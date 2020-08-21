package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.mechanical.TEJawCrusher;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

/**
 * Created by Willi on 3/5/2018.
 */
public class TESRJawCrusher extends TileEntityRenderer<TEJawCrusher>
{

	public TESRJawCrusher(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEJawCrusher te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{

	}
}
