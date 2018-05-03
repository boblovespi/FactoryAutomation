package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ToolMaterials
{
	/*
		Harvest levels:
		0: wood
		1: stone
		2: copper
		3: iron
		4: bronze
		5: diamond / steel

		 */

	// TODO: populate from config

	public static final int WOOD = 0;
	public static final int STONE = 1;
	public static final int COPPER = 2;
	public static final int IRON = 3;
	public static final int BRONZE = 4;
	public static final int DIAMOND = 5;
	public static final int STEEL = 5;

	public static final ToolMaterial bronzeMaterial = EnumHelper
			.addToolMaterial(FactoryAutomation.MODID + ":bronze", BRONZE, 251, 5.0F, 2.0F, 12);
	public static final ToolMaterial steelMaterial = EnumHelper
			.addToolMaterial(FactoryAutomation.MODID + ":steel", STEEL, 1920, 6.5f, 8, 3);

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
