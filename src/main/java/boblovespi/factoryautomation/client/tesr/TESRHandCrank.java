package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHandCrank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

/**
 * Created by Willi on 9/9/2018.
 */
public class TESRHandCrank implements BlockEntityRenderer<TEHandCrank>
{
	// private final HandCrankModel handCrankModel = new HandCrankModel();
	private BlockEntityRendererProvider.Context context;

	public TESRHandCrank(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
	}

	@Override
	public void render(TEHandCrank te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{
		// renderDispatcher.textureManager.bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/hand_crank.png"));

		matrix.pushPose();
		{
			// RenderSystem.enableLighting();
			// RenderSystem.enableDepthTest();
			// RenderHelper.enableStandardItemLighting();
			// RenderSystem.enableRescaleNormal();

			// matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
			if (te.inverted)
			{
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.translate(-0.5, -1, 0.5);
			} else
			{
				matrix.translate(0.5, 0, 0.5);
			}
			if (te.IsRotating())
				matrix.mulPose(
						TESRUtils.QuatFromAngleAxis((te.inverted ? -1 : 1) * (te.rotation + EnergyConstants.RadiansSecondToDegreesTick(te.SPEED) * partialTicks), 0, 1, 0));

			// matrix.scale(1 / 16f, 1 / 16f, 1 / 16f);
			if (Minecraft.useAmbientOcclusion())
			{
				// RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				// RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			// handCrankModel.renderToBuffer(matrix, buffer.getBuffer(handCrankModel.renderType(
			//		new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/hand_crank.png"))),
			//		combinedLight, combinedOverlay, 1, 1, 1, 1);
			BlockState state = te.getBlockState();
			BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
			dispatcher.renderSingleBlock(state, matrix, buffer, combinedLight, combinedOverlay, ModelData.EMPTY, null);

		}
		matrix.popPose();
	}
}
