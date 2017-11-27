package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 11/26/2017.
 */
public class MultiblockPart extends Block
		implements ITileEntityProvider, FABlock
{
	public MultiblockPart(Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(Material.IRON, MapColor.IRON);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMultiblockPart();
	}

	@Override
	public String UnlocalizedName()
	{
		return "multiblock_part";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world,
			BlockPos pos, IBlockState state, int fortune)
	{
		assert world.getTileEntity(pos) != null;
		assert world.getTileEntity(pos) != null;
		assert world.getTileEntity(pos) != null;
		Block.getBlockById(((TileEntityMultiblockPart) world.getTileEntity(pos))
				.getBlockStateIdToDrop()).getDrops(drops, world, pos,
				Block.getStateById(
						((TileEntityMultiblockPart) world.getTileEntity(pos))
								.getBlockStateIdToDrop()), fortune);
	}
}
