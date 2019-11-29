package boblovespi.factoryautomation.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Bevel_Gearbox - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
@SideOnly(Side.CLIENT)
public class BevelGearbox extends ModelBase
{
	public ModelRenderer box_front;
	public ModelRenderer axle_out;
	public ModelRenderer axle_in;
	public ModelRenderer box_right;
	public ModelRenderer box_bottom;
	public ModelRenderer box_left;
	public ModelRenderer box_back;

	public BevelGearbox()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;
		this.box_bottom = new ModelRenderer(this, 0, 36);
		this.box_bottom.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.box_bottom.addBox(-8.0F, 0.0F, -7.0F, 16, 1, 15, 0.0F);
		this.axle_out = new ModelRenderer(this, 34, 56);
		this.axle_out.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.axle_out.addBox(2.5F, -1.0F, -0.95F, 6, 2, 2, 0.0F);
		this.box_right = new ModelRenderer(this, 0, 0);
		this.box_right.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.box_right.addBox(-8.0F, -15.0F, 7.0F, 16, 16, 1, 0.0F);
		this.setRotateAngle(box_right, 0.0F, 1.5707963267948966F, 0.0F);
		this.box_front = new ModelRenderer(this, 0, 18);
		this.box_front.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.box_front.addBox(-8.0F, -15.0F, -8.0F, 16, 16, 1, 0.0F);
		this.axle_in = new ModelRenderer(this, 34, 62);
		this.axle_in.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.axle_in.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 6, 0.0F);
		this.box_back = new ModelRenderer(this, 0, 53);
		this.box_back.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.box_back.addBox(7.0F, -5.0F, -7.0F, 1, 5, 15, 0.0F);
		this.setRotateAngle(box_back, 0.0F, -1.5707963267948966F, 0.0F);
		this.box_left = new ModelRenderer(this, 0, 74);
		this.box_left.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.box_left.addBox(-8.0F, -5.0F, -7.0F, 1, 5, 14, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.box_bottom.render(f5);
		this.axle_out.render(f5);
		this.box_right.render(f5);
		this.box_front.render(f5);
		this.axle_in.render(f5);
		this.box_back.render(f5);
		this.box_left.render(f5);
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

	public void Rotate(float rotation, int dir1, int dir2)
	{
		setRotateAngle(axle_in, 0, 0, (float) (dir1 * Math.toRadians(rotation)));
		setRotateAngle(axle_out, (float) (dir2 * Math.toRadians(rotation)), 0, 0);
	}
}
