package boblovespi.factoryautomation.common.block.crafter;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 5/28/2018.
 */
public class ChipCreator extends FABaseBlock
{
	public ChipCreator()
	{
		super(Material.METAL, "chip_creator", null);
		TileEntityHandler.tiles.add(TEBasicCircuitCreator.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "workbench/" + RegistryName();
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter unused)
	{
		return new TEBasicCircuitCreator();
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit)
	{
		if (!world.isClientSide && player instanceof ServerPlayer)
			NetworkHooks.openGui((ServerPlayer) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
		return InteractionResult.SUCCESS;
	}
}
