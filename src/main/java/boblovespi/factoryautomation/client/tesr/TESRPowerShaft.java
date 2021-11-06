package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 1/15/2018.
 * power shaft renderer
 */
public class TESRPowerShaft extends TileEntityRenderer<TEPowerShaft>
{
	public TESRPowerShaft(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEPowerShaft te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		if (!te.hasLevel() || te.getBlockState().getBlock() != FABlocks.powerShaft)
			return;

		float toRotate = te.rotation + partialTicks * te.GetSpeed();
		BlockState state = te.getBlockState().setValue(PowerShaft.IS_TESR, true);
		Direction.Axis axis = state.getValue(PowerShaft.AXIS);
		float xD = 0, yD = 0, zD = 0;
		switch (axis)
		{
		case X:
			xD = 1;
			yD = zD = 0;
			break;
		case Y:
			yD = 1;
			xD = zD = 0;
			break;
		case Z:
			zD = 1;
			yD = xD = 0;
			break;
		}

		matrix.pushPose();
		{
			RenderHelper.setupForFlatItems();
			RenderSystem.disableLighting();
			RenderSystem.disableRescaleNormal();
			matrix.translate(0.5, 0.5, 0.5);
			matrix.translate(-xD / 2d, -yD / 2d, -zD / 2d);
			matrix.mulPose(TESRUtils.QuatFromAngleAxis(toRotate, xD, yD, zD));
			// matrix.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			// bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.useAmbientOcclusion())
			{
				RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			// Tessellator tessellator = Tessellator.getInstance();
			// BufferBuilder bufferBuilder = tessellator.getBuffer();

			// bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

			dispatcher.renderBlock(state, matrix, buffer, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
			// tessellator.draw();
			RenderHelper.setupFor3DItems();
			RenderSystem.enableLighting();
		}
		matrix.popPose();
	}
}
