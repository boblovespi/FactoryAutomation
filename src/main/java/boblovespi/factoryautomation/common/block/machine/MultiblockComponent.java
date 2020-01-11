package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Willi on 11/26/2017.
 */
public class MultiblockComponent extends Block implements ITileEntityProvider, FABlock
{
	public MultiblockComponent()
	{
		super(Material.IRON, MapColor.IRON);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setLightOpacity(0);
		setHardness(1.5f);
		setHarvestLevel("pickaxe", 0);
		FABlocks.blocks.add(this);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEMultiblockPart();
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
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState state, int fortune)
	{
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		TileEntity te = world.getTileEntity(pos);

		assert te != null && te instanceof TEMultiblockPart;

		TEMultiblockPart part = (TEMultiblockPart) te;
		MultiblockStructurePattern structure = MultiblockHandler.Get(part.GetStructureId());
		int[] loc = part.GetPosition();
		Block block = structure.GetPattern()[loc[0]][loc[1]][loc[2]].GetBlock();
		Item item = block.getItemDropped(block.getDefaultState(), rand, fortune);
		if (item != Items.AIR)
			drops.add(new ItemStack(item));
	}

	@Override
	public void breakBlock(World world, BlockPos pos, BlockState state)
	{

		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		//		Log.LogInfo("something broke!");
		//		Log.LogInfo("blockPos", pos);
		//
		//		TEMultiblockPart part = (TEMultiblockPart) world.getTileEntity(pos);
		//
		//		Log.LogInfo("part is null", part == null);
		//
		//		if (part != null)
		//		{
		//			MultiblockStructurePattern structure = MultiblockHandler.Get(part.GetStructureId());
		//			int[] offset = part.GetOffset();
		//
		//			BlockPos controllerLoc = pos.add(-offset[0], -offset[1], -offset[2]);
		//
		//			Log.LogInfo("offset", Arrays.toString(offset));
		//
		//			Block controller = world.getBlockState(controllerLoc).getBlock();
		//			Log.LogInfo("is controller", controller instanceof IMultiblockStructureController);
		//
		//			if (controller instanceof IMultiblockStructureController)
		//			{
		//				((IMultiblockStructureController) controller).BreakStructure(world, controllerLoc);
		//				((IMultiblockStructureController) controller).SetStructureCompleted(world, controllerLoc, false);
		//			}
		//		}
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(BlockState state)
	{
		return false;
	}
}
