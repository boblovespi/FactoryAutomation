package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.workbench.TEIronWorkbench;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/15/2018.
 */
public class IronWorkbench extends Workbench
{
	public IronWorkbench()
	{
		super(Material.ROCK, "iron_workbench");
		TileEntityHandler.tiles.add(TEIronWorkbench.class);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TEIronWorkbench();
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		if (!world.isRemote && player instanceof ServerPlayerEntity)
			// playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.WORKBENCH.id, worldIn, pos.getX(), pos.getY(),
			// 		pos.getZ());
			NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(world.getTileEntity(pos)), pos);
		return ActionResultType.SUCCESS;
	}
}
