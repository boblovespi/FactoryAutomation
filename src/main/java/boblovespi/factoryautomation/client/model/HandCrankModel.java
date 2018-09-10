package boblovespi.factoryautomation.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * hand_crank - King_of_Creepers
 * Created using Tabula 7.0.0
 */
@SideOnly(Side.CLIENT)
public class HandCrankModel extends ModelBase
{
	public ModelRenderer shape1;
	public ModelRenderer shape1_1;

	public HandCrankModel()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.shape1 = new ModelRenderer(this, 8, 0);
		this.shape1.setRotationPoint(0.0F, 12.0F, 0.0F);
		this.shape1.addBox(-1.0F, -1.0F, -1.0F, 8, 2, 2, 0.0F);
		this.shape1_1 = new ModelRenderer(this, 0, 0);
		this.shape1_1.setRotationPoint(0.0F, 12.0F, 0.0F);
		this.shape1_1.addBox(-1.0F, 1.0F, -1.0F, 2, 11, 2, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.shape1.render(f5);
		this.shape1_1.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void RenderTESR(float scale)
	{
		shape1.render(scale);
		shape1_1.render(scale);
	}
}
