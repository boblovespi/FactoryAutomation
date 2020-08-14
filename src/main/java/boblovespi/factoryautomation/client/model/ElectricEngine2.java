package boblovespi.factoryautomation.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * New_Electric_Engine - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class ElectricEngine2 extends ModelBase
{
	public ModelRenderer bottom;
	public ModelRenderer front;
	public ModelRenderer left;
	public ModelRenderer right;
	public ModelRenderer back;
	public ModelRenderer top;
	public ModelRenderer bottom2;
	public ModelRenderer front2;
	public ModelRenderer left2;
	public ModelRenderer right2;
	public ModelRenderer back2;
	public ModelRenderer top2;
	public ModelRenderer axle;
	public ModelRenderer coil;

	public ElectricEngine2()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;
		this.bottom = new ModelRenderer(this, -16, 0);
		this.bottom.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.bottom.addBox(-8.0F, 0.98F, -8.0F, 16, 0, 16, 0.0F);
		this.right = new ModelRenderer(this, 0, 32);
		this.right.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.right.addBox(8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.axle = new ModelRenderer(this, 32, 39);
		this.axle.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.axle.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 17, 0.0F);
		this.back = new ModelRenderer(this, 0, 64);
		this.back.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.back.addBox(-8.0F, -15.0F, 8.0F, 16, 16, 0, 0.0F);
		this.front = new ModelRenderer(this, 0, 16);
		this.front.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.front.addBox(-8.0F, -15.0F, -8.0F, 16, 16, 0, 0.0F);
		this.coil = new ModelRenderer(this, 32, 18);
		this.coil.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.coil.addBox(-3.85F, -4.0F, -6.39F, 8, 8, 13, 0.0F);
		this.left = new ModelRenderer(this, 0, 16);
		this.left.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.left.addBox(-8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.top = new ModelRenderer(this, -16, 80);
		this.top.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.top.addBox(-8.0F, -15.0F, -8.0F, 16, 0, 16, 0.0F);

		this.bottom2 = new ModelRenderer(this, -16, 80);
		this.bottom2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.bottom2.addBox(-8.0F, 0.98F, -8.0F, 16, 0, 16, 0.0F);
		this.right2 = new ModelRenderer(this, 0, 16);
		this.right2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.right2.addBox(8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.back2 = new ModelRenderer(this, 0, 16);
		this.back2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.back2.addBox(-8.0F, -15.0F, 8.0F, 16, 16, 0, 0.0F);
		this.front2 = new ModelRenderer(this, 0, 64);
		this.front2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.front2.addBox(-8.0F, -15.0F, -8.0F, 16, 16, 0, 0.0F);
		this.left2 = new ModelRenderer(this, 0, 32);
		this.left2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.left2.addBox(-8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.top2 = new ModelRenderer(this, -16, 0);
		this.top2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.top2.addBox(-8.0F, -15.0F, -8.0F, 16, 0, 16, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.bottom.render(f5);
		this.right.render(f5);
		this.axle.render(f5);
		this.back.render(f5);
		this.front.render(f5);
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.coil.offsetX, this.coil.offsetY, this.coil.offsetZ);
		GlStateManager
				.translate(this.coil.rotationPointX * f5, this.coil.rotationPointY * f5, this.coil.rotationPointZ * f5);
		GlStateManager.scale(1.0D, 1.0D, 1.01D);
		GlStateManager.translate(-this.coil.offsetX, -this.coil.offsetY, -this.coil.offsetZ);
		GlStateManager.translate(-this.coil.rotationPointX * f5, -this.coil.rotationPointY * f5,
				-this.coil.rotationPointZ * f5);
		this.coil.render(f5);
		GlStateManager.popMatrix();
		this.left.render(f5);
		this.top.render(f5);
		this.bottom2.render(f5);
		this.right2.render(f5);
		this.back2.render(f5);
		this.front2.render(f5);
		this.left2.render(f5);
		this.top2.render(f5);
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

	public void Rotate(float rot)
	{
		setRotateAngle(axle, 0, 0, rot);
	}

	public void RenderTESR(float scale)
	{
		render(null, 0, 0, 0, 0, 0, scale);
	}
}
