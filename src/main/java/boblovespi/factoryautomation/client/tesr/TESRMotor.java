package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.machine.Motor;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMotor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.EmptyModelData;

/**
 * Created by Willi on 5/26/2018.
 */
public class TESRMotor implements BlockEntityRenderer<TEMotor>
{
	private BlockEntityRendererProvider.Context context;
	// private ElectricEngine2 engineModel = new ElectricEngine2();

	public TESRMotor(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
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

			// engineModel.Rotate((float) Math.toRadians(te.rotation + partialTicks * EnergyConstants.RadiansSecondToDegreesTick(te.GetSpeedOnFace(facing))));

			// engineModel.renderToBuffer(matrix, buffer.getBuffer(engineModel.renderType(
			//		new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/electric_engine_2.png"))),
			//		combinedLight, combinedOverlay, 1, 1, 1, 1);

		}
		matrix.popPose();
	}

	private void RenderAxle(TEGearbox te, Vec3 pos, Vec3 rotVec, float length, float rotation, Direction facing,
							PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		BlockState state = FABlocks.powerShaft.ToBlock().defaultBlockState().setValue(PowerShaft.AXIS, Direction.Axis.Z)
				.setValue(PowerShaft.IS_TESR, true);

		matrix.pushPose();
		{
			// RenderSystem.disableLighting();
			// RenderSystem.disableRescaleNormal();

			matrix.translate(pos.x, pos.y, pos.z);

			matrix.scale(0.2f, 0.2f, length);

			matrix.mulPose(TESRUtils.QuatFromAngleAxis(rotation, (float) rotVec.x, (float) rotVec.y, (float) rotVec.z));

			// matrix.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			// bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.useAmbientOcclusion())
			{
				// RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				// RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			// Tessellator tessellator = Tessellator.getInstance();
			// BufferBuilder buffer = tessellator.getBuffer();

			// buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
			// IBakedModel model = dispatcher.getModelForState(state);

			dispatcher.renderSingleBlock(state, matrix, buffer, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
			// tessellator.draw();

			// Lighting.turnBackOn();
			// RenderSystem.enableLighting();
		}
		matrix.popPose();
	}
}