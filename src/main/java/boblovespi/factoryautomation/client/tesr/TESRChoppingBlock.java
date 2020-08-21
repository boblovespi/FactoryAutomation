package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRChoppingBlock extends TileEntityRenderer<TEChoppingBlock>
{
	public TESRChoppingBlock(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}
	//	private RenderItem itemRenderer = null;
	//	private ItemStack itemCache = ItemStack.EMPTY;
	//	private IBakedModel modelCache = null;
	//	private BlockRendererDispatcher renderer = null;

	//	@Override
	//	public void renderTileEntityFast(TEChoppingBlock te, double x, double y, double z, float partialTicks,
	//			int destroyStage, float partial, BufferBuilder buffer)
	//	{
	//		ItemStack item = te.GetRenderStack();
	//		if (itemRenderer == null)
	//			itemRenderer = Minecraft.getMinecraft().getRenderItem();
	//		if (renderer == null)
	//			renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
	//		if (!item.isEmpty())
	//		{
	//			BlockPos pos = te.getPos();
	//			if (!item.isItemEqual(itemCache)) // update the cache if outdated
	//			{
	//				modelCache = itemRenderer.getItemModelWithOverrides(item.copy().splitStack(1), null, null);
	//				itemCache = item;
	//			}
	//			buffer.setTranslation(x - pos.getX(), y - pos.getY() + 1, z - pos.getZ());
	//			TESRUtils.RenderItemModelFast(modelCache, -1, itemCache, buffer, Minecraft.getMinecraft().getItemColors());
	//		}
	//	}

	@Override
	public void render(TEChoppingBlock te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			matrix.push();
			{
				matrix.translate(0.5, 0.75, 0.5);
				matrix.scale(0.5f, 0.5f, 0.5f);
				Minecraft.getInstance().getItemRenderer()
						 .renderItem(item, ItemCameraTransforms.TransformType.NONE, combinedLight, combinedOverlay,
								 matrix, buffer);
			}
			matrix.pop();
		}
	}
}
