package boblovespi.factoryautomation.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class GuiUtils
{
	public static void Blit(PoseStack matrix, int x, int y, int z, int w, int h, TextureAtlasSprite sprite)
	{
		GuiComponent.blit(matrix, x, y, z, w, h, sprite);
	}

	public static void Blit(PoseStack matrix, int x, int y, int z, int w, int h, TextureAtlasSprite sprite,
							float wPercent, float hPercent)
	{
		GuiComponent.innerBlit(matrix.last()
									   .pose(), x, (int) (x + w * wPercent), y, (int) (y + h * hPercent), z, sprite.getU0(), sprite.getU(wPercent * 16), sprite.getV0(), sprite.getV(hPercent * 16));
	}

	public static void Blit(PoseStack matrix, int x, int y, int z, TextureAtlasSprite sprite, float wPix, float hPix)
	{
		Blit(matrix, x, y, z, sprite.getWidth(), sprite.getHeight(), sprite, wPix / sprite.getWidth(), hPix / sprite.getHeight());
	}
}
