package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.machine.Millstone;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMillstone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

/**
 * Created by Willi on 2/12/2019.
 */
public class TESRMillstone implements BlockEntityRenderer<TEMillstone>
{
	private BlockEntityRendererProvider.Context context;
	private BlockState state = null;

	public TESRMillstone(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
	}

	@Override
	public void render(TEMillstone te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{
		float toRotate = te.rotation + partialTicks * te.GetSpeed();
		if (state == null)
			state = FABlocks.millstone.defaultBlockState().setValue(Millstone.IS_TOP, true);
		matrix.pushPose();
		{
			matrix.translate(0.5, 0, 0.5);
			matrix.mulPose(TESRUtils.QuatFromAngleAxis(toRotate, 0, 1, 0));
			// matrix.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			// bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.useAmbientOcclusion())
			{
				// RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				// RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			// version that caches the blockstate model

			// Tessellator tessellator = Tessellator.getInstance();
			// BufferBuilder buffer = tessellator.getBuffer();
			// buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			BlockRenderDispatcher dispatcher = context.getBlockRenderDispatcher();
			dispatcher.renderSingleBlock(state, matrix, buffer, combinedLight, combinedOverlay, ModelData.EMPTY, null);
			// tessellator.draw();

			// Lighting.turnBackOn();
		}
		matrix.popPose();
	}
}
