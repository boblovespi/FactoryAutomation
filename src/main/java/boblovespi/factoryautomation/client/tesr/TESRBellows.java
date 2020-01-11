package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.BellowsModel;
import boblovespi.factoryautomation.common.tileentity.mechanical.TELeatherBellows;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 8/7/2019.
 */
public abstract class TESRBellows<T extends TileEntity & IBellowsTE> extends TileEntitySpecialRenderer<T>
{
	private final String texture;
	private BellowsModel model = new BellowsModel();

	protected TESRBellows(String texture)
	{
		this.texture = texture;
	}

	@Override
	public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		bindTexture(new ResourceLocation(FactoryAutomation.MODID, texture));

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);

			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();

			BlockState state = te.getWorld().getBlockState(te.getPos());
			Direction facing = state.getValue(BlockHorizontal.FACING);
			switch (facing)
			{
			case NORTH:
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.translate(-0.5, -1.5, 0.5);
				break;
			case SOUTH:
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.rotate(180, 0, 1, 0);
				GlStateManager.translate(0.5, -1.5, -0.5);
				break;
			case WEST:
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.rotate(270, 0, 1, 0);
				GlStateManager.translate(0.5, -1.5, 0.5);
				break;
			case EAST:
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.translate(-0.5, -1.5, -0.5);
				break;
			}

			GlStateManager.scale(1 / 16d, 1 / 16d, 1 / 16d);
			if (Minecraft.isAmbientOcclusionEnabled())
			{
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				GlStateManager.shadeModel(GL11.GL_FLAT);
			}

			model.Rotate(te.GetLerp() + te.GetLerpSpeed() * partialTicks);

			model.RenderTESR(1);
		}
		GlStateManager.popMatrix();
	}

	public static class Leather extends TESRBellows<TELeatherBellows>
	{
		public Leather()
		{
			super("textures/blocks/machines/leather_bellows.png");
		}
	}

	public static class Paper extends TESRBellows<TEPaperBellows>
	{
		public Paper()
		{
			super("textures/blocks/machines/paper_bellows.png");
		}
	}
}