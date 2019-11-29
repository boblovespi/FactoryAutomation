package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 4/11/2017.
 */
public class Concrete extends Block implements FABlock
{
	public Concrete()
	{
		super(Material.ROCK);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(10);
		setResistance(10000);
		FABlocks.blocks.add(this);
		//		new FAItemBlock(this);
		FAItems.items.add(new BlockItem(this)
				.setRegistryName(this.getRegistryName()));
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
	public void onEntityWalk(World world, BlockPos pos, Entity entity)
	{
		if (entity instanceof EntityPlayer)
		{
			((EntityPlayer) entity)
					.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 1));
		}
	}
}
