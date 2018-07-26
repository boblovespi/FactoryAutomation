package boblovespi.factoryautomation.client.tesr;

import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Willi on 7/25/2018.
 */

@SideOnly(Side.CLIENT)
public class TESRUtils
{
	private TESRUtils()
	{
	}

	public static TextureAtlasSprite GetFlowingTextureFromFluid(FluidStack fluid)
	{
		return ModelLoader.defaultTextureGetter().apply(fluid.getFluid().getFlowing(fluid));
	}

	public static final FaceBakery faceBakery = new FaceBakery();
}
