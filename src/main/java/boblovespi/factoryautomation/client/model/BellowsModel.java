package boblovespi.factoryautomation.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Paper_Bellows - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class BellowsModel extends Model
{
	public ModelPart bellowpart_1;
	public ModelPart bellowpart_2;
	public ModelPart bellowpart_3;
	public ModelPart bellowpart_4;
	public ModelPart bellowpart_5;
	public ModelPart pipe;
	public ModelPart top;
	public ModelPart base;

	public BellowsModel()
	{
		super(RenderType::entityCutoutNoCull);
		this.texWidth = 128;
		this.texHeight = 128;
		this.bellowpart_4 = new ModelPart(this, 2, 22);
		this.bellowpart_4.setPos(0.0F, 21.0F, -8.0F);
		this.bellowpart_4.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_4, 0.40997784129346804F, 0.0F, 0.0F);
		this.bellowpart_2 = new ModelPart(this, 2, 22);
		this.bellowpart_2.setPos(0.0F, 21.0F, -8.0F);
		this.bellowpart_2.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_2, 0.136659280431156F, 0.0F, 0.0F);
		this.bellowpart_3 = new ModelPart(this, 2, 22);
		this.bellowpart_3.setPos(0.0F, 21.0F, -8.0F);
		this.bellowpart_3.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_3, 0.273318560862312F, 0.0F, 0.0F);
		this.pipe = new ModelPart(this, 7, 43);
		this.pipe.setPos(0.0F, 23.0F, 0.0F);
		this.pipe.addBox(-1.5F, -1.5F, -12.0F, 3, 2, 5, 0.0F);
		this.top = new ModelPart(this, 0, 0);
		this.top.setPos(0.0F, 21.0F, -8.0F);
		this.top.addBox(-8.0F, -3.0F, 0.0F, 16, 3, 16, 0.0F);
		this.setRotateAngle(top, 0.6373942428283291F, 0.0F, 0.0F);
		this.base = new ModelPart(this, 0, 0);
		this.base.setPos(0.0F, 23.0F, 0.0F);
		this.base.addBox(-8.0F, -2.0F, -8.0F, 16, 3, 16, 0.0F);
		this.bellowpart_1 = new ModelPart(this, 2, 22);
		this.bellowpart_1.setPos(0.0F, 21.0F, -8.0F);
		this.bellowpart_1.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.bellowpart_5 = new ModelPart(this, 2, 22);
		this.bellowpart_5.setPos(0.0F, 21.0F, -8.0F);
		this.bellowpart_5.addBox(-7.0F, -2.0F, 0.0F, 14, 2, 15, 0.0F);
		this.setRotateAngle(bellowpart_5, 0.546637121724624F, 0.0F, 0.0F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
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
	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	public void RenderTESR(float scale)
	{
		// render(null, 0, 0, 0, 0, 0, scale);
	}

	public void Rotate(float lerp)
	{
		setRotateAngle(bellowpart_1, (float) Mth.clampedLerp(0, 0, lerp), 0, 0);
		setRotateAngle(bellowpart_2, (float) Mth.clampedLerp(0, 0.136659280431156F, lerp), 0, 0);
		setRotateAngle(bellowpart_3, (float) Mth.clampedLerp(0, 0.273318560862312F, lerp), 0, 0);
		setRotateAngle(bellowpart_4, (float) Mth.clampedLerp(0, 0.40997784129346804F, lerp), 0, 0);
		setRotateAngle(bellowpart_5, (float) Mth.clampedLerp(0, 0.546637121724624F, lerp), 0, 0);
		setRotateAngle(top, (float) Mth.clampedLerp(0, 0.6373942428283291F, lerp), 0, 0);
	}
}
