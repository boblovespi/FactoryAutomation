package boblovespi.factoryautomation.client.tesr;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.FaceBakery;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;

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
		float sin = MathHelper.sin((float) (deg * Math.PI / 360f));
		return new Quaternion(x * sin, y * sin, z * sin, MathHelper.cos((float) (deg * Math.PI / 360f)));
	}

	public static TextureAtlasSprite GetFlowingTextureFromFluid(FluidStack fluid)
	{
		return ModelLoader.defaultTextureGetter()
						  .apply(ForgeHooksClient.getFluidMaterials(fluid.getFluid()).skip(1).findFirst().get());
	}

	public static void RenderQuads(MatrixStack matrix, IVertexBuilder buffer, List<BakedQuad> quads, ItemStack stack,
			int combinedLight, int combinedOverlay, int color)
	{
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex())
			{
				k = Minecraft.getInstance().getItemColors().getColor(stack, bakedquad.getTintIndex());
			}

			float f = (float) (k >> 16 & 255) / 255.0F;
			float f1 = (float) (k >> 8 & 255) / 255.0F;
			float f2 = (float) (k & 255) / 255.0F;
			buffer.addVertexData(matrix.getLast(), bakedquad, f, f1, f2, combinedLight, combinedOverlay, true);
		}
	}

	private static void RenderModel(IBakedModel modelIn, ItemStack stack, int combinedLightIn, int combinedOverlayIn,
			MatrixStack matrixStackIn, IVertexBuilder bufferIn, int color)
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

	public static void RenderItemModelFast(IBakedModel model, int color, ItemStack stack, BufferBuilder bufferBuilder,
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

	public static void RenderItemWithColor(ItemStack stack, ItemCameraTransforms.TransformType transforms,
			boolean leftHand, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn,
			IBakedModel model, int color)
	{
		matrix.push();
		{
			RenderType rendertype = RenderTypeLookup.getRenderType(stack);
			model = net.minecraftforge.client.ForgeHooksClient
					.handleCameraTransforms(matrix, model, transforms, leftHand);
			IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(buffer, rendertype, true, stack.hasEffect());
			RenderModel(model, stack, combinedLightIn, combinedOverlayIn, matrix, ivertexbuilder, color);
		}
		matrix.pop();
	}

	public static void RenderBakedModel(IBakedModel model, VertexFormat fmt, int color)
	{
		/*Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder levelRenderer = tessellator.getBuffer();
		levelRenderer.begin(GL11.GL_QUADS, fmt);
		for (BakedQuad bakedquad : model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(levelRenderer, bakedquad, color);
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
}
