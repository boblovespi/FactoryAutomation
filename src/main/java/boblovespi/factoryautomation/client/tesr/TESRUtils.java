package boblovespi.factoryautomation.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Willi on 7/25/2018.
 */

@SideOnly(Side.CLIENT)
public class TESRUtils
{
	public static final FaceBakery faceBakery = new FaceBakery();
	private static BlockColors itemColors;

	private TESRUtils()
	{
	}

	public static TextureAtlasSprite GetFlowingTextureFromFluid(FluidStack fluid)
	{
		return ModelLoader.defaultTextureGetter().apply(fluid.getFluid().getFlowing(fluid));
	}

	public static void RenderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack,
			ItemColors colorMult)
	{
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex())
			{
				k = colorMult.colorMultiplier(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable)
				{
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	public static void RenderItemModelFast(IBakedModel model, int color, ItemStack stack, BufferBuilder bufferBuilder,
			ItemColors colorMult)
	{
		// bufferBuilder.begin(7, DefaultVertexFormats.ITEM);

		for (Direction Direction : Direction.values())
		{
			RenderQuads(bufferBuilder, model.getQuads(null, Direction, 0L), color, stack, colorMult);
		}

		RenderQuads(bufferBuilder, model.getQuads(null, null, 0L), color, stack, colorMult);

	}

	public static void RenderItemWithColor(ItemStack stack, ItemCameraTransforms.TransformType transform, float r,
			float g, float b, float a)
	{
		if (!stack.isEmpty())
		{
			IBakedModel bakedModel = Minecraft.getMinecraft().getRenderItem()
											  .getItemModelWithOverrides(stack, null, null);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
												 .setBlurMipmap(false, false);
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
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
												 .restoreLastBlurMipmap();
		}
	}

	public static void RenderBakedModel(IBakedModel model, VertexFormat fmt, int color)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRenderer = tessellator.getBuffer();
		worldRenderer.begin(GL11.GL_QUADS, fmt);
		for (BakedQuad bakedquad : model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(worldRenderer, bakedquad, color);
		}
		tessellator.draw();
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
