package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TECreativeMechanicalSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/20/2018.
 */
public class CreativeMechanicalSource extends FABaseBlock implements EntityBlock
{
	public CreativeMechanicalSource()
	{
		super(Material.METAL, "creative_mechanical_source", null);
		TileEntityHandler.tiles.add(TECreativeMechanicalSource.class);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TECreativeMechanicalSource(pos, state);
	}
}
