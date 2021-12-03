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
 * hand_crank - King_of_Creepers
 * Created using Tabula 7.0.0
 */
@OnlyIn(Dist.CLIENT)
public class HandCrankModel extends Model
{
	public ModelPart shape1;
	public ModelPart shape1_1;

	public HandCrankModel()
	{
		super(RenderType::entityCutoutNoCull);
		this.texWidth = 64;
		this.texHeight = 32;
		this.shape1 = new ModelPart(this, 8, 0);
		this.shape1.setPos(0.0F, 12.0F, 0.0F);
		this.shape1.addBox(-1.0F, -1.0F, -1.0F, 8, 2, 2, 0.0F);
		this.shape1_1 = new ModelPart(this, 0, 0);
		this.shape1_1.setPos(0.0F, 12.0F, 0.0F);
		this.shape1_1.addBox(-1.0F, 1.0F, -1.0F, 2, 11, 2, 0.0F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha)
	{
		this.shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.shape1_1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
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
}
