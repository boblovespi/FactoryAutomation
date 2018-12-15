package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.ElectricEngine;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.machine.Motor;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMotor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 5/26/2018.
 */
public class TESRMotor extends TileEntitySpecialRenderer<TEMotor>
{
	private ElectricEngine engineModel = new ElectricEngine();

	@Override
	public void render(TEMotor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if (!te.hasWorld() || te.getWorld().getBlockState(te.getPos()).getBlock() != FABlocks.motor)
			return;

		engineModel.Rotate(0);
		engineModel = new ElectricEngine();
		bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/electric_engine.png"));

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);

			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();

			IBlockState state = te.getWorld().getBlockState(te.getPos());
			EnumFacing facing = state.getValue(Motor.FACING);
			int m = 0;
			int n = 0;
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
				m = 1;
				n = -1;
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
				m = 1;
				n = -1;
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

			engineModel.Rotate((float) Math.toRadians(te.rotation + partialTicks * te.GetSpeedOnFace(facing)));

			engineModel.RenderTESR(1);

		}
		GlStateManager.popMatrix();
	}
}
