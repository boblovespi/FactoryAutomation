package boblovespi.factoryautomation.common.item.tools;
import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ToolMaterials
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
