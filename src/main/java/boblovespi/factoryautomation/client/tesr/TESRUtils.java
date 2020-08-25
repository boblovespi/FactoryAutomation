package boblovespi.factoryautomation.client.tesr;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.FaceBakery;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
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

	public static void RenderItemWithColor(ItemStack stack, ItemCameraTransforms.TransformType transform, float r,
			float g, float b, float a)
	{
		/*if (!stack.isEmpty())
		{
			IBakedModel bakedModel = Minecraft.getInstance().getItemRenderer()
											  .getItemModelWithOverrides(stack, null, null);
			GlStateManager.color(r, g, b, a);
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			bakedModel = ForgeHooksClient.handleCameraTransforms(bakedModel, transform, false);
			// Minecraft.getMinecraft().getRenderItem().renderItem(stack, bakedModel);
			RenderBakedModel(bakedModel, DefaultVertexFormats.ITEM, RGBAToHex(r, g, b, a));
			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
			Minecraft.getInstance().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getInstance().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		}*/
	}

	public static void RenderBakedModel(IBakedModel model, VertexFormat fmt, int color)
	{
		/*Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRenderer = tessellator.getBuffer();
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
}
