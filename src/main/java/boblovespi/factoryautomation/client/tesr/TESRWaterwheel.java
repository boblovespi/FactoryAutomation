package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

/**
 * Created by Willi on 8/6/2019.
 */
public class TESRWaterwheel extends BlockEntityRenderer<TEWaterwheel>
{

	public TESRWaterwheel(BlockEntityRenderDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEWaterwheel te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{

	}
}
