package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 7/25/2018.
 */
public class TESRPlacedBucket extends TileEntitySpecialRenderer<TEPlacedBucket>
{
	@Override
	public void render(TEPlacedBucket te, double x, double y, double z, float partialTicks, int destroyStage,
			float partial)
	{
		FluidStack fluidStack = te.GetFluidStack();
		if (fluidStack == null)
			return;
		// bindTexture();
		TextureAtlasSprite sprite = TESRUtils.GetFlowingTextureFromFluid(fluidStack);
		double amount = fluidStack.amount / 1000d;
		float minU = sprite.getMinU();
		float maxU = sprite.getMaxU();
		float minV = sprite.getMinV();
		float maxV = sprite.getMaxV();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x + 5 / 16d, y + amount * 0.4 + 1 / 16d, z + 5 / 16d);
			GlStateManager.scale(6 / 16d, 0.5, 6 / 16d);

			GlStateManager.disableLighting();

			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			buffer.pos(0, 0, 0).tex(minU, minV).endVertex();
			buffer.pos(0, 0, 1).tex(minU, maxV).endVertex();
			buffer.pos(1, 0, 1).tex(maxU, maxV).endVertex();
			buffer.pos(1, 0, 0).tex(maxU, minV).endVertex();

			tessellator.draw();
		}
		GlStateManager.popMatrix();
	}
}
