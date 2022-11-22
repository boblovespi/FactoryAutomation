package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

/**
 * Created by Willi on 4/11/2017.
 */
public class Concrete extends Block
{
	public Concrete()
	{
		super(Properties.of(Material.STONE).strength(10, 1000));
		// setUnlocalizedName(UnlocalizedName());
		// setRegistryName(RegistryName());
		// setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		// setHardness(10);
		// setResistance(10000);
		FABlocks.blocks.add(RegistryObjectWrapper.Block("concrete", this));
		//		new FAItemBlock(this);
		FAItems.items.add(RegistryObjectWrapper.Item("concrete", new BlockItem(this, FAItems.Building())));
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		if (entity instanceof Player)
		{
			((Player) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 1));
		}
	}
}
