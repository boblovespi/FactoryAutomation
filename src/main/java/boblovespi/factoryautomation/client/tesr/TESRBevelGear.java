package boblovespi.factoryautomation.client.tesr;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.model.BevelGearbox;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEBevelGear;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Willi on 8/9/2019.
 */
public class TESRBevelGear extends TileEntitySpecialRenderer<TEBevelGear>
{
	private BevelGearbox model = new BevelGearbox();

	@Override
	public void render(TEBevelGear te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		bindTexture(new ResourceLocation(FactoryAutomation.MODID, "textures/blocks/machines/bevel_gearbox.png"));

		boolean isUpper = false;
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);

			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();

			IBlockState state = te.getWorld().getBlockState(te.getPos());
			EnumFacing facing = state.getValue(BevelGear.FACING);
			int layer = state.getValue(BevelGear.LAYER);
			switch (facing)
			{
			case NORTH:
				switch (layer)
				{
				case 0: // upper gear
					GlStateManager.rotate(90, 1, 0, 0);
					GlStateManager.rotate(270, 0, 1, 0);
					GlStateManager.rotate(270, 1, 0, 0);
					GlStateManager.translate(-0.5, -0.5, 0.5);
					break;
				case 1:
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.rotate(90, 0, 1, 0);
					GlStateManager.translate(-0.5, -1.5, -0.5);
					break;
				case 2:
					GlStateManager.rotate(270, 0, 0, 1);
					GlStateManager.rotate(90, 0, 1, 0);
					GlStateManager.translate(-0.5, -0.5, -0.5);
					break;
				}
				isUpper = true;
				break;
			case SOUTH: // lower gear
				switch (layer)
				{
				case 0:
					GlStateManager.rotate(90, 0, 0, 1);
					GlStateManager.rotate(180, 0, 1, 0);
					GlStateManager.translate(-0.5, -1.5, -0.5);
					break;
				case 1:
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.rotate(270, 0, 1, 0);
					GlStateManager.translate(0.5, -1.5, 0.5);
					break;
				case 2:
					GlStateManager.rotate(90, 0, 0, 1);
					GlStateManager.rotate(270, 0, 1, 0);
					GlStateManager.translate(0.5, -1.5, -0.5);
					break;
				}
				break;
			case WEST: // upper gear
				switch (layer)
				{
				case 0:
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.translate(-0.5, -1.5, 0.5);
					break;
				case 1:
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.translate(-0.5, -1.5, 0.5);
					break;
				case 2:
					GlStateManager.rotate(90, 1, 0, 0);
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.translate(-0.5, -1.5, -0.5);
					break;
				}
				isUpper = true;
				break;
			case EAST: // lower gear
				switch (layer)
				{
				case 0:
					GlStateManager.rotate(90, 1, 0, 0);
					GlStateManager.rotate(270, 0, 1, 0);
					GlStateManager.translate(-0.5, -0.5, -0.5);
					break;
				case 1:
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.rotate(180, 0, 1, 0);
					GlStateManager.translate(0.5, -1.5, -0.5);
					break;
				case 2:
					GlStateManager.rotate(270, 1, 0, 0);
					GlStateManager.translate(0.5, -1.5, 0.5);
					break;
				}
				break;
			}

			GlStateManager.scale(1 / 16d, 1 / 16d, 1 / 16d);
			if (Minecraft.isAmbientOcclusionEnabled())
			{
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
			} else
			{
				GlStateManager.shadeModel(GL11.GL_FLAT);
			}
			model.Rotate(te.rotation);
			model.RenderTESR(1);
		}
		GlStateManager.popMatrix();
	}

	private void RenderGear(TEGearbox te, float posX, float posY, float posZ, float size, Gearbox.GearType type,
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
