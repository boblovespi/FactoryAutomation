package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 4/11/2017.
 */
public class Concrete extends Block implements FABlock
{
	public Concrete()
	{
		super(Properties.of(Material.STONE).strength(10, 1000));
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		// setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		// setHardness(10);
		// setResistance(10000);
		FABlocks.blocks.add(this);
		//		new FAItemBlock(this);
		FAItems.items.add(new BlockItem(this, FAItems.Building()).setRegistryName(this.getRegistryName()));
	}

	@Override
	public String UnlocalizedName()
	{
		return "concrete";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public void stepOn(Level level, BlockPos pos, Entity entity)
	{
		if (entity instanceof Player)
		{
			((Player) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 1));
		}
	}
}
