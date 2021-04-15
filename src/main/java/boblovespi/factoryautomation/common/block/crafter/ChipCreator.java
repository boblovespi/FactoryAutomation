package boblovespi.factoryautomation.common.block.crafter;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
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
 * Created by Willi on 5/28/2018.
 */
public class ChipCreator extends FABaseBlock implements ITileEntityProvider
{
	public ChipCreator()
	{
		super(Material.METAL, "chip_creator", null);
		TileEntityHandler.tiles.add(TEBasicCircuitCreator.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "workbench/" + RegistryName();
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader unused)
	{
		return new TEBasicCircuitCreator();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (!world.isClientSide && player instanceof ServerPlayerEntity)
			NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
		return ActionResultType.SUCCESS;
	}
}
