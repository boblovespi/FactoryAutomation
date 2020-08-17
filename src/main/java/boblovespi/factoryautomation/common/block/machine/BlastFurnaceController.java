package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockPart;
import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import boblovespi.factoryautomation.common.util.Log;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by Willi on 11/11/2017.
 */
public class BlastFurnaceController extends Block
		implements FABlock /*, ITileEntityProvider, IMultiblockStructureController*/
{
	// TODO: implement tile entity stuff

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");

	private final String structurePattern = "blast_furnace";

	public BlastFurnaceController()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(10).harvestTool(ToolType.PICKAXE).harvestLevel(0));
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		// setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		// setHardness(10);
		// setResistance(10000);
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(MULTIBLOCK_COMPLETE, false));
		FABlocks.blocks.add(this);
		//		new FAItemBlock(this);
		FAItems.items.add(new BlockItem(this, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
				.setRegistryName(getRegistryName()));

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

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, MULTIBLOCK_COMPLETE);
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
		return new TEBlastFurnaceController();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit)
	{
		if (!worldIn.isRemote)
		{
			if (IsValidStructure(worldIn, pos, state))
			{
				if (!worldIn.getBlockState(pos).get(MULTIBLOCK_COMPLETE))
				{
					CreateStructure(worldIn, pos);
					worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(MULTIBLOCK_COMPLETE, true));
				}

				NetworkHooks
						.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(worldIn.getTileEntity(pos)), pos);
			} else
			{
				if (worldIn.getBlockState(pos).get(MULTIBLOCK_COMPLETE))
				{
					BreakStructure(worldIn, pos);
					worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(MULTIBLOCK_COMPLETE, false));
				}
			}
		}
		return ActionResultType.SUCCESS;
	}

	public String GetPatternId()
	{
		return structurePattern;
	}

	public boolean IsValidStructure(World world, BlockPos pos, BlockState state)
	{
		boolean isValid = true;
		MultiblockPart[][][] pattern = MultiblockHandler.Get(structurePattern).GetPattern();
		switch (state.get(FACING))
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
						TEMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(x, y, z)) != null && world
								.getTileEntity(lowerLeftFront.add(x, y, z)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getTileEntity(lowerLeftFront.add(x, y, z));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.add(x, y, z)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(x, y, z)).getBlock().getTranslationKey());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getTranslationKey());
						Log.LogInfo("te position", te != null ? Arrays.toString(te.GetPosition()) : "no te found");
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
						TEMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(-x, y, -z)) != null && world
								.getTileEntity(lowerLeftFront.add(-x, y, -z)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getTileEntity(lowerLeftFront.add(-x, y, -z));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.add(-x, y, -z)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(-x, y, -z)).getBlock().getTranslationKey());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getTranslationKey());
						Log.LogInfo("te position", te != null ? Arrays.toString(te.GetPosition()) : "no te found");
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
						TEMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(-z, y, x)) != null && world
								.getTileEntity(lowerLeftFront.add(-z, y, x)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getTileEntity(lowerLeftFront.add(-z, y, x));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.add(-z, y, x)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(-z, y, x)).getBlock().getTranslationKey());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getTranslationKey());
						Log.LogInfo("te position", te != null ? Arrays.toString(te.GetPosition()) : "no te found");
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
						TEMultiblockPart te = null;
						if (world.getTileEntity(lowerLeftFront.add(z, y, -x)) != null && world
								.getTileEntity(lowerLeftFront.add(z, y, -x)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getTileEntity(lowerLeftFront.add(z, y, -x));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.add(z, y, -x)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(z, y, -x)).getBlock().getTranslationKey());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getTranslationKey());
						Log.LogInfo("te position", te != null ? Arrays.toString(te.GetPosition()) : "no te found");
						Log.LogInfo("actual position", x + ", " + y + ", " + z);
					}
				}
			}
			break;
		}
		default:
			Log.LogWarning("BlastFurnaceController facing not NORTH, SOUTH, EAST, or WEST!");
		}
		return isValid;
	}

	public void CreateStructure(World world, BlockPos pos)
	{
		MultiblockPart[][][] pattern = MultiblockHandler.Get(structurePattern).GetPattern();

		switch (world.getBlockState(pos).get(FACING))
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
						BlockState st = world.getBlockState(lowerLeftFront.add(x, y, z));

						if (world.isAirBlock(lowerLeftFront.add(x, y, z))
								|| world.getBlockState(lowerLeftFront.add(x, y, z)).getBlock() == this)
							continue;

						world.setBlockState(lowerLeftFront.add(x, y, z),
								FABlocks.multiblockPart.ToBlock().getDefaultState());
						TileEntity te = world.getTileEntity(lowerLeftFront.add(x, y, z));
						assert te instanceof TEMultiblockPart;
						TEMultiblockPart part = (TEMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z, x, y, z - 1, st);

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(x, y, z)).getBlock().getTranslationKey());
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
						BlockState st = world.getBlockState(lowerLeftFront.add(-x, y, -z));

						if (world.isAirBlock(lowerLeftFront.add(-x, y, -z))
								|| world.getBlockState(lowerLeftFront.add(-x, y, -z)).getBlock() == this)
							continue;

						world.setBlockState(lowerLeftFront.add(-x, y, -z),
								FABlocks.multiblockPart.ToBlock().getDefaultState());
						TileEntity te = world.getTileEntity(lowerLeftFront.add(-x, y, -z));
						assert te instanceof TEMultiblockPart;
						TEMultiblockPart part = (TEMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z, -x, y, -z + 1, st);

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(-x, y, -z)).getBlock().getTranslationKey());
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
						BlockState st = world.getBlockState(lowerLeftFront.add(-z, y, x));

						if (world.isAirBlock(lowerLeftFront.add(-z, y, x))
								|| world.getBlockState(lowerLeftFront.add(-z, y, x)).getBlock() == this)
							continue;

						world.setBlockState(lowerLeftFront.add(-z, y, x),
								FABlocks.multiblockPart.ToBlock().getDefaultState());
						TileEntity te = world.getTileEntity(lowerLeftFront.add(-z, y, x));
						assert te instanceof TEMultiblockPart;
						TEMultiblockPart part = (TEMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z, 1 - z, y, x, st);

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(-z, y, x)).getBlock().getTranslationKey());
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
						BlockState st = world.getBlockState(lowerLeftFront.add(z, y, -x));

						if (world.isAirBlock(lowerLeftFront.add(z, y, -x))
								|| world.getBlockState(lowerLeftFront.add(z, y, -x)).getBlock() == this)
							continue;

						world.setBlockState(lowerLeftFront.add(z, y, -x),
								FABlocks.multiblockPart.ToBlock().getDefaultState());
						TileEntity te = world.getTileEntity(lowerLeftFront.add(z, y, -x));
						assert te instanceof TEMultiblockPart;
						TEMultiblockPart part = (TEMultiblockPart) te;
						part.SetMultiblockInformation(structurePattern, x, y, z, z - 1, y, -x, st);

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(z, y, -x)).getBlock().getTranslationKey());
					}
				}
			}
			break;
		}

		}
	}

	public void BreakStructure(World world, BlockPos pos)
	{
		MultiblockPart[][][] pattern = MultiblockHandler.Get(structurePattern).GetPattern();

		switch (world.getBlockState(pos).get(FACING))
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

						TileEntity te = world.getTileEntity(lowerLeftFront.add(x, y, z));

						if (te == null || !(te instanceof TEMultiblockPart))
							continue;

						TEMultiblockPart part = (TEMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(x, y, z),
								MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																							 .getDefaultState());

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(x, y, z)).getBlock().getTranslationKey());
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

						TileEntity te = world.getTileEntity(lowerLeftFront.add(-x, y, -z));

						if (te == null || !(te instanceof TEMultiblockPart))
							continue;

						TEMultiblockPart part = (TEMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(-x, y, -z),
								MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																							 .getDefaultState());

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(-x, y, -z)).getBlock().getTranslationKey());
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

						TileEntity te = world.getTileEntity(lowerLeftFront.add(-z, y, x));

						if (te == null || !(te instanceof TEMultiblockPart))
							continue;
						TEMultiblockPart part = (TEMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(-z, y, x),
								MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																							 .getDefaultState());

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(-z, y, x)).getBlock().getTranslationKey());
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

						TileEntity te = world.getTileEntity(lowerLeftFront.add(z, y, -x));

						if (te == null || !(te instanceof TEMultiblockPart))
							continue;
						TEMultiblockPart part = (TEMultiblockPart) te;
						if (!part.GetStructureId().equals(structurePattern))
							return;

						world.setBlockState(lowerLeftFront.add(z, y, -x),
								MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																							 .getDefaultState());

						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.add(z, y, -x)).getBlock().getTranslationKey());
					}
				}
			}
			break;
		}
		}
	}

	public void SetStructureCompleted(World world, BlockPos pos, boolean completed)
	{
		world.setBlockState(pos, world.getBlockState(pos).with(MULTIBLOCK_COMPLETE, completed));
	}
}