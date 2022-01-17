package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.mechanical.TETripHammerController;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class TESRTripHammer extends GeoBlockRenderer<TETripHammerController>
{
	public TESRTripHammer(BlockEntityRendererProvider.Context rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new AnimatedGeoModel<>()
		{
			@Override
			public ResourceLocation getAnimationFileLocation(TETripHammerController animatable)
			{
				return new ResourceLocation(MODID, "animations/trip_hammer.animation.json");
			}

			@Override
			public ResourceLocation getModelLocation(TETripHammerController object)
			{
				return new ResourceLocation(MODID, "geo/trip_hammer.geo.json");
			}

			@Override
			public ResourceLocation getTextureLocation(TETripHammerController object)
			{
				return new ResourceLocation(MODID, "textures/blocks/machines/trip_hammer.png");
			}
		});
	}

	@Override
	public void render(TETripHammerController tile, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn)
	{
		if (tile.IsStructureValid())
			super.render(tile, partialTicks, stack, bufferIn, packedLightIn);
	}
}
