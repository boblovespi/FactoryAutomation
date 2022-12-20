package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class OmniMeter extends FABaseItem
{
	public OmniMeter()
	{
		super("omnimeter", new Properties().tab(FAItemGroups.tools).stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		var te = world.getBlockEntity(pos);
		var player = context.getPlayer();

		if (te != null)
		{
			if (world.isClientSide)
				return InteractionResult.CONSUME;
			var heat = te.getCapability(CapabilityHeatUser.HEAT_USER_CAPABILITY, context.getClickedFace());
			var mech = te.getCapability(CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY, context.getClickedFace());
			var fluid = te.getCapability(ForgeCapabilities.FLUID_HANDLER, context.getClickedFace());

			var ref = new Object()
			{
				boolean used = false;
			};

			heat.ifPresent(h ->
			{
				ref.used = true;
				player.displayClientMessage(
						Component.translatable("info.heat_user", String.format("%,.1f", h.GetEnergy()),
								String.format("%,.1f", h.GetTemperature()),
								String.format("%,.1f", h.GetHeatCapacity())),
						false);
			});
			mech.ifPresent(m ->
			{
				var speed = m.GetSpeedOnFace(context.getClickedFace());
				var torque = m.GetTorqueOnFace(context.getClickedFace());
				var power = speed * torque;
				ref.used = true;
				player.displayClientMessage(
						Component.translatable("info.mechanical_user", String.format("%,.1f", power),
								String.format("%,.1f", torque), String.format("%,.1f", speed)), false);
			});
			fluid.ifPresent(f ->
			{
				ref.used = true;
				for (int i = 0; i < f.getTanks(); i++)
				{
					var amount = f.getFluidInTank(i).getAmount() / 1000f;
					var fluidInTank = f.getFluidInTank(i).getFluid().getFluidType().getDescription();
					player.displayClientMessage(
							Component.translatable("info.fluid_container", String.format("%,.2f", amount), fluidInTank),
							false);
				}
			});

			return ref.used ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
		}

		return InteractionResult.PASS;
	}
}
