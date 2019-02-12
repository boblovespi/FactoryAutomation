package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.machine.Millstone;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMillstone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 2/12/2019.
 */
public class TESRMillstone extends TileEntitySpecialRenderer<TEMillstone>
{
	private IBakedModel cache = null;
	private IBlockState state = null;

	@Override
	public void render(TEMillstone te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if (!te.hasWorld() || te.getWorld().getBlockState(te.getPos()).getBlock() != FABlocks.millstone)
			return;

		float toRotate = te.rotation + partialTicks * te.GetSpeed();
		if (state == null)
			state = te.getWorld().getBlockState(te.getPos()).withProperty(Millstone.IS_TOP, true);
		GlStateManager.pushMatrix();
		{
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.translate(x + 0.5, y, z + 0.5);
			GlStateManager.rotate(toRotate, 0, 1, 0);
			GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.isAmbientOcclusionEnabled())
			{
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				GlStateManager.shadeModel(GL11.GL_FLAT);
			}

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			if (cache == null)
				cache = dispatcher.getModelForState(state);

			dispatcher.getBlockModelRenderer().renderModel(te.getWorld(), cache, state, te.getPos(), buffer, true);
			tessellator.draw();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
		}
		GlStateManager.popMatrix();
	}
}
