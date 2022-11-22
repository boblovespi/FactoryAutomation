package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.workbench.TEIronWorkbench;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
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
 * Created by Willi on 4/15/2018.
 */
public class IronWorkbench extends Workbench implements EntityBlock
{
	private static final VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 16, 15, 16);

	public IronWorkbench()
	{
		super(Material.STONE, "iron_workbench");
		TileEntityHandler.tiles.add(TEIronWorkbench.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEIronWorkbench(pos, state);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit)
	{
		if (!world.isClientSide && player instanceof ServerPlayer)
			// playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.WORKBENCH.id, levelIn, pos.getX(), pos.getY(),
			// 		pos.getZ());
			NetworkHooks.openScreen((ServerPlayer) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
		return InteractionResult.SUCCESS;
	}

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
