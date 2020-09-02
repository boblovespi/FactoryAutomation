package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.ElectricEngine2;
import boblovespi.factoryautomation.common.block.machine.Motor;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMotor;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 5/26/2018.
 */
public class TESRMotor extends TileEntityRenderer<TEMotor>
{
	private ElectricEngine2 engineModel = new ElectricEngine2();

	public TESRMotor(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEMotor te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay)
	{
		// if (!te.hasWorld() || te.getWorld().getBlockState(te.getPos()).getBlock() != FABlocks.motor)
		// 	return;

		// engineModel.Rotate(0);
		// engineModel = new ElectricEngine2();
		renderDispatcher.textureManager.bindTexture(
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/electric_engine_2.png"));

		matrix.push();
		{
			RenderSystem.enableLighting();
			RenderSystem.enableDepthTest();
			RenderHelper.enableStandardItemLighting();
			RenderSystem.enableRescaleNormal();

			BlockState state = te.getWorld().getBlockState(te.getPos());
			Direction facing = state.get(Motor.FACING);
			int m = 0;
			int n = 0;
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
				m = 1;
				n = -1;
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
				m = 1;
				n = -1;
				break;
			}

			// matrix.scale(1 / 16f, 1 / 16f, 1 / 16f);
			if (Minecraft.isAmbientOcclusionEnabled())
			{
				RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			engineModel.Rotate((float) Math.toRadians(te.rotation + partialTicks * te.GetSpeedOnFace(facing)));

			engineModel.render(matrix, buffer.getBuffer(RenderType.getEntityCutoutNoCull(
					new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/electric_engine_2.png"))),
					combinedLight, combinedOverlay, 1, 1, 1, 1);

		}
		matrix.pop();
	}
}