package boblovespi.factoryautomation.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * WaterWheel - King_of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class WaterWheel extends Model
{
	public ModelRenderer small_plate_horizontal_top;
	public ModelRenderer small_plate_horizontal_bottom;
	public ModelRenderer small_plate_vertical_left;
	public ModelRenderer small_plate_vertical_right;
	public ModelRenderer plate_upright_to_downleft;
	public ModelRenderer plate_upleft_to_downright;
	public ModelRenderer plate_horizontal;
	public ModelRenderer plate_vertical;
	public ModelRenderer axle;

	public WaterWheel()
	{
		super(RenderType::entityCutoutNoCull);
		this.texWidth = 128;
		this.texHeight = 128;
		this.plate_upleft_to_downright = new ModelRenderer(this, 35, 101);
		this.plate_upleft_to_downright.setPos(0.0F, 16.0F, 0.0F);
		this.plate_upleft_to_downright.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
		this.setRotateAngle(plate_upleft_to_downright, 0.0F, 0.0F, 0.7853981633974483F);
		this.small_plate_horizontal_bottom = new ModelRenderer(this, 2, 29);
		this.small_plate_horizontal_bottom.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_horizontal_bottom.addBox(-5.0F, 4.76F, -8.0F, 10, 1, 16, 0.0F);
		this.small_plate_vertical_left = new ModelRenderer(this, 49, 3);
		this.small_plate_vertical_left.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_vertical_left.addBox(-5.7F, -5.0F, -8.0F, 1, 10, 16, 0.0F);
		this.plate_vertical = new ModelRenderer(this, 0, 82);
		this.plate_vertical.setPos(0.0F, 16.0F, 0.0F);
		this.plate_vertical.addBox(-0.47F, -15.0F, -8.0F, 1, 30, 16, 0.0F);
		this.axle = new ModelRenderer(this, 3, 2);
		this.axle.setPos(0.0F, 16.0F, 0.0F);
		this.axle.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 17, 0.0F);
		this.small_plate_horizontal_top = new ModelRenderer(this, 2, 29);
		this.small_plate_horizontal_top.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_horizontal_top.addBox(-5.0F, -5.47F, -8.0F, 10, 1, 16, 0.0F);
		this.small_plate_vertical_right = new ModelRenderer(this, 49, 3);
		this.small_plate_vertical_right.setPos(0.0F, 16.0F, 0.0F);
		this.small_plate_vertical_right.addBox(4.7F, -5.0F, -8.0F, 1, 10, 16, 0.0F);
		this.plate_horizontal = new ModelRenderer(this, 35, 101);
		this.plate_horizontal.setPos(0.0F, 16.0F, 0.0F);
		this.plate_horizontal.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
		this.plate_upright_to_downleft = new ModelRenderer(this, 35, 101);
		this.plate_upright_to_downleft.setPos(0.0F, 16.0F, 0.0F);
		this.plate_upright_to_downleft.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
		this.setRotateAngle(plate_upright_to_downleft, 0.0F, 0.0F, -0.7853981633974483F);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
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

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
