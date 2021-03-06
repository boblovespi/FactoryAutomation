package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TESolidFueledFirebox;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 10/28/2018.
 */
public class SolidFueledFirebox extends FABaseBlock
{
	public SolidFueledFirebox()
	{
		super("solid_fueled_firebox", false,
				Properties.create(Material.ROCK).hardnessAndResistance(3).harvestTool(ToolType.PICKAXE).harvestLevel(0),
				new Item.Properties().group(FAItemGroups.heat));
		TileEntityHandler.tiles.add(TESolidFueledFirebox.class);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit)
	{
		if (!worldIn.isRemote)
			NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(worldIn.getTileEntity(pos)), pos);
		return ActionResultType.SUCCESS;
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
		return new TESolidFueledFirebox();
	}
}
