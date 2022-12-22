package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEBevelGear;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by Willi on 8/9/2019.
 */
public class TESRBevelGear implements BlockEntityRenderer<TEBevelGear>
{
	// private final BevelGearbox model = new BevelGearbox();
	// public static final Material LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(FactoryAutomation.MODID,
	//		"textures/blocks/machines/bevel_gearbox.png"));
	private BlockEntityRendererProvider.Context context;

	public TESRBevelGear(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
	}

	@Override
	public void render(TEBevelGear te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{
		// renderer.textureManager.bind(
		//		new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/bevel_gearbox.png"));

		boolean isUpper = false;
		matrix.pushPose();
		{
			// RenderSystem.enableLighting();
			// RenderSystem.enableDepthTest();
			// RenderHelper.enableStandardItemLighting();
			// RenderSystem.enableRescaleNormal();

			BlockState state = te.getBlockState();
			if (!(state.getBlock() instanceof BevelGear))
				return;

			Direction facing = state.getValue(BevelGear.FACING);
			int layer = state.getValue(BevelGear.LAYER);
			int out = 1, in = 1;
			switch (facing)
			{
			case NORTH:
				switch (layer)
				{
				case 0: // upper gear
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 1, 0, 0));
					matrix.translate(-0.5, -0.5, 0.5);
					in = -1;
					isUpper = true;
					break;
				case 1:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
					matrix.translate(-0.5, -1.5, -0.5);
					in = -1;
					out = -1;
					break;
				case 2:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 0, 0, 1));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
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
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 0, 1));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
					matrix.translate(-0.5, -1.5, -0.5);
					out = -1;
					in = -1;
					break;
				case 1:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
					matrix.translate(0.5, -1.5, 0.5);
					break;
				case 2:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 0, 1));
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
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 1, 0, 0));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
					matrix.translate(-0.5, -1.5, 0.5);
					in = -1;
					isUpper = true;
					break;
				case 1:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.translate(-0.5, -1.5, 0.5);
					in = -1;
					isUpper = true;
					break;
				case 2:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
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
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(270, 0, 1, 0));
					matrix.translate(-0.5, -0.5, -0.5);
					in = -1;
					out = -1;
					break;
				case 1:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 0, 1));
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(180, 0, 1, 0));
					matrix.translate(0.5, -1.5, -0.5);
					out = -1;
					isUpper = true;
					break;
				case 2:
					matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 1, 0, 0));
					matrix.translate(0.5, -0.5, -0.5);
					out = -1;
					isUpper = true;
					break;
				}
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
			// model.Rotate(te.rotation + te.GetSpeed() * partialTicks, out, in);
			// model.renderToBuffer(matrix, buffer.getBuffer(model.renderType(new ResourceLocation(FactoryAutomation.MODID,
			// 		"textures/blocks/machines/bevel_gearbox.png"))), combinedLight, combinedOverlay, 1, 1, 1, 1);

			if (isUpper)
			{
				in *= -1;
				out *= -1; // flip the signs for some reason
				TESRUtils.RenderGear(te, 0, 16/16f, 6/16f, 12/16f, Gearbox.GearType.IRON, te.rotation + te.GetSpeed() * partialTicks, 0, 0,
						in, matrix, buffer, combinedLight, combinedOverlay);
			} else
			{
				TESRUtils.RenderGear(te, 0, 16/16f, -6/16f, 12/16f, Gearbox.GearType.IRON, te.rotation + te.GetSpeed() * partialTicks, 0, 0,
						in, matrix, buffer, combinedLight, combinedOverlay);
			}
			matrix.mulPose(TESRUtils.QuatFromAngleAxis(90, 0, 1, 0));
			TESRUtils.RenderGear(te, 0, 16/16f, 6/16f, 12/16f, Gearbox.GearType.IRON, te.rotation + te.GetSpeed() * partialTicks + 22.5f, 0,
					0, out, matrix, buffer, combinedLight, combinedOverlay);
			// Lighting.turnBackOn();
		}
		matrix.popPose();
	}
}
