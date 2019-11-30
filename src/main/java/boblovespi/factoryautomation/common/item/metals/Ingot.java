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
	public String GetMetaFilePath(int meta)
	{
		if (meta > 1 && meta < itemTypes.getEnumConstants().length)
			return super.GetMetaFilePath(meta);
		else
			return super.GetMetaFilePath(2);
	}
}
