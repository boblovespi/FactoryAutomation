package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.multiblock.IMultiblockStructureController;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.tileentity.TileEntityBlastFurnaceController;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 11/11/2017.
 */
public class BlastFurnaceController extends Block
		implements FABlock, ITileEntityProvider, IMultiblockStructureController
{
	// TODO: implement tile entity stuff

	public static PropertyDirection FACING = BlockHorizontal.FACING;
	private MultiblockStructurePattern structurePattern;

	public BlastFurnaceController()
	{
		super(Material.DRAGON_EGG);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(10);
		setResistance(10000);
		setDefaultState(blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH));
		FABlocks.blocks.add(this);
		//		new FAItemBlock(this);
		FAItems.items
				.add(new ItemBlock(this).setRegistryName(getRegistryName()));

		structurePattern = new MultiblockStructurePattern(new Block[][][] {
				new Block[][] {
						new Block[] { Blocks.NETHER_BRICK, this.ToBlock(),
								Blocks.NETHER_BRICK },
						new Block[] { Blocks.AIR, Blocks.IRON_BLOCK,
								Blocks.AIR },
						new Block[] { Blocks.AIR, Blocks.IRON_BLOCK,
								Blocks.AIR },
						new Block[] { Blocks.AIR, Blocks.IRON_BLOCK,
								Blocks.AIR },
						new Block[] { Blocks.AIR, Blocks.AIR, Blocks.AIR } },
				new Block[][] {
						new Block[] { Blocks.NETHER_BRICK, Blocks.CAULDRON,
								Blocks.HOPPER },
						new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICK,
								Blocks.IRON_BLOCK },
						new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICK,
								Blocks.IRON_BLOCK },
						new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICK,
								Blocks.IRON_BLOCK },
						new Block[] { Blocks.AIR, Blocks.IRON_BLOCK,
								Blocks.HOPPER } }, new Block[][] {
				new Block[] { Blocks.NETHER_BRICK, Blocks.NETHER_BRICK,
						Blocks.NETHER_BRICK },
				new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
				new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
				new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
				new Block[] { Blocks.AIR, Blocks.AIR, Blocks.AIR } } });
	}

	@Override
	public String UnlocalizedName()
	{
		return "blast_furnace_controller";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING,
				placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(FACING,
				placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityBlastFurnaceController();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote && IsValidStructure(worldIn, pos, state))
		{
			playerIn.openGui(FactoryAutomation.instance,
					GuiHandler.GuiID.BLAST_FURNACE.id, worldIn, pos.getX(),
					pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public MultiblockStructurePattern GetPattern()
	{
		return structurePattern;
	}

	@Override
	public boolean IsValidStructure(World world, BlockPos pos,
			IBlockState state)
	{
		boolean isValid = true;
		switch (state.getValue(FACING))
		{
		case EAST:
			BlockPos lowerLeftFront = pos.north();
			Block[][][] pattern = structurePattern.GetPattern();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[y].length; z++)
					{
						if (!Block.isEqualTo(pattern[x][y][z],
								world.getBlockState(lowerLeftFront.add(x, y, z))
										.getBlock()))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(x, y, z))
										.getBlock().getLocalizedName());
						Log.LogInfo("block in pattern");
					}
				}
			}
			break;
		}
		return isValid;
	}
}
