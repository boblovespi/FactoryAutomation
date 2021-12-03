package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.client.model.ElectricEngine2;
import boblovespi.factoryautomation.common.block.machine.Motor;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMotor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 5/26/2018.
 */
public class TESRMotor extends BlockEntityRenderer<TEMotor>
{
	private ElectricEngine2 engineModel = new ElectricEngine2();

	public TESRMotor(BlockEntityRenderDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEMotor te, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int combinedLight,
			int combinedOverlay)
	{
		// if (!te.hasWorld() || te.getWorld().getBlockState(te.getPos()).getBlock() != FABlocks.motor)
		// 	return;

		// engineModel.Rotate(0);
		// engineModel = new ElectricEngine2();
		// renderDispatcher.textureManager.bindTexture(
		//		new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/electric_engine_2.png"));

		matrix.pushPose();
		{
			// RenderSystem.enableLighting();
			// RenderSystem.enableDepthTest();
			// RenderHelper.enableStandardItemLighting();
			// RenderSystem.enableRescaleNormal();

			BlockState state = te.getBlockState();
			Direction facing = state.getValue(Motor.FACING);
			int m = 0;
			int n = 0;
			switch (facing)
			{
			case NORTH:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.translate(-0.5, -1.5, 0.5);
				break;
			case SOUTH:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
				matrix.translate(0.5, -1.5, -0.5);
				m = 1;
				n = -1;
				break;
			case WEST:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
				matrix.translate(0.5, -1.5, 0.5);
				break;
			case EAST:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
				matrix.translate(-0.5, -1.5, -0.5);
				m = 1;
				n = -1;
				break;
			}

			// matrix.scale(1 / 16f, 1 / 16f, 1 / 16f);
			if (Minecraft.useAmbientOcclusion())
			{
				// RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				// RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			engineModel.Rotate((float) Math.toRadians(te.rotation + partialTicks * EnergyConstants.RadiansSecondToDegreesTick(te.GetSpeedOnFace(facing))));

			engineModel.renderToBuffer(matrix, buffer.getBuffer(engineModel.renderType(
					new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/electric_engine_2.png"))),
					combinedLight, combinedOverlay, 1, 1, 1, 1);

		}
		matrix.popPose();
	}
}