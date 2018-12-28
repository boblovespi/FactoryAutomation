package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRChoppingBlock extends TileEntitySpecialRenderer<TEChoppingBlock>
{
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
	public void render(TEChoppingBlock te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(x + 0.5, y + 0.75, z + 0.5);
				GlStateManager.scale(0.5, 0.5, 0.5);
				Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
			}
			GlStateManager.popMatrix();
		}
	}
}
