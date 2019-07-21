package insert_class_here;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Water_Wheel - King_of_Creepers
 * Created using Tabula 7.0.1
 */
public class Water_Wheel extends ModelBase {
    public ModelRenderer small_plate_horizontal_top;
    public ModelRenderer small_plate_horizontal_bottom;
    public ModelRenderer small_plate_vertical_left;
    public ModelRenderer small_plate_vertical_right;
    public ModelRenderer plate_upright_to_downleft;
    public ModelRenderer plate_upleft_to_downright;
    public ModelRenderer plate_horizontal;
    public ModelRenderer plate_vertical;
    public ModelRenderer axel;

    public Water_Wheel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.plate_upleft_to_downright = new ModelRenderer(this, 35, 101);
        this.plate_upleft_to_downright.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.plate_upleft_to_downright.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
        this.setRotateAngle(plate_upleft_to_downright, 0.0F, 0.0F, 0.7853981633974483F);
        this.small_plate_horizontal_bottom = new ModelRenderer(this, 2, 29);
        this.small_plate_horizontal_bottom.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.small_plate_horizontal_bottom.addBox(-5.0F, 4.76F, -8.0F, 10, 1, 16, 0.0F);
        this.small_plate_vertical_left = new ModelRenderer(this, 49, 3);
        this.small_plate_vertical_left.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.small_plate_vertical_left.addBox(-5.7F, -5.0F, -8.0F, 1, 10, 16, 0.0F);
        this.plate_vertical = new ModelRenderer(this, 0, 82);
        this.plate_vertical.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.plate_vertical.addBox(-0.47F, -15.0F, -8.0F, 1, 30, 16, 0.0F);
        this.axel = new ModelRenderer(this, 3, 2);
        this.axel.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.axel.addBox(-0.95F, -1.0F, -8.5F, 2, 2, 17, 0.0F);
        this.small_plate_horizontal_top = new ModelRenderer(this, 2, 29);
        this.small_plate_horizontal_top.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.small_plate_horizontal_top.addBox(-5.0F, -5.47F, -8.0F, 10, 1, 16, 0.0F);
        this.small_plate_vertical_right = new ModelRenderer(this, 49, 3);
        this.small_plate_vertical_right.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.small_plate_vertical_right.addBox(4.7F, -5.0F, -8.0F, 1, 10, 16, 0.0F);
        this.plate_horizontal = new ModelRenderer(this, 35, 101);
        this.plate_horizontal.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.plate_horizontal.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
        this.plate_upright_to_downleft = new ModelRenderer(this, 35, 101);
        this.plate_upright_to_downleft.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.plate_upright_to_downleft.addBox(-15.0F, -0.47F, -8.0F, 30, 1, 16, 0.0F);
        this.setRotateAngle(plate_upright_to_downleft, 0.0F, 0.0F, -0.7853981633974483F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.plate_upleft_to_downright.render(f5);
        this.small_plate_horizontal_bottom.render(f5);
        this.small_plate_vertical_left.render(f5);
        this.plate_vertical.render(f5);
        this.axel.render(f5);
        this.small_plate_horizontal_top.render(f5);
        this.small_plate_vertical_right.render(f5);
        this.plate_horizontal.render(f5);
        this.plate_upright_to_downleft.render(f5);
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
