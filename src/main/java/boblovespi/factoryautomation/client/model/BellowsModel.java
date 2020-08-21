package boblovespi.factoryautomation.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Paper_Bellows - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class BellowsModel extends Model
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
		super(RenderType::getEntityCutoutNoCull);
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
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		this.bellowpart_4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.bellowpart_2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.bellowpart_3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.pipe.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.bellowpart_1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.bellowpart_5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
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
		// render(null, 0, 0, 0, 0, 0, scale);
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
