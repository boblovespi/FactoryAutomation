package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/8/2018.
 */
public class StoneWorkbench extends Workbench
{
	private static final VoxelShape BOUNDING_BOX = VoxelShapes
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
	public TileEntity createTileEntity(BlockState state, IBlockReader level)
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
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		if (!world.isClientSide && player instanceof ServerPlayerEntity)
			// playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.WORKBENCH.id, levelIn, pos.getX(), pos.getY(),
			// 		pos.getZ());
			NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
		return ActionResultType.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader levelIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
	}
}
