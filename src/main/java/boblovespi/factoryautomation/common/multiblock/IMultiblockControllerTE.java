package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Created by Willi on 11/19/2017.
 * interface for multiblock controller tile entities
 */
public interface IMultiblockControllerTE
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

	/**
	 * Gets the capability, or null, of the block at offset for the given side
	 * @param capability the type of capability to get
	 * @param offset the offset of the multiblock part
	 * @param side the side which is accessed
	 * @return the capability implementation which to use
	 */
	<T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side);
}
