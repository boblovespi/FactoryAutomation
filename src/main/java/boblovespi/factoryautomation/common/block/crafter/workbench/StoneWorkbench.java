package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/8/2018.
 */
public class StoneWorkbench extends Workbench
{
	private static final VoxelShape BOUNDING_BOX = Shapes
			.or(Block.box(1, 0, 1, 15, 13, 15), Block.box(0, 13, 0, 16, 16, 16));

	public StoneWorkbench()
	{
		super(Material.STONE, "stone_workbench");
		TileEntityHandler.tiles.add(TEStoneWorkbench.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TEStoneWorkbench();
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit)
	{
		if (!world.isClientSide && player instanceof ServerPlayer)
			// playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.WORKBENCH.id, levelIn, pos.getX(), pos.getY(),
			// 		pos.getZ());
			NetworkHooks.openGui((ServerPlayer) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}
}
