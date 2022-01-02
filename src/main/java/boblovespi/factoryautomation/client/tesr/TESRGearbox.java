package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox.GearType;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 5/13/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TESRGearbox implements BlockEntityRenderer<TEGearbox>
{
	private BlockEntityRendererProvider.Context context;

	public TESRGearbox(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
	}

	@Override
	public void render(TEGearbox te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{
		float inToRotate = te.rotationIn + partialTicks * te.GetSpeedIn();
		float outToRotate = te.rotationOut + partialTicks * te.GetSpeedOut();
		float topToRotate = te.rotationTop + partialTicks * te.speedTop;

		BlockState state = Objects.requireNonNull(te.getLevel()).getBlockState(te.getBlockPos());
		if (state.isAir())
			return;
		Direction facing = state.getValue(Gearbox.FACING);
		Direction.Axis axis = facing.getAxis();
		float xD = 0, yD = 0, zD = 1;
		float m = -1;
		float n = 1;

		matrix.pushPose();
		{
			// matrix.translate(x, y, z);
			// GlStateManager.translate(-xD / 2d, -yD / 2d, -zD / 2d);
			switch (facing)
			{
			case DOWN:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 1, 0, 0));
				matrix.translate(0, -1, 0);
				break;
			case UP:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
				matrix.translate(0, 0, -1);
				m = 1;
				n = -1;
				break;
			case NORTH:
				break;
			case SOUTH:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
				matrix.translate(-1, 0, -1);
				m = 1;
				n = -1;
				break;
			case WEST:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
				matrix.translate(-1, 0, 0);
				break;
			case EAST:
				matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
				matrix.translate(0, 0, -1);
				m = 1;
				n = -1;
				break;
			}

			GearType gearIn = te.GetGearIn();
			GearType gearOut = te.GetGearOut();

			float sum = (gearIn == null ? 1 : gearIn.scaleFactor) + (gearOut == null ? 1 : gearOut.scaleFactor);

			float v = 0.1f;

			if (gearIn != null)
			{
				TESRUtils.RenderGear(te, 0.5f, 0.5f, 0.9f, gearIn.scaleFactor / sum, gearIn, n * inToRotate, xD, yD, zD, matrix,
						buffer, combinedLight, combinedOverlay);
				TESRUtils.RenderGear(te, 0.5f, 0.5f, 0.1f, 0.5f, gearIn, n * outToRotate + 22.5f, xD, yD, zD, matrix, buffer,
						combinedLight, combinedOverlay);
			}

			if (gearOut != null)
			{
				TESRUtils.RenderGear(te, 0.5f, 1f, 0.9f, gearOut.scaleFactor / sum, gearOut, m * outToRotate, xD, yD, zD, matrix,
						buffer, combinedLight, combinedOverlay);
				TESRUtils.RenderGear(te, 0.5f, 1f, 0.1f, 0.5f, gearOut, m * outToRotate, xD, yD, zD, matrix, buffer,
						combinedLight, combinedOverlay);
			}

			RenderAxle(te, new Vec3(0.5, 1, 0.05f), new Vec3(xD, yD, zD), 0.9f, m * outToRotate, facing, matrix,
					buffer, combinedLight, combinedOverlay);

			RenderAxle(te, new Vec3(0.5, 0.5, -0.14), new Vec3(xD, yD, zD), 0.28f, n * outToRotate, facing, matrix,
					buffer, combinedLight, combinedOverlay);
			RenderAxle(te, new Vec3(0.5, 0.5, 0.86), new Vec3(xD, yD, zD), 0.28f, n * inToRotate, facing, matrix,
					buffer, combinedLight, combinedOverlay);

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
