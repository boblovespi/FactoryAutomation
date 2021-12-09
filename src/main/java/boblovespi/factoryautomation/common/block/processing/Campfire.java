package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 12/27/2018.
 */
public class Campfire extends FABaseBlock implements EntityBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	private static final VoxelShape BOUNDING_BOX = Block
			.box(3, 0, 3, 13, 6, 13);

	public Campfire()
	{
		super("campfire", false, Properties.of(Materials.WOOD_MACHINE).strength(4).lightLevel(s -> s.getValue(LIT) ? 11 : 0),
				new Item.Properties().tab(FAItemGroups.primitive));
		registerDefaultState(defaultBlockState().setValue(LIT, false));
		TileEntityHandler.tiles.add(TECampfire.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TECampfire(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(LIT);
	}

	public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			BlockEntity te = levelIn.getBlockEntity(pos);
			if (te instanceof TECampfire)
			{
				((TECampfire) te).DropItems();
			}

			super.onRemove(state, levelIn, pos, newState, isMoving);
		}
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit)
	{
		if (!world.isClientSide)
		{
			boolean canLight = !state.getValue(LIT);
			ItemStack stack = player.getItemInHand(hand);
			Item item = stack.getItem();
			if (canLight && (item == Items.TORCH || item == Items.FLINT_AND_STEEL
					|| item == FAItems.advancedFlintAndSteel.ToItem()))
			{
				world.setBlockAndUpdate(pos, state.setValue(LIT, true));
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TECampfire)
					((TECampfire) te).SetLit(true);
			} else
			{
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TECampfire)
					((TECampfire) te).TakeOrPlace(stack, player);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand)
	{
		if (!state.getValue(LIT))
			return;
		if (rand.nextDouble() < 0.1D)
		{
			world.playSound(null, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
					SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.2;
		double z = pos.getZ() + 0.5;
		world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.02, 0);
		world.addParticle(ParticleTypes.SMOKE, x, y, z, rand.nextDouble() / 20d, 0.15, rand.nextDouble() / 20d);
		if (rand.nextDouble() < 0.5D)
			world.addParticle(
					ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, rand.nextDouble() / 40d, 0.08, rand.nextDouble() / 40d);
	}
}
