package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.HandCrankModel;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHandCrank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 9/9/2018.
 */
public class TESRHandCrank extends TileEntitySpecialRenderer<TEHandCrank>
{
	private HandCrankModel handCrankModel = new HandCrankModel();

	@Override
	public void render(TEHandCrank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/hand_crank.png"));

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);

			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();

			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.translate(-0.5, -1.5, 0.5);
			if (te.IsRotating())
				GlStateManager.rotate(te.rotation + partialTicks, 0, -1, 0);

			GlStateManager.scale(1 / 16d, 1 / 16d, 1 / 16d);
			if (Minecraft.isAmbientOcclusionEnabled())
			{
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				GlStateManager.shadeModel(GL11.GL_FLAT);
			}

			handCrankModel.RenderTESR(1);

		}
		GlStateManager.popMatrix();
	}
}
