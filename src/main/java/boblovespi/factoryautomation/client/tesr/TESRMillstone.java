package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.machine.Millstone;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMillstone;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 2/12/2019.
 */
public class TESRMillstone extends TileEntityRenderer<TEMillstone>
{
	private IBakedModel cache = null;
	private BlockState state = null;

	public TESRMillstone(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEMillstone te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		float toRotate = te.rotation + partialTicks * te.GetSpeed();
		if (state == null)
			state = FABlocks.millstone.ToBlock().getDefaultState().with(Millstone.IS_TOP, true);
		matrix.push();
		{
			RenderHelper.disableStandardItemLighting();
			RenderSystem.disableLighting();
			RenderSystem.disableRescaleNormal();
			matrix.translate(0.5, 0, 0.5);
			matrix.rotate(TESRUtils.QuatFromAngleAxis(toRotate, 0, 1, 0));
			// matrix.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			// bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.isAmbientOcclusionEnabled())
			{
				RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			// version that caches the blockstate model

			// Tessellator tessellator = Tessellator.getInstance();
			// BufferBuilder buffer = tessellator.getBuffer();
			// buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
			if (cache == null)
				cache = dispatcher.getModelForState(state);
			dispatcher.getBlockModelRenderer()
					  .renderModel(matrix.getLast(), buffer.getBuffer(Atlases.getCutoutBlockType()), state, cache, 1f,
							  1f, 1f, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
			// tessellator.draw();

			RenderHelper.enableStandardItemLighting();
			RenderSystem.enableLighting();
		}
		matrix.pop();
	}
}
