package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConcreteSlab extends BlockSlab implements FABlock{
//TODO Finish
    public ConcreteSlab()
    {
        super(Material.ROCK);
        setUnlocalizedName(UnlocalizedName());
        setRegistryName(RegistryName());
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(10);
        setResistance(10000);
        FABlocks.blocks.add(this);
        FAItems.items
                .add(new ItemBlock(this).setUnlocalizedName(UnlocalizedName())
                        .setRegistryName(RegistryName()));
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
                    .addPotionEffect(new PotionEffect(MobEffects.SPEED, 0, 1));
        }
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return UnlocalizedName();
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return null;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return null;
    }
}
