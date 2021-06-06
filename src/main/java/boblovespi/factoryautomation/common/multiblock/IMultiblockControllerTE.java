package boblovespi.factoryautomation.common.multiblock;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 11/19/2017.
 * interface for multiblock controller tile entities
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IMultiblockControllerTE
{
	default void setStructureValid()
	{
		setStructureValid(true);
	}

	void setStructureValid(boolean isValid);

	default void setStructureInvalid()
	{
		setStructureValid(false);
	}

	boolean isStructureValid();

	void createStructure();

	void breakStructure();

	/**
	 * Gets the capability, or null, of the block at offset for the given side
	 * @param capability the type of capability to get
	 * @param offset the offset of the multiblock part
	 * @param side the side which is accessed
	 * @return the capability implementation which to use
	 */
	<T> LazyOptional<T> getCapability(Capability<T> capability, int[] offset, Direction side);
}
