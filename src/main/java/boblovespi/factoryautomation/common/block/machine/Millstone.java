package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMillstone;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 2/12/2019.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Millstone extends FABaseBlock implements EntityBlock
{
	public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");
	public static final VoxelShape BOUNDING_BOX = Shapes
			.or(Block.box(0, 0, 0, 16, 8, 16), Block.box(2, 8, 2, 14, 12, 14),
					Block.box(7.5, 12, 7.5, 8.5, 16, 8.5)).optimize();

	public Millstone()
	{
		super("millstone", false,
				Properties.of(Material.STONE).strength(2.5f).requiresCorrectToolForDrops(), new Item.Properties().tab(FAItemGroups.mechanical));

		TileEntityHandler.tiles.add(TEMillstone.class);
		registerDefaultState(stateDefinition.any().setValue(IS_TOP, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEMillstone(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(IS_TOP);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult result)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;
		ItemStack item = player.getItemInHand(hand);
		BlockEntity te = world.getBlockEntity(pos);

		if (te instanceof TEMillstone)
		{
			((TEMillstone) te).TakeOrPlace(item, player);
		}
		return InteractionResult.SUCCESS;
	}

	//	@Override
	//	public boolean isFullCube(BlockState state)
	//	{
	//		return false;
	//	}
	//
	//	/**
	//	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	//	 */
	//	@Override
	//	public boolean isOpaqueCube(BlockState state)
	//	{
	//		return false;
	//	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return Shapes.empty();
	}
}
