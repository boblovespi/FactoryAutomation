package boblovespi.factoryautomation.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * New_Electric_Engine - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class ElectricEngine2 extends Model
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
		super(RenderType::entityCutoutNoCull);
		this.texWidth = 128;
		this.texHeight = 128;
		this.bottom = new ModelRenderer(this, -16, 0);
		this.bottom.setPos(0.0F, 23.0F, 0.0F);
		this.bottom.addBox(-8.0F, 0.98F, -8.0F, 16, 0, 16, 0.0F);
		this.right = new ModelRenderer(this, 0, 32);
		this.right.setPos(0.0F, 23.0F, 0.0F);
		this.right.addBox(8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.axle = new ModelRenderer(this, 32, 39);
		this.axle.setPos(0.0F, 16.0F, 0.0F);
		this.axle.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 17, 0.0F);
		this.back = new ModelRenderer(this, 0, 64);
		this.back.setPos(0.0F, 23.0F, 0.0F);
		this.back.addBox(-8.0F, -15.0F, 8.0F, 16, 16, 0, 0.0F);
		this.front = new ModelRenderer(this, 0, 16);
		this.front.setPos(0.0F, 23.0F, 0.0F);
		this.front.addBox(-8.0F, -15.0F, -8.0F, 16, 16, 0, 0.0F);
		this.coil = new ModelRenderer(this, 32, 18);
		this.coil.setPos(0.0F, 16.0F, 0.0F);
		this.coil.addBox(-3.85F, -4.0F, -6.39F, 8, 8, 13, 0.0F);
		this.left = new ModelRenderer(this, 0, 16);
		this.left.setPos(0.0F, 23.0F, 0.0F);
		this.left.addBox(-8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.top = new ModelRenderer(this, -16, 80);
		this.top.setPos(0.0F, 23.0F, 0.0F);
		this.top.addBox(-8.0F, -15.0F, -8.0F, 16, 0, 16, 0.0F);

		this.bottom2 = new ModelRenderer(this, -16, 80);
		this.bottom2.setPos(0.0F, 23.0F, 0.0F);
		this.bottom2.addBox(-8.0F, 0.98F, -8.0F, 16, 0, 16, 0.0F);
		this.right2 = new ModelRenderer(this, 0, 16);
		this.right2.setPos(0.0F, 23.0F, 0.0F);
		this.right2.addBox(8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.back2 = new ModelRenderer(this, 0, 16);
		this.back2.setPos(0.0F, 23.0F, 0.0F);
		this.back2.addBox(-8.0F, -15.0F, 8.0F, 16, 16, 0, 0.0F);
		this.front2 = new ModelRenderer(this, 0, 64);
		this.front2.setPos(0.0F, 23.0F, 0.0F);
		this.front2.addBox(-8.0F, -15.0F, -8.0F, 16, 16, 0, 0.0F);
		this.left2 = new ModelRenderer(this, 0, 32);
		this.left2.setPos(0.0F, 23.0F, 0.0F);
		this.left2.addBox(-8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
		this.top2 = new ModelRenderer(this, -16, 0);
		this.top2.setPos(0.0F, 23.0F, 0.0F);
		this.top2.addBox(-8.0F, -15.0F, -8.0F, 16, 0, 16, 0.0F);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha)
	{
		this.bottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.right.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.axle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.back.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.front.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		// matrixStackIn.push();
		// matrixStackIn.translate(this.coil.offsetX, this.coil.offsetY, this.coil.offsetZ);
		// RenderSystem
		// 		.translate(this.coil.rotationPointX * f5, this.coil.rotationPointY * f5, this.coil.rotationPointZ * f5);
		// GlStateManager.scale(1.0D, 1.0D, 1.01D);
		// GlStateManager.translate(-this.coil.offsetX, -this.coil.offsetY, -this.coil.offsetZ);
		// GlStateManager.translate(-this.coil.rotationPointX * f5, -this.coil.rotationPointY * f5,
		// 		-this.coil.rotationPointZ * f5);
		this.coil.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		// GlStateManager.popMatrix();
		this.left.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.bottom2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.right2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.back2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.front2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.left2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.top2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
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

	public void Rotate(float rot)
	{
		setRotateAngle(axle, 0, 0, rot);
	}
}
