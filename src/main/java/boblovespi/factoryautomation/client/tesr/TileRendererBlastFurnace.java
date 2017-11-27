package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.TileEntityBlastFurnaceController;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * Created by Willi on 11/21/2017.
 */
public class TileRendererBlastFurnace
		extends TileEntitySpecialRenderer<TileEntityBlastFurnaceController>
{
	@Override
	public void render(TileEntityBlastFurnaceController te, double x, double y,
			double z, float partialTicks, int destroyStage, float alpha)
	{
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		//		Minecraft.getMinecraft().getRenderManager().doRenderEntity();

		Tessellator tess = Tessellator.getInstance();

		tess.draw();
	}
}
