package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import boblovespi.factoryautomation.common.util.TEHelper;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/8/2018.
 */
public class StoneWorkbench extends Workbench implements ITileEntityProvider
{
	public StoneWorkbench()
	{
		super(Material.ROCK, "stone_workbench");
		TileEntityHandler.tiles.add(TEStoneWorkbench.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader world)
	{
		return new TEStoneWorkbench();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (!world.isRemote && player instanceof ServerPlayerEntity)
			// playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.WORKBENCH.id, worldIn, pos.getX(), pos.getY(),
			// 		pos.getZ());
			NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(world.getTileEntity(pos)), pos);
		return ActionResultType.SUCCESS;
	}

	//	@Override
	//	public BlockRenderLayer getRenderLayer()
	//	{
	//		return BlockRenderLayer.CUTOUT;
	//	}
}
