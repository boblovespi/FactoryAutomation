package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.BellowsModel;
import boblovespi.factoryautomation.common.tileentity.mechanical.TELeatherBellows;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by Willi on 8/7/2019.
 */
public abstract class TESRBellows<T extends BlockEntity & IBellowsTE> implements BlockEntityRenderer<T>
{
	private final String texture;
	private BellowsModel model = new BellowsModel();

	protected TESRBellows(BlockEntityRenderDispatcher renderDispatcher, String texture)
	{
		this.texture = texture;
	}

	@Override
	public void render(T te, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int combinedLight,
			int combinedOverlay)
	{
		// renderDispatcher.textureManager.bindTexture(new ResourceLocation(FactoryAutomation.MODID, texture));

		matrix.pushPose();
		{
			// RenderSystem.enableLighting();
			// RenderSystem.enableDepthTest();
			// RenderHelper.enableStandardItemLighting();
			// RenderSystem.enableRescaleNormal();

			BlockState state = te.getBlockState();
			Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
			switch (facing)
			{
			case NORTH:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.translate(-0.5, -1.5, 0.5);
				break;
			case SOUTH:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
				matrix.translate(0.5, -1.5, -0.5);
				break;
			case WEST:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
				matrix.translate(0.5, -1.5, 0.5);
				break;
			case EAST:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
				matrix.translate(-0.5, -1.5, -0.5);
				break;
			}

			// matrix.scale(1 / 16f, 1 / 16f, 1 / 16f);
			if (Minecraft.useAmbientOcclusion())
			{
				// RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				// RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			model.Rotate(te.GetLerp() + te.GetLerpSpeed() * partialTicks);

			model.renderToBuffer(matrix,
					buffer.getBuffer(model.renderType(new ResourceLocation(FactoryAutomation.MODID, texture))),
					combinedLight, combinedOverlay, 1, 1, 1, 1);
		}
		matrix.popPose();
	}

	public static class Leather extends TESRBellows<TELeatherBellows>
	{
		public Leather(BlockEntityRenderDispatcher dispatcher)
		{
			super(dispatcher, "textures/blocks/machines/leather_bellows.png");
		}
	}

	public static class Paper extends TESRBellows<TEPaperBellows>
	{
		public Paper(BlockEntityRenderDispatcher dispatcher)
		{
			super(dispatcher, "textures/blocks/machines/paper_bellows.png");
		}
	}
}