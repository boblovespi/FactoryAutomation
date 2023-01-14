package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TEBrickMaker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;

public class TESRBrickMaker implements BlockEntityRenderer<TEBrickMaker>
{
	private final BlockEntityRendererProvider.Context context;
	private BakedModel left;
	private BakedModel right;

	public TESRBrickMaker(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
	}

	@Override
	public void render(TEBrickMaker te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
					   int combinedLight, int combinedOverlay)
	{

	}
}
