package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

/**
 * Created by Willi on 12/27/2018.
 */
public class TESRChoppingBlock implements BlockEntityRenderer<TEChoppingBlock>
{
	private BlockEntityRendererProvider.Context context;

	public TESRChoppingBlock(BlockEntityRendererProvider.Context context)
	{
		this.context = context;
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
	public void render(TEChoppingBlock te, float partialTicks, PoseStack matrix, MultiBufferSource buffer,
			int combinedLight, int combinedOverlay)
	{
		ItemStack item = te.GetRenderStack();

		if (!item.isEmpty())
		{
			matrix.pushPose();
			{
				matrix.translate(0.5, 0.75, 0.5);
				matrix.scale(0.5f, 0.5f, 0.5f);
				Minecraft.getInstance().getItemRenderer()
						 .renderStatic(item, ItemTransforms.TransformType.NONE, combinedLight, combinedOverlay,
								 matrix, buffer, te.getBlockPos().hashCode());
			}
			matrix.popPose();
		}
	}
}
