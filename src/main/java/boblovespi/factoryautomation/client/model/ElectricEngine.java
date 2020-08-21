package boblovespi.factoryautomation.client.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * electric_engine - King_of_Creepers
 * Created using Tabula 7.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ElectricEngine // extends ModelBase
{
	/*public ModelRenderer frame;
	public ModelRenderer frame_1;
	public ModelRenderer frame_2;
	public ModelRenderer frame_3;
	public ModelRenderer frame_4;
	public ModelRenderer frame_5;
	public ModelRenderer frame_6;
	public ModelRenderer frame_7;
	public ModelRenderer frame_8;
	public ModelRenderer frame_9;
	public ModelRenderer engine_top;
	public ModelRenderer engine_side;
	public ModelRenderer engine_side_1;
	public ModelRenderer engine_bottom;
	public ModelRenderer engine_body;
	public ModelRenderer energy_port;
	public ModelRenderer jack;
	public ModelRenderer axis;
	public ModelRenderer energy_box;
	public ModelRenderer energy_wire;
	public ModelRenderer energy_port_1;
	public ModelRenderer energy_wire_1;
	public ModelRenderer jack_plate;
	public ModelRenderer jack_1;
	public ModelRenderer jack_2;
	public ModelRenderer jack_3;
	public ModelRenderer frame_10;
	public ModelRenderer frame_11;
	public ModelRenderer frame_12;
	public ModelRenderer frame_13;
	public ModelRenderer frame_14;
	public ModelRenderer frame_15;
	public ModelRenderer frame_16;
	public ModelRenderer frame_17;
	public ModelRenderer frame_18;

	public ElectricEngine()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.energy_port_1 = new ModelRenderer(this, 0, 18);
		this.energy_port_1.setRotationPoint(-3.0F, 21.0F, 7.4F);
		this.energy_port_1.addBox(0.0F, 0.0F, -4.0F, 6, 3, 5, 0.0F);
		this.energy_wire_1 = new ModelRenderer(this, 5, 39);
		this.energy_wire_1.setRotationPoint(-3.0F, 18.78F, 6.0F);
		this.energy_wire_1.addBox(7.0F, 2.0F, -4.0F, 1, 1, 3, -0.2F);
		this.frame_2 = new ModelRenderer(this, 0, 32);
		this.frame_2.setRotationPoint(-8.0F, 9.0F, 7.0F);
		this.frame_2.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
		this.engine_bottom = new ModelRenderer(this, 0, 0);
		this.engine_bottom.setRotationPoint(-3.0F, 28.0F, 7.4F);
		this.engine_bottom.addBox(0.0F, -9.0F, -13.0F, 6, 1, 12, 0.0F);
		this.energy_box = new ModelRenderer(this, 46, 0);
		this.energy_box.setRotationPoint(-3.0F, 18.8F, 1.9F);
		this.energy_box.addBox(5.0F, 0.0F, -5.0F, 3, 3, 6, 0.0F);
		this.frame_5 = new ModelRenderer(this, 0, 32);
		this.frame_5.setRotationPoint(4.0F, 9.0F, -5.0F);
		this.frame_5.addBox(0.0F, 0.0F, 0.0F, 1, 15, 1, 0.0F);
		this.axis = new ModelRenderer(this, 22, 22);
		this.axis.setRotationPoint(0.0F, 16.0F, -0.6F);
		this.axis.addBox(-0.5F, -0.5F, -7.0F, 1, 1, 15, 0.0F);
		this.jack_plate = new ModelRenderer(this, 0, 5);
		this.jack_plate.setRotationPoint(0.0F, 16.0F, -0.6F);
		this.jack_plate.addBox(-1.5F, -1.5F, -6.5F, 3, 3, 1, 0.0F);
		this.frame = new ModelRenderer(this, 29, 47);
		this.frame.setRotationPoint(-8.0F, 8.0F, 7.0F);
		this.frame.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
		this.jack = new ModelRenderer(this, 0, 0);
		this.jack.setRotationPoint(0.0F, 16.1F, -0.6F);
		this.jack.addBox(1.5F, -0.6F, -8.5F, 1, 1, 3, 0.0F);
		this.energy_wire = new ModelRenderer(this, 6, 33);
		this.energy_wire.setRotationPoint(2.94F, 21.95F, 5.0F);
		this.energy_wire.addBox(0.0F, -2.0F, -1.0F, 1, 4, 1, -0.2F);
		this.setRotateAngle(energy_wire, 0.0F, 0.0F, 0.8651597102135892F);
		this.frame_9 = new ModelRenderer(this, 29, 47);
		this.frame_9.setRotationPoint(-8.0F, 23.0F, 7.0F);
		this.frame_9.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
		this.engine_side = new ModelRenderer(this, 0, 0);
		this.engine_side.setRotationPoint(-3.0F, 21.0F, 7.4F);
		this.engine_side.addBox(6.0F, -8.0F, -13.0F, 1, 6, 12, 0.0F);
		this.frame_10 = new ModelRenderer(this, 0, 49);
		this.frame_10.setRotationPoint(-8.0F, 23.0F, -13.0F);
		this.frame_10.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
		this.frame_12 = new ModelRenderer(this, 0, 32);
		this.frame_12.setRotationPoint(-5.0F, 9.0F, -5.0F);
		this.frame_12.addBox(0.0F, 0.0F, 0.0F, 1, 15, 1, 0.0F);
		this.frame_18 = new ModelRenderer(this, 0, 49);
		this.frame_18.setRotationPoint(7.0F, 8.0F, -13.0F);
		this.frame_18.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
		this.engine_top = new ModelRenderer(this, 0, 0);
		this.engine_top.setRotationPoint(-3.0F, 21.0F, 7.4F);
		this.engine_top.addBox(0.0F, -9.0F, -13.0F, 6, 1, 12, 0.0F);
		this.frame_17 = new ModelRenderer(this, 29, 47);
		this.frame_17.setRotationPoint(-8.0F, 23.0F, -8.0F);
		this.frame_17.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
		this.frame_8 = new ModelRenderer(this, 0, 49);
		this.frame_8.setRotationPoint(7.0F, 23.0F, -13.0F);
		this.frame_8.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
		this.jack_3 = new ModelRenderer(this, 0, 0);
		this.jack_3.setRotationPoint(0.0F, 16.1F, -0.6F);
		this.jack_3.addBox(-2.5F, -0.6F, -8.5F, 1, 1, 3, 0.0F);
		this.frame_6 = new ModelRenderer(this, 0, 32);
		this.frame_6.setRotationPoint(7.0F, 9.0F, -8.0F);
		this.frame_6.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
		this.frame_4 = new ModelRenderer(this, 0, 32);
		this.frame_4.setRotationPoint(7.0F, 9.0F, 7.0F);
		this.frame_4.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
		this.frame_16 = new ModelRenderer(this, 0, 49);
		this.frame_16.setRotationPoint(-4.0F, 8.0F, -13.0F);
		this.frame_16.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
		this.frame_1 = new ModelRenderer(this, 0, 49);
		this.frame_1.setRotationPoint(3.0F, 8.0F, -13.0F);
		this.frame_1.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
		this.frame_3 = new ModelRenderer(this, 29, 47);
		this.frame_3.setRotationPoint(-8.0F, 8.0F, -8.0F);
		this.frame_3.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
		this.jack_1 = new ModelRenderer(this, 0, 0);
		this.jack_1.setRotationPoint(0.0F, 16.1F, -0.6F);
		this.jack_1.addBox(-0.5F, -2.5F, -8.5F, 1, 1, 3, 0.0F);
		this.frame_15 = new ModelRenderer(this, 0, 49);
		this.frame_15.setRotationPoint(-8.0F, 8.0F, -13.0F);
		this.frame_15.addBox(0.0F, 0.0F, 6.0F, 1, 1, 14, 0.0F);
		this.engine_body = new ModelRenderer(this, 0, 0);
		this.engine_body.setRotationPoint(-3.0F, 21.0F, 7.4F);
		this.engine_body.addBox(0.0F, -8.0F, -13.0F, 6, 6, 12, 0.0F);
		this.energy_port = new ModelRenderer(this, 0, 26);
		this.energy_port.setRotationPoint(-3.0F, 20.9F, 7.4F);
		this.energy_port.addBox(0.0F, -1.0F, -4.0F, 6, 2, 3, 0.0F);
		this.frame_7 = new ModelRenderer(this, 22, 50);
		this.frame_7.setRotationPoint(-8.0F, 24.0F, -7.0F);
		this.frame_7.addBox(1.0F, -0.01F, 0.0F, 14, 0, 14, 0.0F);
		this.jack_2 = new ModelRenderer(this, 0, 0);
		this.jack_2.setRotationPoint(0.0F, 16.1F, -0.6F);
		this.jack_2.addBox(-0.5F, 1.4F, -8.5F, 1, 1, 3, 0.0F);
		this.frame_11 = new ModelRenderer(this, 0, 32);
		this.frame_11.setRotationPoint(-8.0F, 9.0F, -8.0F);
		this.frame_11.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
		this.frame_13 = new ModelRenderer(this, 0, 32);
		this.frame_13.setRotationPoint(-5.0F, 9.0F, 5.0F);
		this.frame_13.addBox(0.0F, 0.0F, 0.0F, 1, 15, 1, 0.0F);
		this.engine_side_1 = new ModelRenderer(this, 0, 0);
		this.engine_side_1.setRotationPoint(-3.0F, 21.0F, 7.4F);
		this.engine_side_1.addBox(-1.0F, -8.0F, -13.0F, 1, 6, 12, 0.0F);
		this.frame_14 = new ModelRenderer(this, 0, 32);
		this.frame_14.setRotationPoint(4.0F, 9.0F, 5.0F);
		this.frame_14.addBox(0.0F, 0.0F, 0.0F, 1, 15, 1, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.energy_port_1.render(f5);
		this.energy_wire_1.render(f5);
		this.frame_2.render(f5);
		this.engine_bottom.render(f5);
		this.energy_box.render(f5);
		this.frame_5.render(f5);
		this.axis.render(f5);
		this.jack_plate.render(f5);
		this.frame.render(f5);
		this.jack.render(f5);
		this.energy_wire.render(f5);
		this.frame_9.render(f5);
		this.engine_side.render(f5);
		this.frame_10.render(f5);
		this.frame_12.render(f5);
		this.frame_18.render(f5);
		this.engine_top.render(f5);
		this.frame_17.render(f5);
		this.frame_8.render(f5);
		this.jack_3.render(f5);
		this.frame_6.render(f5);
		this.frame_4.render(f5);
		this.frame_16.render(f5);
		this.frame_1.render(f5);
		this.frame_3.render(f5);
		this.jack_1.render(f5);
		this.frame_15.render(f5);
		this.engine_body.render(f5);
		this.energy_port.render(f5);
		this.frame_7.render(f5);
		this.jack_2.render(f5);
		this.frame_11.render(f5);
		this.frame_13.render(f5);
		this.engine_side_1.render(f5);
		this.frame_14.render(f5);
	}

	*//**
 * This is a helper function from Tabula to set the rotation of model parts
 *//*
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void RenderTESR(float scale)
	{
		this.energy_port_1.render(scale);
		this.energy_wire_1.render(scale);
		this.frame_2.render(scale);
		this.engine_bottom.render(scale);
		this.energy_box.render(scale);
		this.frame_5.render(scale);
		this.axis.render(scale);
		this.jack_plate.render(scale);
		this.frame.render(scale);
		this.jack.render(scale);
		this.energy_wire.render(scale);
		this.frame_9.render(scale);
		this.engine_side.render(scale);
		this.frame_10.render(scale);
		this.frame_12.render(scale);
		this.frame_18.render(scale);
		this.engine_top.render(scale);
		this.frame_17.render(scale);
		this.frame_8.render(scale);
		this.jack_3.render(scale);
		this.frame_6.render(scale);
		this.frame_4.render(scale);
		this.frame_16.render(scale);
		this.frame_1.render(scale);
		this.frame_3.render(scale);
		this.jack_1.render(scale);
		this.frame_15.render(scale);
		this.engine_body.render(scale);
		this.energy_port.render(scale);
		this.frame_7.render(scale);
		this.jack_2.render(scale);
		this.frame_11.render(scale);
		this.frame_13.render(scale);
		this.engine_side_1.render(scale);
		this.frame_14.render(scale);
	}

	public void Rotate(float deg)
	{
		SetRot(axis, 0, 0, deg);
		SetRot(jack, 0, 0, deg);
		SetRot(jack_1, 0, 0, deg);
		SetRot(jack_2, 0, 0, deg);
		SetRot(jack_3, 0, 0, deg);
		SetRot(jack_plate, 0, 0, deg);
	}

	private void SetRot(ModelRenderer r, float x, float y, float z)
	{
		setRotateAngle(r, x, y, z);
	}*/
}
