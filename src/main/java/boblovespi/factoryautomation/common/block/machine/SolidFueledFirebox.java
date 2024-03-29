package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TESolidFueledFirebox;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 10/28/2018.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SolidFueledFirebox extends FABaseBlock implements EntityBlock
{
	public SolidFueledFirebox()
	{
		super("solid_fueled_firebox", false,
			  Properties.of(Material.STONE).strength(3).requiresCorrectToolForDrops(),
			  new Item.Properties().tab(FAItemGroups.heat));
		TileEntityHandler.tiles.add(TESolidFueledFirebox.class);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the result type of using this block.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
								 InteractionHand handIn, BlockHitResult hit)
	{
		if (!world.isClientSide)
			NetworkHooks.openGui((ServerPlayer) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TESolidFueledFirebox(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}
}
