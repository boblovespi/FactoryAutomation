package insert_class_here;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * New_Electric_Engine - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
public class New_Electric_Engine extends ModelBase {
    public ModelRenderer bottom;
    public ModelRenderer front;
    public ModelRenderer left;
    public ModelRenderer right;
    public ModelRenderer back;
    public ModelRenderer top;
    public ModelRenderer axel;
    public ModelRenderer coil;

    public New_Electric_Engine() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.bottom = new ModelRenderer(this, -16, 0);
        this.bottom.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.bottom.addBox(-8.0F, 0.98F, -8.0F, 16, 0, 16, 0.0F);
        this.right = new ModelRenderer(this, 0, 32);
        this.right.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.right.addBox(8.0F, -15.0F, -8.0F, 0, 16, 16, 0.0F);
        this.axel = new ModelRenderer(this, 32, 39);
        this.axel.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.axel.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 17, 0.0F);
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
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.bottom.render(f5);
        this.right.render(f5);
        this.axel.render(f5);
        this.back.render(f5);
        this.front.render(f5);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.coil.offsetX, this.coil.offsetY, this.coil.offsetZ);
        GlStateManager.translate(this.coil.rotationPointX * f5, this.coil.rotationPointY * f5, this.coil.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 1.0D, 1.01D);
        GlStateManager.translate(-this.coil.offsetX, -this.coil.offsetY, -this.coil.offsetZ);
        GlStateManager.translate(-this.coil.rotationPointX * f5, -this.coil.rotationPointY * f5, -this.coil.rotationPointZ * f5);
        this.coil.render(f5);
        GlStateManager.popMatrix();
        this.left.render(f5);
        this.top.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
