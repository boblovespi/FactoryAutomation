package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.BellowsModel;
import boblovespi.factoryautomation.common.tileentity.mechanical.TELeatherBellows;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 8/7/2019.
 */
public abstract class TESRBellows<T extends TileEntity & IBellowsTE> extends TileEntityRenderer<T>
{
	private final String texture;
	private BellowsModel model = new BellowsModel();

	protected TESRBellows(TileEntityRendererDispatcher renderDispatcher, String texture)
	{
		super(renderDispatcher);
		this.texture = texture;
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay)
	{
		// renderDispatcher.textureManager.bindTexture(new ResourceLocation(FactoryAutomation.MODID, texture));

		matrix.push();
		{
			// RenderSystem.enableLighting();
			// RenderSystem.enableDepthTest();
			// RenderHelper.enableStandardItemLighting();
			// RenderSystem.enableRescaleNormal();

			BlockState state = te.getWorld().getBlockState(te.getPos());
			Direction facing = state.get(HorizontalBlock.HORIZONTAL_FACING);
			switch (facing)
			{
			case NORTH:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.translate(-0.5, -1.5, 0.5);
				break;
			case SOUTH:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
				matrix.translate(0.5, -1.5, -0.5);
				break;
			case WEST:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
				matrix.translate(0.5, -1.5, 0.5);
				break;
			case EAST:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
				matrix.translate(-0.5, -1.5, -0.5);
				break;
			}

			// matrix.scale(1 / 16f, 1 / 16f, 1 / 16f);
			if (Minecraft.isAmbientOcclusionEnabled())
			{
				// RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				// RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			model.Rotate(te.GetLerp() + te.GetLerpSpeed() * partialTicks);

			model.render(matrix,
					buffer.getBuffer(model.getRenderType(new ResourceLocation(FactoryAutomation.MODID, texture))),
					combinedLight, combinedOverlay, 1, 1, 1, 1);
		}
		matrix.pop();
	}

	public static class Leather extends TESRBellows<TELeatherBellows>
	{
		public Leather(TileEntityRendererDispatcher dispatcher)
		{
			super(dispatcher, "textures/blocks/machines/leather_bellows.png");
		}
	}

	public static class Paper extends TESRBellows<TEPaperBellows>
	{
		public Paper(TileEntityRendererDispatcher dispatcher)
		{
			super(dispatcher, "textures/blocks/machines/paper_bellows.png");
		}
	}
}