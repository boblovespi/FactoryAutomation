package boblovespi.factoryautomation.test;

/**
 * Created by Willi on 4/14/2018.
 */
public class JsonTester
{
	public static void main(String[] args)
	{
		//		WorkbenchRecipeHandler.LoadFromJson(new ForgeModContainer(),
		//				new ResourceLocation(FactoryAutomation.MODID, "recipes/workbench"));
		//		System.out.println(WorkbenchRecipeHandler.recipes);

		for (int x1 = 0; x1 < 7; x1++)
		{
			for (int y1 = 0; y1 < 5; y1++)
			{
				System.out.println("id: " + (y1 + x1 * 5 + 2) + " | pos: " + (16 + (x1 < 1 ? 0 :
						26 + (x1 < 2 ? 0 : 26 + (x1 - 2) * 18))) + ", " + (17 + 18 * y1));
			}
		}
	}
}
