package example_tabula;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * gearbox_2 - King_of_Creepers
 * Created using Tabula 7.0.0
 */
public class gearbox_2 extends ModelBase {
    public ModelRenderer frame;
    public ModelRenderer frame_1;
    public ModelRenderer frame_2;
    public ModelRenderer frame_3;
    public ModelRenderer frame_4;
    public ModelRenderer frame_5;
    public ModelRenderer jack;
    public ModelRenderer axis;
    public ModelRenderer jack_plate;
    public ModelRenderer jack_1;
    public ModelRenderer jack_2;
    public ModelRenderer frame_6;
    public ModelRenderer frame_7;
    public ModelRenderer frame_8;
    public ModelRenderer frame_9;
    public ModelRenderer frame_10;
    public ModelRenderer jack_3;
    public ModelRenderer jack_4;
    public ModelRenderer frame_11;
    public ModelRenderer frame_12;
    public ModelRenderer frame_13;
    public ModelRenderer plate_bottom;
    public ModelRenderer plate_bottom_1;
    public ModelRenderer frame_14;
    public ModelRenderer frame_15;
    public ModelRenderer frame_16;
    public ModelRenderer frame_17;
    public ModelRenderer frame_18;
    public ModelRenderer frame_19;

    public gearbox_2() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.frame_7 = new ModelRenderer(this, 0, 32);
        this.frame_7.setRotationPoint(-8.0F, 9.0F, -8.0F);
        this.frame_7.addBox(0.0F, 2.0F, 0.0F, 1, 12, 1, 0.0F);
        this.frame_12 = new ModelRenderer(this, 0, 32);
        this.frame_12.setRotationPoint(-0.2F, 16.0F, 0.3F);
        this.frame_12.addBox(-0.3F, -7.0F, -0.55F, 1, 15, 1, 0.0F);
        this.axis = new ModelRenderer(this, 32, 32);
        this.axis.setRotationPoint(0.0F, 16.0F, -0.6F);
        this.axis.addBox(-0.5F, -0.5F, -7.0F, 1, 1, 5, 0.0F);
        this.frame_1 = new ModelRenderer(this, 29, 47);
        this.frame_1.setRotationPoint(-8.0F, 8.0F, -8.0F);
        this.frame_1.addBox(0.0F, 2.0F, 0.0F, 16, 1, 1, 0.0F);
        this.frame_8 = new ModelRenderer(this, 0, 49);
        this.frame_8.setRotationPoint(-8.0F, 8.0F, -13.0F);
        this.frame_8.addBox(0.0F, 2.0F, 6.0F, 1, 1, 14, 0.0F);
        this.frame_4 = new ModelRenderer(this, 0, 49);
        this.frame_4.setRotationPoint(7.0F, 23.0F, -13.0F);
        this.frame_4.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
        this.jack_4 = new ModelRenderer(this, 0, 0);
        this.jack_4.setRotationPoint(0.0F, 16.1F, -0.6F);
        this.jack_4.addBox(1.5F, -0.6F, -8.5F, 1, 1, 3, 0.0F);
        this.jack_3 = new ModelRenderer(this, 0, 0);
        this.jack_3.setRotationPoint(0.0F, 16.1F, -0.6F);
        this.jack_3.addBox(1.5F, -0.6F, -8.5F, 1, 1, 3, 0.0F);
        this.frame_14 = new ModelRenderer(this, 0, 32);
        this.frame_14.setRotationPoint(7.0F, 9.0F, 7.0F);
        this.frame_14.addBox(0.0F, 2.0F, 0.0F, 1, 12, 1, 0.0F);
        this.frame_16 = new ModelRenderer(this, 0, 32);
        this.frame_16.setRotationPoint(7.0F, 9.1F, 8.4F);
        this.frame_16.addBox(0.0F, 2.0F, 0.0F, 1, 19, 1, 0.0F);
        this.setRotateAngle(frame_16, -0.8426449628628624F, 0.0F, 0.0F);
        this.frame_3 = new ModelRenderer(this, 0, 32);
        this.frame_3.setRotationPoint(-3.0F, 8.5F, -5.6F);
        this.frame_3.addBox(0.0F, 2.0F, 0.0F, 1, 14, 1, 0.0F);
        this.frame_2 = new ModelRenderer(this, 0, 32);
        this.frame_2.setRotationPoint(-8.0F, 9.7F, -9.0F);
        this.frame_2.addBox(0.0F, 2.0F, 0.0F, 1, 19, 1, 0.0F);
        this.setRotateAngle(frame_2, 0.8471828189180476F, 0.0F, 0.0F);
        this.jack_plate = new ModelRenderer(this, 0, 5);
        this.jack_plate.setRotationPoint(0.0F, 16.0F, -0.6F);
        this.jack_plate.addBox(-1.5F, -1.5F, -6.5F, 3, 3, 1, 0.0F);
        this.frame_13 = new ModelRenderer(this, 0, 32);
        this.frame_13.setRotationPoint(-8.0F, 9.0F, 7.0F);
        this.frame_13.addBox(0.0F, 2.0F, 0.0F, 1, 12, 1, 0.0F);
        this.plate_bottom_1 = new ModelRenderer(this, 22, 50);
        this.plate_bottom_1.setRotationPoint(-7.0F, 23.9F, -7.0F);
        this.plate_bottom_1.addBox(0.0F, 0.0F, 0.0F, 14, 0, 14, 0.0F);
        this.jack = new ModelRenderer(this, 0, 0);
        this.jack.setRotationPoint(0.0F, 16.1F, -0.6F);
        this.jack.addBox(1.5F, -0.6F, -8.5F, 1, 1, 3, 0.0F);
        this.setRotateAngle(jack, 0.0F, 0.0F, -3.141592653589793F);
        this.frame_15 = new ModelRenderer(this, 0, 32);
        this.frame_15.setRotationPoint(-9.0F, 10.0F, 6.9F);
        this.frame_15.addBox(0.0F, 2.0F, 0.0F, 1, 19, 1, 0.0F);
        this.setRotateAngle(frame_15, 0.0F, 0.0F, -0.8674286382411819F);
        this.frame_5 = new ModelRenderer(this, 29, 47);
        this.frame_5.setRotationPoint(-8.0F, 23.0F, 7.0F);
        this.frame_5.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
        this.jack_2 = new ModelRenderer(this, 0, 0);
        this.jack_2.setRotationPoint(0.0F, 16.1F, -0.6F);
        this.jack_2.addBox(-0.5F, 1.4F, -8.5F, 1, 1, 3, 0.0F);
        this.frame_9 = new ModelRenderer(this, 29, 47);
        this.frame_9.setRotationPoint(-2.0F, 15.4F, -5.5F);
        this.frame_9.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
        this.jack_1 = new ModelRenderer(this, 0, 0);
        this.jack_1.setRotationPoint(0.0F, 16.1F, -0.6F);
        this.jack_1.addBox(-0.5F, -2.5F, -8.5F, 1, 1, 3, 0.0F);
        this.plate_bottom = new ModelRenderer(this, 22, 50);
        this.plate_bottom.setRotationPoint(-7.0F, 10.4F, -7.0F);
        this.plate_bottom.addBox(0.0F, 0.0F, 0.0F, 14, 0, 14, 0.0F);
        this.frame_11 = new ModelRenderer(this, 55, 5);
        this.frame_11.setRotationPoint(-0.2F, 16.0F, 0.3F);
        this.frame_11.addBox(-0.3F, -8.0F, -0.55F, 1, 1, 1, 0.0F);
        this.frame_17 = new ModelRenderer(this, 0, 32);
        this.frame_17.setRotationPoint(7.0F, 9.0F, -8.0F);
        this.frame_17.addBox(0.0F, 2.0F, 0.0F, 1, 12, 1, 0.0F);
        this.frame_10 = new ModelRenderer(this, 0, 49);
        this.frame_10.setRotationPoint(7.0F, 8.0F, -13.0F);
        this.frame_10.addBox(0.0F, 2.0F, 6.0F, 1, 1, 14, 0.0F);
        this.frame_18 = new ModelRenderer(this, 0, 32);
        this.frame_18.setRotationPoint(2.0F, 8.5F, -5.6F);
        this.frame_18.addBox(0.0F, 2.0F, 0.0F, 1, 14, 1, 0.0F);
        this.frame_6 = new ModelRenderer(this, 0, 49);
        this.frame_6.setRotationPoint(-8.0F, 23.0F, -13.0F);
        this.frame_6.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
        this.frame_19 = new ModelRenderer(this, 29, 47);
        this.frame_19.setRotationPoint(-8.0F, 23.0F, -8.0F);
        this.frame_19.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
        this.frame = new ModelRenderer(this, 29, 47);
        this.frame.setRotationPoint(-8.0F, 8.0F, 7.0F);
        this.frame.addBox(0.0F, 2.0F, 0.0F, 16, 1, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.frame_7.render(f5);
        this.frame_12.render(f5);
        this.axis.render(f5);
        this.frame_1.render(f5);
        this.frame_8.render(f5);
        this.frame_4.render(f5);
        this.jack_4.render(f5);
        this.jack_3.render(f5);
        this.frame_14.render(f5);
        this.frame_16.render(f5);
        this.frame_3.render(f5);
        this.frame_2.render(f5);
        this.jack_plate.render(f5);
        this.frame_13.render(f5);
        this.plate_bottom_1.render(f5);
        this.jack.render(f5);
        this.frame_15.render(f5);
        this.frame_5.render(f5);
        this.jack_2.render(f5);
        this.frame_9.render(f5);
        this.jack_1.render(f5);
        this.plate_bottom.render(f5);
        this.frame_11.render(f5);
        this.frame_17.render(f5);
        this.frame_10.render(f5);
        this.frame_18.render(f5);
        this.frame_6.render(f5);
        this.frame_19.render(f5);
        this.frame.render(f5);
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
