package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.ResourceLocation;

public class Pickaxe extends ItemPickaxe{

    public Pickaxe(ToolMaterial material, String unlocalizedName)
    {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, unlocalizedName));
    }

}
