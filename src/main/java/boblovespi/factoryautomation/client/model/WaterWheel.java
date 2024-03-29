package boblovespi.factoryautomation.client.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * WaterWheel - King_of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class WaterWheel {}/*extends Model
{
	public ModelPart small_plate_horizontal_top;
	public ModelPart small_plate_horizontal_bottom;
	public ModelPart small_plate_vertical_left;
	public ModelPart small_plate_vertical_right;
	public ModelPart plate_upright_to_downleft;
	public ModelPart plate_upleft_to_downright;
	public ModelPart plate_horizontal;
	public ModelPart plate_vertical;
	public ModelPart axle;

	public WaterWheel()
	{
		super(RenderType::entityCutoutNoCull);
		this.texWidth = 128;
		this.texHeight = 128;
		this.plate_upleft_to_downright = new ModelPart(this, 35, 101);
		this.plate_upleft_to_downright.setPos(0.0F, 16.0F, 0.0F);
		this.plate_upleft_to_downright.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
		this.setRotateAngle(plate_upleft_to_downright, 0.0F, 0.0F, 0.7853981633974483F);
		this.small_plate_horizontal_bottom = new ModelPart(this, 2, 29);
		this.small_plate_horizontal_bottom.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_horizontal_bottom.addBox(-5.0F, 4.76F, -8.0F, 10, 1, 16, 0.0F);
		this.small_plate_vertical_left = new ModelPart(this, 49, 3);
		this.small_plate_vertical_left.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_vertical_left.addBox(-5.7F, -5.0F, -8.0F, 1, 10, 16, 0.0F);
		this.plate_vertical = new ModelPart(this, 0, 82);
		this.plate_vertical.setPos(0.0F, 16.0F, 0.0F);
		this.plate_vertical.addBox(-0.47F, -15.0F, -8.0F, 1, 30, 16, 0.0F);
		this.axle = new ModelPart(this, 3, 2);
		this.axle.setPos(0.0F, 16.0F, 0.0F);
		this.axle.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 17, 0.0F);
		this.small_plate_horizontal_top = new ModelPart(this, 2, 29);
		this.small_plate_horizontal_top.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_horizontal_top.addBox(-5.0F, -5.47F, -8.0F, 10, 1, 16, 0.0F);
		this.small_plate_vertical_right = new ModelPart(this, 49, 3);
		this.small_plate_vertical_right.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_vertical_right.addBox(4.7F, -5.0F, -8.0F, 1, 10, 16, 0.0F);
		this.plate_horizontal = new ModelPart(this, 35, 101);
		this.plate_horizontal.setPos(0.0F, 16.0F, 0.0F);
		this.plate_horizontal.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
		this.plate_upright_to_downleft = new ModelPart(this, 35, 101);
		this.plate_upright_to_downleft.setPos(0.0F, 16.0F, 0.0F);
		this.plate_upright_to_downleft.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
		this.setRotateAngle(plate_upright_to_downleft, 0.0F, 0.0F, -0.7853981633974483F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha)
	{
		this.plate_upleft_to_downright
				.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.small_plate_horizontal_bottom
				.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.small_plate_vertical_left
				.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.plate_vertical.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.axle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.small_plate_horizontal_top
				.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.small_plate_vertical_right
				.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.plate_horizontal.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.plate_upright_to_downleft
				.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	*//**
	 * This is a helper function from Tabula to set the rotation of model parts
	 *//*
	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
*/