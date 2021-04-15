package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.BevelGearbox;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEBevelGear;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 8/9/2019.
 */
public class TESRBevelGear extends TileEntityRenderer<TEBevelGear>
{
	private final BevelGearbox model = new BevelGearbox();

	public TESRBevelGear(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TEBevelGear te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		renderDispatcher.textureManager.bindTexture(
				new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/bevel_gearbox.png"));

		boolean isUpper = false;
		matrix.push();
		{
			// RenderSystem.enableLighting();
			// RenderSystem.enableDepthTest();
			// RenderHelper.enableStandardItemLighting();
			// RenderSystem.enableRescaleNormal();

			BlockState state = te.getWorld().getBlockState(te.getPos());
			Direction facing = state.getValue(BevelGear.FACING);
			int layer = state.getValue(BevelGear.LAYER);
			int out = 1, in = 1;
			switch (facing)
			{
			case NORTH:
				switch (layer)
				{
				case 0: // upper gear
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 1, 0, 0));
					matrix.translate(-0.5, -0.5, 0.5);
					in = -1;
					isUpper = true;
					break;
				case 1:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
					matrix.translate(-0.5, -1.5, -0.5);
					in = -1;
					out = -1;
					break;
				case 2:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 0, 0, 1));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
					matrix.translate(-0.5, -0.5, -0.5);
					in = -1;
					out = -1;
					break;
				}
				break;
			case SOUTH:
				switch (layer)
				{
				case 0: // lower gear
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 0, 1));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
					matrix.translate(-0.5, -1.5, -0.5);
					out = -1;
					in = -1;
					break;
				case 1:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
					matrix.translate(0.5, -1.5, 0.5);
					break;
				case 2:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 0, 1));
					matrix.translate(0.5, -1.5, -0.5);
					out = -1;
					isUpper = true;
					break;
				}
				break;
			case WEST:
				switch (layer)
				{
				case 0: // upper gear
					matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 1, 0, 0));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
					matrix.translate(-0.5, -1.5, 0.5);
					in = -1;
					isUpper = true;
					break;
				case 1:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.translate(-0.5, -1.5, 0.5);
					in = -1;
					isUpper = true;
					break;
				case 2:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.translate(-0.5, -1.5, -0.5);
					in = -1;
					out = -1;
					break;
				}
				break;
			case EAST:
				switch (layer)
				{
				case 0: // lower gear
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
					matrix.translate(-0.5, -0.5, -0.5);
					in = -1;
					out = -1;
					break;
				case 1:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.rotate(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
					matrix.translate(0.5, -1.5, -0.5);
					out = -1;
					isUpper = true;
					break;
				case 2:
					matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.translate(0.5, -0.5, -0.5);
					out = -1;
					isUpper = true;
					break;
				}
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
			model.Rotate(te.rotation + te.GetSpeed() * partialTicks, out, in);
			model.render(matrix, buffer.getBuffer(model.getRenderType(new ResourceLocation(FactoryAutomation.MODID,
					"textures/blocks/machines/bevel_gearbox.png"))), combinedLight, combinedOverlay, 1, 1, 1, 1);

			if (isUpper)
			{
				in *= -1;
				out *= -1; // flip the signs for some reason
				RenderGear(null, 0, 16, 6, 12, Gearbox.GearType.IRON, te.rotation + te.GetSpeed() * partialTicks, 0, 0,
						in, matrix, buffer, combinedLight, combinedOverlay);
			} else
			{
				RenderGear(null, 0, 16, -6, 12, Gearbox.GearType.IRON, te.rotation + te.GetSpeed() * partialTicks, 0, 0,
						in, matrix, buffer, combinedLight, combinedOverlay);
			}
			matrix.rotate(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
			RenderGear(null, 0, 16, 6, 12, Gearbox.GearType.IRON, te.rotation + te.GetSpeed() * partialTicks + 22.5f, 0,
					0, out, matrix, buffer, combinedLight, combinedOverlay);

		}
		matrix.pop();
	}

	private void RenderGear(TEGearbox te, float posX, float posY, float posZ, float size, Gearbox.GearType type,
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
			matrix.scale(1 / 16f, 1 / 16f, 1 / 16f);
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
