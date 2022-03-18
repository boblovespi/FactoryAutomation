package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Willi on 7/25/2018.
 */

@OnlyIn(Dist.CLIENT)
public class TESRUtils
{
	public static final FaceBakery faceBakery = new FaceBakery();
	private static BlockColors itemColors;

	private TESRUtils()
	{
	}

	public static Quaternion QuatFromAngleAxis(float deg, float x, float y, float z)
	{
		float sin = Mth.sin((float) (deg * Math.PI / 360f));
		return new Quaternion(x * sin, y * sin, z * sin, Mth.cos((float) (deg * Math.PI / 360f)));
	}

	public static TextureAtlasSprite GetFlowingTextureFromFluid(FluidStack fluid)
	{
		return null; /*ModelLoader.defaultTextureGetter()
						  .apply(ForgeHooksClient.getFluidMaterials(fluid.getFluid()).skip(1).findFirst().get());*/
	}

	public static void RenderQuads(PoseStack matrix, VertexConsumer buffer, List<BakedQuad> quads, ItemStack stack,
			int combinedLight, int combinedOverlay, int color)
	{
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.isTinted())
			{
				k = Minecraft.getInstance().getItemColors().getColor(stack, bakedquad.getTintIndex());
			}

			float f = (float) (k >> 16 & 255) / 255.0F;
			float f1 = (float) (k >> 8 & 255) / 255.0F;
			float f2 = (float) (k & 255) / 255.0F;
			buffer.putBulkData(matrix.last(), bakedquad, f, f1, f2, combinedLight, combinedOverlay, true);
		}
	}

	private static void RenderModel(BakedModel modelIn, ItemStack stack, int combinedLightIn, int combinedOverlayIn,
			PoseStack matrixStackIn, VertexConsumer bufferIn, int color)
	{
		Random random = new Random();
		long i = 42L;

		for (Direction direction : Direction.values())
		{
			random.setSeed(42L);
			RenderQuads(
					matrixStackIn, bufferIn, modelIn.getQuads(null, direction, random), stack, combinedLightIn,
					combinedOverlayIn, color);
		}

		random.setSeed(42L);
		RenderQuads(
				matrixStackIn, bufferIn, modelIn.getQuads(null, null, random), stack, combinedLightIn,
				combinedOverlayIn, color);
	}

	public static void RenderItemModelFast(BakedModel model, int color, ItemStack stack, BufferBuilder bufferBuilder,
			ItemColors colorMult)
	{
		/*// bufferBuilder.begin(7, DefaultVertexFormats.ITEM);
		Random random = new Random(42);
		for (Direction Direction : Direction.values())
		{
			RenderQuads(bufferBuilder, model.getQuads(null, Direction, random), color, stack, colorMult);
		}

		RenderQuads(bufferBuilder, model.getQuads(null, null, random), color, stack, colorMult);*/
	}

	public static void RenderItemWithColor(ItemStack stack, ItemTransforms.TransformType transforms,
			boolean leftHand, PoseStack matrix, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn,
			BakedModel model, int color)
	{
		matrix.pushPose();
		{
			RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, true);
			model = net.minecraftforge.client.ForgeHooksClient
					.handleCameraTransforms(matrix, model, transforms, leftHand);
			VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(buffer, rendertype, true, stack.hasFoil());
			RenderModel(model, stack, combinedLightIn, combinedOverlayIn, matrix, ivertexbuilder, color);
		}
		matrix.popPose();
	}

	public static void RenderBakedModel(BakedModel model, VertexFormat fmt, int color)
	{
		/*Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder levelRenderer = tessellator.getBuffer();
		worldRenderer.begin(GL11.GL_QUADS, fmt);
		for (BakedQuad bakedquad : model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(worldRenderer, bakedquad, color);
		}
		tessellator.draw();*/
	}

	public static int RGBAToHex(float r, float g, float b, float a)
	{
		int bPart = (int) (b * 255);
		int gPart = (int) (g * 255) << 8;
		int rPart = (int) (r * 255) << 16;
		int aPart = (int) (a * 255) << 24;
		return aPart | rPart | gPart | bPart;
	}

	public static float RadiansToDegrees(float radians)
	{
		return (float) Math.toDegrees(radians);
	}

	public static void RenderGear(BlockEntity te, float posX, float posY, float posZ, float size, @Nullable Gearbox.GearType type,
								  float inToRotate, float xD, float yD, float zD, PoseStack matrix, MultiBufferSource buffer,
								  int combinedLight, int combinedOverlay)
	{
		if (type == null)
			return;
		ItemStack stack = new ItemStack(FAItems.gear.GetItem(type));
		// Lighting.turnBackOn();
		// RenderSystem.enableLighting();
		matrix.pushPose();
		{
			matrix.translate(posX, posY, posZ);
			matrix.scale(size, size, 0.6f);
			matrix.mulPose(TESRUtils.QuatFromAngleAxis(inToRotate, xD, yD, zD));

			Minecraft.getInstance().getItemRenderer()
					.renderStatic(stack, ItemTransforms.TransformType.NONE, combinedLight, combinedOverlay, matrix, buffer, te.getBlockPos().hashCode() + (int) (posY * 5 + posZ * 11 + posX * 13));

		}
		matrix.popPose();
	}

	public static void RenderBlockModel(BlockRenderDispatcher dispatcher, BlockState state, PoseStack stack, MultiBufferSource buffer, int i1, int i2, IModelData modelData)
	{
		BakedModel bakedmodel = dispatcher.getBlockModel(state);
		int i = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
		float f = (float)(i >> 16 & 255) / 255.0F;
		float f1 = (float)(i >> 8 & 255) / 255.0F;
		float f2 = (float)(i & 255) / 255.0F;
		dispatcher.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, bakedmodel, f, f1, f2, i1, i2, modelData);
	}
}
