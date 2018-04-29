package boblovespi.factoryautomation.common.multiblock;

/**
 * Created by Willi on 11/19/2017.
 */
public interface IMultiblockStructureControllerTE
{
	default void SetStructureValid()
	{
		SetStructureValid(true);
	}

	void SetStructureValid(boolean isValid);

	default void SetStructureInvalid()
	{
		SetStructureValid(false);
	}

	boolean IsStructureValid();

	void CreateStructure();

	void BreakStructure();
}
