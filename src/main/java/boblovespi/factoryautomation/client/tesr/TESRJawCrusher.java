package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.mechanical.TEJawCrusher;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

/**
 * Created by Willi on 3/5/2018.
 */
public class TESRJawCrusher implements BlockEntityRenderer<TEJawCrusher>
{
	private BlockEntityRendererProvider.Context context;

	public TESRJawCrusher(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
	}

	@Override
	public void render(TEJawCrusher te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{

	}
}
