package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.multiblock.IMultiblockStructureController;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockPart;
import boblovespi.factoryautomation.common.tileentity.TileEntityBlastFurnaceController;
import boblovespi.factoryautomation.common.tileentity.TileEntityMultiblockPart;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by Willi on 11/11/2017.
 */
public class BlastFurnaceController extends Block
		implements FABlock, ITileEntityProvider, IMultiblockStructureController
{
	// TODO: implement tile entity stuff

	public static PropertyDirection FACING = BlockHorizontal.FACING;
	public static PropertyBool MULTIBLOCK_COMPLETE = PropertyBool
			.create("multiblock_complete");

	private final String structurePattern = "blast_furnace";

	public BlastFurnaceController()
	{
		super(Material.DRAGON_EGG);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(10);
		setResistance(10000);
		setDefaultState(
				blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
						  .withProperty(MULTIBLOCK_COMPLETE, false));
		FABlocks.blocks.add(this);
		//		new FAItemBlock(this);
		FAItems.items
				.add(new ItemBlock(this).setRegistryName(getRegistryName()));

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
												   placer.getHorizontalFacing()
														 .getOpposite());
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(FACING,
												   placer.getHorizontalFacing()
														 .getOpposite());
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta & 3);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing)
				   .withProperty(MULTIBLOCK_COMPLETE, (meta & 4) == 4);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() | (state
				.getValue(MULTIBLOCK_COMPLETE) ? 4 : 0);
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
		return new BlockStateContainer(this, FACING, MULTIBLOCK_COMPLETE);
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
		if (!worldIn.isRemote)
		{
			if (IsValidStructure(worldIn, pos, state))
			{
				if (!worldIn.getBlockState(pos).getValue(MULTIBLOCK_COMPLETE))
				{
					CreateStructure(worldIn, pos);
					worldIn.setBlockState(
							pos, worldIn.getBlockState(pos)
										.withProperty(MULTIBLOCK_COMPLETE,
													  true));
				}

				playerIn.openGui(FactoryAutomation.instance,
								 GuiHandler.GuiID.BLAST_FURNACE.id, worldIn,
								 pos.getX(), pos.getY(), pos.getZ());
			} else
			{
				if (worldIn.getBlockState(pos).getValue(MULTIBLOCK_COMPLETE))
				{
					BreakStructure(worldIn, pos);
					worldIn.setBlockState(
							pos, worldIn.getBlockState(pos)
										.withProperty(MULTIBLOCK_COMPLETE,
													  false));
				}
			}
		}
		return true;
	}

	@Override
	public String GetPatternId()
	{
		return structurePattern;
	}

	@Override
	public boolean IsValidStructure(World world, BlockPos pos,
			IBlockState state)
	{
		boolean isValid = true;
		MultiblockPart[][][] pattern = MultiblockHandler.Get(structurePattern)
														.GetPattern();
		switch (state.getValue(FACING))
		{
		case WEST:
		{
			BlockPos lowerLeftFront = pos.north();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						TileEntityMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(x, y, z))
								!= null && world.getTileEntity(lowerLeftFront
																	   .add(x,
																			y,
																			z)) instanceof TileEntityMultiblockPart)
							te = (TileEntityMultiblockPart) world
									.getTileEntity(lowerLeftFront.add(x, y, z));

						if (!Block.isEqualTo(pattern[x][y][z].GetBlock(),
											 world.getBlockState(lowerLeftFront
																		 .add(x,
																			  y,
																			  z))
												  .getBlock()) && !(te != null
								&& te.GetStructureId().equals(structurePattern)
								&& Arrays.equals(te.GetPosition(),
												 new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(x, y, z)).getBlock()
													   .getLocalizedName());
						Log.LogInfo("block in pattern",
									pattern[x][y][z].GetBlock()
													.getLocalizedName());
						Log.LogInfo(
								"te position", te != null ?
										Arrays.toString(te.GetPosition()) :
										"no te found");
						Log.LogInfo("actual position", x + ", " + y + ", " + z);
					}
				}
			}
			break;
		}
		case EAST:
		{
			BlockPos lowerLeftFront = pos.south();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						TileEntityMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(-x, y, -z))
								!= null && world.getTileEntity(lowerLeftFront
																	   .add(-x,
																			y,
																			-z)) instanceof TileEntityMultiblockPart)
							te = (TileEntityMultiblockPart) world.getTileEntity(
									lowerLeftFront.add(-x, y, -z));

						if (!Block.isEqualTo(pattern[x][y][z].GetBlock(),
											 world.getBlockState(lowerLeftFront
																		 .add(-x,
																			  y,
																			  -z))
												  .getBlock()) && !(te != null
								&& te.GetStructureId().equals(structurePattern)
								&& Arrays.equals(te.GetPosition(),
												 new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(-x, y, -z))
													   .getBlock()
													   .getLocalizedName());
						Log.LogInfo("block in pattern",
									pattern[x][y][z].GetBlock()
													.getLocalizedName());
						Log.LogInfo(
								"te position", te != null ?
										Arrays.toString(te.GetPosition()) :
										"no te found");
						Log.LogInfo("actual position", x + ", " + y + ", " + z);
					}
				}
			}
			break;
		}
		case NORTH:
		{
			BlockPos lowerLeftFront = pos.east();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						TileEntityMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(-z, y, x))
								!= null && world.getTileEntity(lowerLeftFront
																	   .add(-z,
																			y,
																			x)) instanceof TileEntityMultiblockPart)
							te = (TileEntityMultiblockPart) world.getTileEntity(
									lowerLeftFront.add(-z, y, x));

						if (!Block.isEqualTo(pattern[x][y][z].GetBlock(),
											 world.getBlockState(lowerLeftFront
																		 .add(-z,
																			  y,
																			  x))
												  .getBlock()) && !(te != null
								&& te.GetStructureId().equals(structurePattern)
								&& Arrays.equals(te.GetPosition(),
												 new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(-z, y, x)).getBlock()
													   .getLocalizedName());
						Log.LogInfo("block in pattern",
									pattern[x][y][z].GetBlock()
													.getLocalizedName());
						Log.LogInfo(
								"te position", te != null ?
										Arrays.toString(te.GetPosition()) :
										"no te found");
						Log.LogInfo("actual position", x + ", " + y + ", " + z);
					}
				}
			}
			break;
		}
		case SOUTH:
		{
			BlockPos lowerLeftFront = pos.west();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						TileEntityMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(z, y, -x))
								!= null && world.getTileEntity(lowerLeftFront
																	   .add(z,
																			y,
																			-x)) instanceof TileEntityMultiblockPart)
							te = (TileEntityMultiblockPart) world.getTileEntity(
									lowerLeftFront.add(z, y, -x));

						if (!Block.isEqualTo(pattern[x][y][z].GetBlock(),
											 world.getBlockState(lowerLeftFront
																		 .add(z,
																			  y,
																			  -x))
												  .getBlock()) && !(te != null
								&& te.GetStructureId().equals(structurePattern)
								&& Arrays.equals(te.GetPosition(),
												 new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(z, y, -x)).getBlock()
													   .getLocalizedName());
						Log.LogInfo("block in pattern",
									pattern[x][y][z].GetBlock()
													.getLocalizedName());
						Log.LogInfo(
								"te position", te != null ?
										Arrays.toString(te.GetPosition()) :
										"no te found");
						Log.LogInfo("actual position", x + ", " + y + ", " + z);
					}
				}
			}
			break;
		}
		default:
			Log.LogWarning(
					"BlastFurnaceController facing not NORTH, SOUTH, EAST, or WEST!");
		}
		return isValid;
	}

	@Override
	public void CreateStructure(World world, BlockPos pos)
	{
		MultiblockPart[][][] pattern = MultiblockHandler.Get(structurePattern)
														.GetPattern();

		switch (world.getBlockState(pos).getValue(FACING))
		{
		case WEST:
		{
			BlockPos lowerLeftFront = pos.north();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(x, y, z))
								|| Block.isEqualTo(
								world.getBlockState(lowerLeftFront.add(x, y, z))
									 .getBlock(), this))
							continue;

						world.setBlockState(lowerLeftFront.add(x, y, z),
											FABlocks.multiblockPart.ToBlock()
																   .getDefaultState());
						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(x, y, z));
						assert te instanceof TileEntityMultiblockPart;
						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z,
													  x, y, z - 1);

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(x, y, z)).getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}
		case EAST:
		{
			BlockPos lowerLeftFront = pos.south();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(x, y, z))
								|| Block.isEqualTo(
								world.getBlockState(lowerLeftFront.add(x, y, z))
									 .getBlock(), this))
							continue;

						world.setBlockState(lowerLeftFront.add(-x, y, -z),
											FABlocks.multiblockPart.ToBlock()
																   .getDefaultState());
						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(-x, y, -z));
						assert te instanceof TileEntityMultiblockPart;
						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z,
													  -x, y, -z + 1);

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(-x, y, -z))
													   .getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}
		case NORTH:
		{
			BlockPos lowerLeftFront = pos.east();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(x, y, z))
								|| Block.isEqualTo(
								world.getBlockState(lowerLeftFront.add(x, y, z))
									 .getBlock(), this))
							continue;

						world.setBlockState(lowerLeftFront.add(-z, y, x),
											FABlocks.multiblockPart.ToBlock()
																   .getDefaultState());
						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(-z, y, x));
						assert te instanceof TileEntityMultiblockPart;
						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z,
													  -z, y, x - 1);

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(-z, y, x)).getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}
		case SOUTH:
		{
			BlockPos lowerLeftFront = pos.west();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(x, y, z))
								|| Block.isEqualTo(
								world.getBlockState(lowerLeftFront.add(x, y, z))
									 .getBlock(), this))
							continue;

						world.setBlockState(lowerLeftFront.add(z, y, -x),
											FABlocks.multiblockPart.ToBlock()
																   .getDefaultState());
						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(z, y, -x));
						assert te instanceof TileEntityMultiblockPart;
						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z,
													  z, y, -x + 1);

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(z, y, -x)).getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}

		}
	}

	@Override
	public void BreakStructure(World world, BlockPos pos)
	{
		MultiblockPart[][][] pattern = MultiblockHandler.Get(structurePattern)
														.GetPattern();

		switch (world.getBlockState(pos).getValue(FACING))
		{
		case WEST:
		{
			BlockPos lowerLeftFront = pos.north();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(x, y, z)))
							continue;

						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(x, y, z));

						if (te == null
								|| !(te instanceof TileEntityMultiblockPart))
							continue;

						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(x, y, z),
											MultiblockHandler
													.Get(structurePattern)
													.GetPattern()[x][y][z]
													.GetBlock()
													.getDefaultState());

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(x, y, z)).getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}
		case EAST:
		{
			BlockPos lowerLeftFront = pos.south();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(-x, y, -z)))
							continue;

						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(-x, y, -z));

						if (te == null
								|| !(te instanceof TileEntityMultiblockPart))
							continue;

						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(-x, y, -z),
											MultiblockHandler
													.Get(structurePattern)
													.GetPattern()[x][y][z]
													.GetBlock()
													.getDefaultState());

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(-x, y, -z))
													   .getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}
		case NORTH:
		{
			BlockPos lowerLeftFront = pos.east();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(-z, y, x)))
							continue;

						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(-z, y, x));

						if (te == null
								|| !(te instanceof TileEntityMultiblockPart))
							continue;
						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(-z, y, x),
											MultiblockHandler
													.Get(structurePattern)
													.GetPattern()[x][y][z]
													.GetBlock()
													.getDefaultState());

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(-z, y, x)).getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}
		case SOUTH:
		{
			BlockPos lowerLeftFront = pos.west();

			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						if (world.isAirBlock(lowerLeftFront.add(z, y, -x)))
							continue;

						TileEntity te = world
								.getTileEntity(lowerLeftFront.add(z, y, -x));

						if (te == null
								|| !(te instanceof TileEntityMultiblockPart))
							continue;
						TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(z, y, -x),
											MultiblockHandler
													.Get(structurePattern)
													.GetPattern()[x][y][z]
													.GetBlock()
													.getDefaultState());

						Log.LogInfo(
								"block in world", world.getBlockState(
										lowerLeftFront.add(z, y, -x)).getBlock()
													   .getLocalizedName());
					}
				}
			}
			break;
		}
		}
	}

	@Override
	public void SetStructureCompleted(World world, BlockPos pos,
			boolean completed)
	{
		world.setBlockState(
				pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE,
														   completed));
	}
}