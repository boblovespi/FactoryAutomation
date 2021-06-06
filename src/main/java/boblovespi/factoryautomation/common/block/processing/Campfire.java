package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Willi on 12/27/2018.
 */
public class Campfire extends FABaseBlock
{
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	private static final VoxelShape BOUNDING_BOX = Block
			.box(3, 0, 3, 13, 6, 13);

	public Campfire()
	{
		super("campfire", false, Properties.of(Materials.WOOD_MACHINE).strength(4),
				new Item.Properties().tab(FAItemGroups.primitive));
		registerDefaultState(getDefaultState().with(LIT, false));
		TileEntityHandler.tiles.add(TECampfire.class);
	}

	@Override
	public int getLightValue(BlockState state)
	{
		return state.getValue(LIT) ? 11 : 0;
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "processing/" + registryName();
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TECampfire();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader levelIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(LIT);
	}

	public void onReplaced(BlockState state, World levelIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity te = levelIn.getTileEntity(pos);
			if (te instanceof TECampfire)
			{
				((TECampfire) te).DropItems();
			}

			super.onReplaced(state, levelIn, pos, newState, isMoving);
		}
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (!world.isClientSide)
		{
			boolean canLight = !state.getValue(LIT);
			ItemStack stack = player.getItemInHand(hand);
			Item item = stack.getItem();
			if (canLight && (item == Items.TORCH || item == Items.FLINT_AND_STEEL
					|| item == FAItems.advancedFlintAndSteel.toItem()))
			{
				world.setBlockState(pos, state.setValue(LIT, true));
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TECampfire)
					((TECampfire) te).SetLit(true);
			} else
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TECampfire)
					((TECampfire) te).TakeOrPlace(stack, player);
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
	{
		if (!state.getValue(LIT))
			return;
		if (rand.nextDouble() < 0.1D)
		{
			world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
					SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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
