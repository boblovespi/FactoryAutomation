package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public void onEntityWalk(World level, BlockPos pos, Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			((PlayerEntity) entity).addPotionEffect(new EffectInstance(Effects.SPEED, 10, 1));
		}
	}
}
