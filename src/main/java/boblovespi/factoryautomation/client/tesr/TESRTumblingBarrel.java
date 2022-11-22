package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TETumblingBarrel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.resource.GeckoLibCache;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class TESRTumblingBarrel extends GeoBlockRenderer<TETumblingBarrel>
{
	public TESRTumblingBarrel(BlockEntityRendererProvider.Context rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new AnimatedGeoModel<>()
		{
			@Override
			public ResourceLocation getAnimationResource(TETumblingBarrel animatable)
			{
				return new ResourceLocation(MODID, "animations/tumbling_barrel.animation.json");
			}

			@Override
			public ResourceLocation getModelResource(TETumblingBarrel object)
			{
				return new ResourceLocation(MODID, "geo/tumbling_barrel.geo.json");
			}

			@Override
			public ResourceLocation getTextureResource(TETumblingBarrel object)
			{
				return new ResourceLocation(MODID, "textures/blocks/machines/tumbling_barrel.png");
			}
		});
	}

	@Override
	public void render(TETumblingBarrel tile, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
	{
		GeckoLibCache.getInstance().parser.setValue("rot", tile.GetSpeed() * partialTicks + tile.rotation);
		super.render(tile, partialTicks, stack, bufferIn, packedLightIn);
	}
}
