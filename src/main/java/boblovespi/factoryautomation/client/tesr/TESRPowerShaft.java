package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 1/15/2018.
 * power shaft renderer
 */
public class TESRPowerShaft extends TileEntitySpecialRenderer<TEPowerShaft>
{
	@Override
	public void render(TEPowerShaft te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		float toRotate = te.rotation + partialTicks * te.GetSpeed();
		IBlockState state = te.getWorld().getBlockState(te.getPos()).withProperty(PowerShaft.IS_TESR, true);
		EnumFacing.Axis axis = state.getValue(PowerShaft.AXIS);
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

		GlStateManager.pushMatrix();
		{
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
			GlStateManager.translate(-xD / 2d, -yD / 2d, -zD / 2d);
			GlStateManager.rotate(toRotate, xD, yD, zD);
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
			IBakedModel model = dispatcher.getModelForState(state);

			dispatcher.getBlockModelRenderer().renderModel(te.getWorld(), model, state, te.getPos(), buffer, true);
			tessellator.draw();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
		}
		GlStateManager.popMatrix();
	}
}
