package horse_power_shaft;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * horse_power_pillar - King_of_Creepers
 * Created using Tabula 7.0.0
 */
public class horse_power_pillar extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape1_1;
    public ModelRenderer shape1_2;
    public ModelRenderer shape1_3;
    public ModelRenderer shape1_4;

    public horse_power_pillar() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.shape1.addBox(-1.6F, 0.0F, -6.0F, 3, 2, 44, 0.0F);
        this.shape1_1 = new ModelRenderer(this, 64, 95);
        this.shape1_1.setRotationPoint(-8.0F, -16.0F, -8.0F);
        this.shape1_1.addBox(0.0F, 0.0F, 0.0F, 16, 8, 16, 0.0F);
        this.shape1_2 = new ModelRenderer(this, 0, 95);
        this.shape1_2.setRotationPoint(-8.0F, 8.0F, -8.0F);
        this.shape1_2.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16, 0.0F);
        this.shape1_3 = new ModelRenderer(this, 0, 0);
        this.shape1_3.setRotationPoint(0.0F, -25.0F, 0.0F);
        this.shape1_3.addBox(-1.0F, 1.0F, -1.0F, 2, 16, 2, 0.0F);
        this.shape1_4 = new ModelRenderer(this, 0, 66);
        this.shape1_4.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.shape1_4.addBox(-4.0F, 0.0F, -4.0F, 8, 16, 8, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape1.render(f5);
        this.shape1_1.render(f5);
        this.shape1_2.render(f5);
        this.shape1_3.render(f5);
        this.shape1_4.render(f5);
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
