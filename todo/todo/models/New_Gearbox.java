package insert_package_here;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * New_Gearbox - King_Of_Creepers
 * Created using Tabula 7.0.1
 */
public class New_Gearbox extends ModelBase {
    public ModelRenderer box_front;
    public ModelRenderer axel_out;
    public ModelRenderer axel_transfer;
    public ModelRenderer axel_in;
    public ModelRenderer box_back;
    public ModelRenderer box_bottom;
    public ModelRenderer box_left;
    public ModelRenderer box_right;

    public New_Gearbox() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.box_left = new ModelRenderer(this, 0, 72);
        this.box_left.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.box_left.addBox(-8.0F, -5.0F, -7.0F, 1, 5, 14, 0.0F);
        this.box_front = new ModelRenderer(this, 0, 18);
        this.box_front.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.box_front.addBox(-8.0F, -15.0F, -8.0F, 16, 16, 1, 0.0F);
        this.axel_transfer = new ModelRenderer(this, 32, 69);
        this.axel_transfer.setRotationPoint(0.0F, 10.9F, 0.0F);
        this.axel_transfer.addBox(-0.95F, -1.0F, -7.0F, 2, 2, 14, 0.0F);
        this.box_bottom = new ModelRenderer(this, 0, 36);
        this.box_bottom.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.box_bottom.addBox(-8.0F, 0.0F, -7.0F, 16, 1, 14, 0.0F);
        this.box_right = new ModelRenderer(this, 0, 52);
        this.box_right.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.box_right.addBox(7.0F, -5.0F, -7.0F, 1, 5, 14, 0.0F);
        this.box_back = new ModelRenderer(this, 0, 0);
        this.box_back.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.box_back.addBox(-8.0F, -15.0F, 7.0F, 16, 16, 1, 0.0F);
        this.axel_in = new ModelRenderer(this, 32, 62);
        this.axel_in.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.axel_in.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 4, 0.0F);
        this.axel_out = new ModelRenderer(this, 32, 56);
        this.axel_out.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.axel_out.addBox(-0.95F, -1.0F, 4.6F, 2, 2, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.box_left.render(f5);
        this.box_front.render(f5);
        this.axel_transfer.render(f5);
        this.box_bottom.render(f5);
        this.box_right.render(f5);
        this.box_back.render(f5);
        this.axel_in.render(f5);
        this.axel_out.render(f5);
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
