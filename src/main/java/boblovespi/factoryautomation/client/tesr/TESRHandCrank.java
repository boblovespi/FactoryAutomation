package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.HandCrankModel;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHandCrank;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 9/9/2018.
 */
public class TESRHandCrank extends TileEntityRenderer<TEHandCrank>
{
	private final HandCrankModel handCrankModel = new HandCrankModel();

	public TESRHandCrank(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEHandCrank te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		// renderDispatcher.textureManager.bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/hand_crank.png"));

		matrix.push();
		{
			// RenderSystem.enableLighting();
			// RenderSystem.enableDepthTest();
			// RenderHelper.enableStandardItemLighting();
			// RenderSystem.enableRescaleNormal();

			matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
			if (te.inverted)
			{
				matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 1, 0, 0));
				matrix.translate(0, 1, -1);
			}
			matrix.translate(-0.5, -1.5, 0.5);
			if (te.IsRotating())
				matrix.rotate(
						TESRUtils.QuatFromAngleAxis((te.inverted ? -1 : 1) * (te.rotation + partialTicks), 0, -1, 0));

			// matrix.scale(1 / 16f, 1 / 16f, 1 / 16f);
			if (Minecraft.isAmbientOcclusionEnabled())
			{
				// RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				// RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			handCrankModel.render(matrix, buffer.getBuffer(handCrankModel.getRenderType(
					new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/hand_crank.png"))),
					combinedLight, combinedOverlay, 1, 1, 1, 1);

		}
		matrix.pop();
	}
}
