package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.processing.TETumblingBarrel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class TumblingBarrel extends FABaseBlock implements EntityBlock
{
	private static final VoxelShape BOUNDING_BOX = Shapes.or(Block.box(2, 4, 2, 14, 16, 14),
															 Block.box(14, 7, 5, 16, 13, 11),
															 Block.box(14, 0, 6, 16, 7, 10),
															 Block.box(0, 7, 5, 2, 13, 11),
															 Block.box(0, 0, 6, 2, 7, 10));

	public TumblingBarrel()
	{
		super("tumbling_barrel", false, BlockBehaviour.Properties.of(Material.WOOD).strength(1.5f).requiresCorrectToolForDrops(),
			  new Item.Properties().tab(FAItemGroups.fluid));
		TileEntityHandler.tiles.add(TETumblingBarrel.class);
		registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.HORIZONTAL_AXIS);
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
			NetworkHooks.openScreen((ServerPlayer) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TETumblingBarrel(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}
}
