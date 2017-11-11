package boblovespi.factoryautomation.common.init;
import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.Tools.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ToolInit
{

	public static final ToolMaterial bronzeMaterial = EnumHelper
			.addToolMaterial(FactoryAutomation.MODID + ":bronze", 2, 251, 5.0F,
					2.0F, 12);



//	public static void Register()
//	{
//		GameRegistry.register(bronzePickaxe);
//		GameRegistry.register(bronzeAxe);
//		GameRegistry.register(bronzeHoe);
//		GameRegistry.register(bronzeShovel);
//		GameRegistry.register(bronzeSword);
//	}

//	public static void RegisterRenders()
//	{
//		registerRender(bronzePickaxe);
//		registerRender(bronzeAxe);
//		registerRender(bronzeHoe);
//		registerRender(bronzeShovel);
//		registerRender(bronzeSword);
//	}
//
//	private static void registerRender(Item item)
//	{
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//				.register(item, 0, new ModelResourceLocation(
//						new ResourceLocation(FactoryAutomation.MODID,
//								item.getUnlocalizedName().substring(5)),
//						"inventory"));
//
//	}
}
