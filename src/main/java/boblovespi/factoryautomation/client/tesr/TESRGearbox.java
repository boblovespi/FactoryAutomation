package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox.GearType;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 5/13/2018.
 */
public class TESRGearbox extends TileEntitySpecialRenderer<TEGearbox>
{
	@Override
	public void render(TEGearbox te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if (!te.hasWorld() || te.getWorld().getBlockState(te.getPos()).getBlock() != FABlocks.gearbox)
			return;

		float inToRotate = te.rotationIn + partialTicks * te.GetSpeedIn();
		float outToRotate = te.rotationOut + partialTicks * te.GetSpeedOut();
		float topToRotate = te.rotationTop + partialTicks * te.speedTop;

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		EnumFacing facing = state.getValue(Gearbox.FACING);
		EnumFacing.Axis axis = facing.getAxis();
		float xD = 0, yD = 0, zD = 1;
		float m = -1;
		float n = 1;

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);
			// GlStateManager.translate(-xD / 2d, -yD / 2d, -zD / 2d);
			switch (facing)
			{
			case DOWN:
				GlStateManager.rotate(270, 1, 0, 0);
				GlStateManager.translate(0, -1, 0);
				break;
			case UP:
				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.translate(0, 0, -1);
				m = 1;
				n = -1;
				break;
			case NORTH:
				break;
			case SOUTH:
				GlStateManager.rotate(180, 0, 1, 0);
				GlStateManager.translate(-1, 0, -1);
				m = 1;
				n = -1;
				break;
			case WEST:
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.translate(-1, 0, 0);
				break;
			case EAST:
				GlStateManager.rotate(270, 0, 1, 0);
				GlStateManager.translate(0, 0, -1);
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
				RenderGear(te, 0.5f, 0.5f, 0.9f, gearIn.scaleFactor / sum, gearIn, n * inToRotate, xD, yD, zD);
				RenderGear(te, 0.5f, 0.5f, 0.1f, 0.5f, gearIn, n * outToRotate + 22.5f, xD, yD, zD);
			}

			if (gearOut != null)
			{
				RenderGear(te, 0.5f, 1f, 0.9f, gearOut.scaleFactor / sum, gearOut, m * outToRotate, xD, yD, zD);
				RenderGear(te, 0.5f, 1f, 0.1f, 0.5f, gearOut, m * outToRotate, xD, yD, zD);
			}

			RenderAxle(te, new Vec3d(0.5, 1, 0.05f), new Vec3d(xD, yD, zD), 0.9f, m * outToRotate, facing);

			RenderAxle(te, new Vec3d(0.5, 0.5, -0.14), new Vec3d(xD, yD, zD), 0.28f, n * outToRotate, facing);
			RenderAxle(te, new Vec3d(0.5, 0.5, 0.86), new Vec3d(xD, yD, zD), 0.28f, n * inToRotate, facing);

		}
		GlStateManager.popMatrix();

	}

	private void RenderAxle(TEGearbox te, Vec3d pos, Vec3d rotVec, float length, float rotation, EnumFacing facing)
	{
		IBlockState state = FABlocks.powerShaft.ToBlock().getDefaultState()
											   .withProperty(PowerShaft.AXIS, EnumFacing.Axis.Z)
											   .withProperty(PowerShaft.IS_TESR, true);

		GlStateManager.pushMatrix();
		{
			GlStateManager.disableLighting();
			GlStateManager.disableRescaleNormal();

			GlStateManager.translate(pos.x, pos.y, pos.z);

			GlStateManager.scale(0.2, 0.2, length);

			GlStateManager.rotate(rotation, (float) rotVec.x, (float) rotVec.y, (float) rotVec.z);

			GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if (Minecraft.isAmbientOcclusionEnabled())
			{
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				GlStateManager.shadeModel(GL11.GL_FLAT);
			}

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			IBakedModel model = dispatcher.getModelForState(state);

			dispatcher.getBlockModelRenderer().renderModel(te.getWorld(), model, state, te.getPos(), buffer, true);
			tessellator.draw();

			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
		}
		GlStateManager.popMatrix();
	}

	private void RenderGear(TEGearbox te, float posX, float posY, float posZ, float size, GearType type,
			float inToRotate, float xD, float yD, float zD)
	{
		if (type == null)
			return;
		ItemStack stack = new ItemStack(FAItems.gear.GetItem(type));
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(posX, posY, posZ);
			GlStateManager.scale(size, size, 0.6);
			GlStateManager.rotate(inToRotate, xD, yD, zD);

			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

		}
		GlStateManager.popMatrix();
	}
}
