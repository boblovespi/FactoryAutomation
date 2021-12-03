package boblovespi.factoryautomation.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Bevel_Gearbox - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class BevelGearbox extends Model
{
	public ModelPart box_front;
	public ModelPart axle_out;
	public ModelPart axle_in;
	public ModelPart box_right;
	public ModelPart box_bottom;
	public ModelPart box_left;
	public ModelPart box_back;

	public BevelGearbox()
	{
		super(RenderType::entityCutoutNoCull);
		this.texWidth = 128;
		this.texHeight = 128;
		this.box_bottom = new ModelPart(this, 0, 36);
		this.box_bottom.setPos(0.0F, 23.0F, 0.0F);
		this.box_bottom.addBox(-8.0F, 0.0F, -7.0F, 16, 1, 15, 0.0F);
		this.axle_out = new ModelPart(this, 34, 56);
		this.axle_out.setPos(0.0F, 16.0F, 0.0F);
		this.axle_out.addBox(2.5F, -1.0F, -0.95F, 6, 2, 2, 0.0F);
		this.box_right = new ModelPart(this, 0, 0);
		this.box_right.setPos(0.0F, 23.0F, 0.0F);
		this.box_right.addBox(-8.0F, -15.0F, 7.0F, 16, 16, 1, 0.0F);
		this.setRotateAngle(box_right, 0.0F, 1.5707963267948966F, 0.0F);
		this.box_front = new ModelPart(this, 0, 18);
		this.box_front.setPos(0.0F, 23.0F, 0.0F);
		this.box_front.addBox(-8.0F, -15.0F, -8.0F, 16, 16, 1, 0.0F);
		this.axle_in = new ModelPart(this, 34, 62);
		this.axle_in.setPos(0.0F, 16.0F, 0.0F);
		this.axle_in.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 6, 0.0F);
		this.box_back = new ModelPart(this, 0, 53);
		this.box_back.setPos(0.0F, 23.0F, 0.0F);
		this.box_back.addBox(7.0F, -5.0F, -7.0F, 1, 5, 15, 0.0F);
		this.setRotateAngle(box_back, 0.0F, -1.5707963267948966F, 0.0F);
		this.box_left = new ModelPart(this, 0, 74);
		this.box_left.setPos(0.0F, 23.0F, 0.0F);
		this.box_left.addBox(-8.0F, -5.0F, -7.0F, 1, 5, 14, 0.0F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha)
	{
		this.box_bottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.axle_out.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.box_right.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.box_front.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.axle_in.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.box_back.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.box_left.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	public void Rotate(float rotation, int dir1, int dir2)
	{
		setRotateAngle(axle_in, 0, 0, (float) (dir1 * Math.toRadians(rotation)));
		setRotateAngle(axle_out, (float) (dir2 * Math.toRadians(rotation)), 0, 0);
	}
}
