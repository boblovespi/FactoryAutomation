package boblovespi.factoryautomation.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Paper_Bellows - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class BellowsModel extends ModelBase
{
	public ModelRenderer bellowpart_1;
	public ModelRenderer bellowpart_2;
	public ModelRenderer bellowpart_3;
	public ModelRenderer bellowpart_4;
	public ModelRenderer bellowpart_5;
	public ModelRenderer pipe;
	public ModelRenderer top;
	public ModelRenderer base;

	public BellowsModel()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;
		this.bellowpart_4 = new ModelRenderer(this, 2, 22);
		this.bellowpart_4.setRotationPoint(0.0F, 21.0F, -8.0F);
		this.bellowpart_4.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_4, 0.40997784129346804F, 0.0F, 0.0F);
		this.bellowpart_2 = new ModelRenderer(this, 2, 22);
		this.bellowpart_2.setRotationPoint(0.0F, 21.0F, -8.0F);
		this.bellowpart_2.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_2, 0.136659280431156F, 0.0F, 0.0F);
		this.bellowpart_3 = new ModelRenderer(this, 2, 22);
		this.bellowpart_3.setRotationPoint(0.0F, 21.0F, -8.0F);
		this.bellowpart_3.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_3, 0.273318560862312F, 0.0F, 0.0F);
		this.pipe = new ModelRenderer(this, 7, 43);
		this.pipe.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.pipe.addBox(-1.5F, -1.5F, -12.0F, 3, 2, 5, 0.0F);
		this.top = new ModelRenderer(this, 0, 0);
		this.top.setRotationPoint(0.0F, 21.0F, -8.0F);
		this.top.addBox(-8.0F, -3.0F, 0.0F, 16, 3, 16, 0.0F);
		this.setRotateAngle(top, 0.6373942428283291F, 0.0F, 0.0F);
		this.base = new ModelRenderer(this, 0, 0);
		this.base.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.base.addBox(-8.0F, -2.0F, -8.0F, 16, 3, 16, 0.0F);
		this.bellowpart_1 = new ModelRenderer(this, 2, 22);
		this.bellowpart_1.setRotationPoint(0.0F, 21.0F, -8.0F);
		this.bellowpart_1.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.bellowpart_5 = new ModelRenderer(this, 2, 22);
		this.bellowpart_5.setRotationPoint(0.0F, 21.0F, -8.0F);
		this.bellowpart_5.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_5, 0.546637121724624F, 0.0F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.bellowpart_4.render(f5);
		this.bellowpart_2.render(f5);
		this.bellowpart_3.render(f5);
		this.pipe.render(f5);
		this.top.render(f5);
		this.base.render(f5);
		this.bellowpart_1.render(f5);
		this.bellowpart_5.render(f5);
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
		render(null, 0, 0, 0, 0, 0, scale);
	}

	public void Rotate(float lerp)
	{
		setRotateAngle(bellowpart_1, (float) MathHelper.clampedLerp(0, 0, lerp), 0, 0);
		setRotateAngle(bellowpart_2, (float) MathHelper.clampedLerp(0, 0.136659280431156F, lerp), 0, 0);
		setRotateAngle(bellowpart_3, (float) MathHelper.clampedLerp(0, 0.273318560862312F, lerp), 0, 0);
		setRotateAngle(bellowpart_4, (float) MathHelper.clampedLerp(0, 0.40997784129346804F, lerp), 0, 0);
		setRotateAngle(bellowpart_5, (float) MathHelper.clampedLerp(0, 0.546637121724624F, lerp), 0, 0);
		setRotateAngle(top, (float) MathHelper.clampedLerp(0, 0.6373942428283291F, lerp), 0, 0);
	}
}
