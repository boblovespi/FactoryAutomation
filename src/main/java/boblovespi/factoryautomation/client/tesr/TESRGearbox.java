package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox.GearType;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 5/13/2018.
 */
public class TESRGearbox extends TileEntityRenderer<TEGearbox>
{
	public TESRGearbox(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEGearbox te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		float inToRotate = te.rotationIn + partialTicks * te.GetSpeedIn();
		float outToRotate = te.rotationOut + partialTicks * te.GetSpeedOut();
		float topToRotate = te.rotationTop + partialTicks * te.speedTop;

		BlockState state = te.getWorld().getBlockState(te.getPos());
		Direction facing = state.get(Gearbox.FACING);
		Direction.Axis axis = facing.getAxis();
		float xD = 0, yD = 0, zD = 1;
		float m = -1;
		float n = 1;

		matrix.push();
		{
			// matrix.translate(x, y, z);
			// GlStateManager.translate(-xD / 2d, -yD / 2d, -zD / 2d);
			switch (facing)
			{
			case DOWN:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 1, 0, 0));
				matrix.translate(0, -1, 0);
				break;
			case UP:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
				matrix.translate(0, 0, -1);
				m = 1;
				n = -1;
				break;
			case NORTH:
				break;
			case SOUTH:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
				matrix.translate(-1, 0, -1);
				m = 1;
				n = -1;
				break;
			case WEST:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
				matrix.translate(-1, 0, 0);
				break;
			case EAST:
				matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
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
				RenderGear(te, 0.5f, 0.5f, 0.9f, gearIn.scaleFactor / sum, gearIn, n * inToRotate, xD, yD, zD, matrix,
						buffer, combinedLight, combinedOverlay);
				RenderGear(te, 0.5f, 0.5f, 0.1f, 0.5f, gearIn, n * outToRotate + 22.5f, xD, yD, zD, matrix, buffer,
						combinedLight, combinedOverlay);
			}

			if (gearOut != null)
			{
				RenderGear(te, 0.5f, 1f, 0.9f, gearOut.scaleFactor / sum, gearOut, m * outToRotate, xD, yD, zD, matrix,
						buffer, combinedLight, combinedOverlay);
				RenderGear(te, 0.5f, 1f, 0.1f, 0.5f, gearOut, m * outToRotate, xD, yD, zD, matrix, buffer,
						combinedLight, combinedOverlay);
			}

			RenderAxle(te, new Vector3d(0.5, 1, 0.05f), new Vector3d(xD, yD, zD), 0.9f, m * outToRotate, facing, matrix,
					buffer, combinedLight, combinedOverlay);

			RenderAxle(te, new Vector3d(0.5, 0.5, -0.14), new Vector3d(xD, yD, zD), 0.28f, n * outToRotate, facing, matrix,
					buffer, combinedLight, combinedOverlay);
			RenderAxle(te, new Vector3d(0.5, 0.5, 0.86), new Vector3d(xD, yD, zD), 0.28f, n * inToRotate, facing, matrix,
					buffer, combinedLight, combinedOverlay);

		}
		matrix.pop();

	}

	private void RenderAxle(TEGearbox te, Vector3d pos, Vector3d rotVec, float length, float rotation, Direction facing,
			MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		BlockState state = FABlocks.powerShaft.ToBlock().getDefaultState().with(PowerShaft.AXIS, Direction.Axis.Z)
											  .with(PowerShaft.IS_TESR, true);

		matrix.push();
		{
			RenderSystem.disableLighting();
			RenderSystem.disableRescaleNormal();

			matrix.translate(pos.x, pos.y, pos.z);

			matrix.scale(0.2f, 0.2f, length);

			matrix.rotate(TESRUtils.QuatFromAngleAxis(rotation, (float) rotVec.x, (float) rotVec.y, (float) rotVec.z));

			// matrix.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			// bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.isAmbientOcclusionEnabled())
			{
				RenderSystem.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				RenderSystem.shadeModel(GL11.GL_FLAT);
			}

			// Tessellator tessellator = Tessellator.getInstance();
			// BufferBuilder buffer = tessellator.getBuffer();

			// buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
			// IBakedModel model = dispatcher.getModelForState(state);

			dispatcher.renderBlock(state, matrix, buffer, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
			// tessellator.draw();

			RenderHelper.enableStandardItemLighting();
			RenderSystem.enableLighting();
		}
		matrix.pop();
	}

	private void RenderGear(TEGearbox te, float posX, float posY, float posZ, float size, GearType type,
			float inToRotate, float xD, float yD, float zD, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		if (type == null)
			return;
		ItemStack stack = new ItemStack(FAItems.gear.GetItem(type));
		RenderHelper.enableStandardItemLighting();
		RenderSystem.enableLighting();
		matrix.push();
		{
			matrix.translate(posX, posY, posZ);
			matrix.scale(size, size, 0.6f);
			matrix.rotate(TESRUtils.QuatFromAngleAxis(inToRotate, xD, yD, zD));

			Minecraft.getInstance().getItemRenderer()
					 .renderItem(stack, ItemCameraTransforms.TransformType.NONE, combinedLight, combinedOverlay, matrix,
							 buffer);

		}
		matrix.pop();
	}
}
