package boblovespi.factoryautomation.common.item.metals;

/**
 * Created by Willi on 11/9/2017.
 */
public class Ingot extends MetalItem
{
	public Ingot()
	{
		super("ingot");
	}

	protected Ingot(String unlocalizedName)
	{
		super(unlocalizedName);
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		if (meta > 1 && meta < itemTypes.getEnumConstants().length)
			return super.getMetaFilePath(meta);
		else
			return super.getMetaFilePath(2);
	}
}
