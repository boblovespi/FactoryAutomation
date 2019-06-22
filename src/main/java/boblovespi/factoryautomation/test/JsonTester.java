package boblovespi.factoryautomation.test;

/**
 * Created by Willi on 4/14/2018.
 */
public class JsonTester
{
	public static void main(String[] args)
	{

		for (int i = 0; i >= 0; --i)
		{
			System.out.println("asddf");
		}
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

		for (String s : splitObjectName("crafting_shaped"))
		{
			System.out.println(s);
		}
	}

	public static String[] splitObjectName(String toSplit)
	{
		String[] astring = new String[] { "minecraft", toSplit };
		int i = toSplit.indexOf(58);

		if (i >= 0)
		{
			astring[1] = toSplit.substring(i + 1, toSplit.length());

			if (i > 1)
			{
				astring[0] = toSplit.substring(0, i);
			}
		}

		return astring;
	}
}