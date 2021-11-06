package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Willi on 7/25/2018.
 */
public class TESRPlacedBucket extends TileEntityRenderer<TEPlacedBucket>
{
	public TESRPlacedBucket(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEPlacedBucket te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		FluidStack fluidStack = te.GetFluidStack();
		if (fluidStack == null || fluidStack.isEmpty())
			return;
		// bindTexture();
		TextureAtlasSprite sprite = TESRUtils.GetFlowingTextureFromFluid(fluidStack);
		double amount = fluidStack.getAmount() / 1000d;

		// Tessellator tessellator = Tessellator.getInstance();
		// BufferBuilder buffer1 = tessellator.getBuffer();

		matrix.pushPose();
		{
			matrix.translate(5 / 16d, amount * 0.4 + 1 / 16d, 5 / 16d);
			matrix.scale(6 / 16f, 0.5f, 6 / 16f);

			RenderSystem.disableLighting();

			// bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			// buffer1.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			// buffer1.pos(0, 0, 0).tex(minU, minV).endVertex();
			// buffer1.pos(0, 0, 1).tex(minU, maxV).endVertex();
			// buffer1.pos(1, 0, 1).tex(maxU, maxV).endVertex();
			// buffer1.pos(1, 0, 0).tex(maxU, minV).endVertex();
			Minecraft.getInstance().getBlockRenderer().renderLiquid(te.getBlockPos(), te.getLevel(),
					buffer.getBuffer(RenderTypeLookup.getRenderLayer(fluidStack.getFluid().defaultFluidState())),
					fluidStack.getFluid().defaultFluidState());
			// TODO: draw quads manually for efficiency

			// tessellator.draw();
		}
		matrix.popPose();
	}
}
