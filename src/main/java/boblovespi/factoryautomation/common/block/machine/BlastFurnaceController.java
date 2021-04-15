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
import java.util.Objects;

/**
 * Created by Willi on 11/11/2017.
 */
@SuppressWarnings("SpellCheckingInspection")
public class BlastFurnaceController extends Block
		implements FABlock /*, ITileEntityProvider, IMultiblockStructureController*/
{
	// TODO: implement tile entity stuff

	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");

	private final String structurePattern = "blast_furnace";

	public BlastFurnaceController() {
		super(Properties.of(Material.METAL).strength(10).harvestTool(ToolType.PICKAXE).harvestLevel(0));
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		// setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		// setHardness(10);
		// setResistance(10000);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MULTIBLOCK_COMPLETE, false));
		FABlocks.blocks.add(this);
		//		new FAItemBlock(this);
		FAItems.items.add(new BlockItem(this, new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS))
				.setRegistryName(Objects.requireNonNull(getRegistryName())));

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
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit)
	{
		if (!worldIn.isClientSide)
		{
			if (IsValidStructure(worldIn, pos, state))
			{
				if (!worldIn.getBlockState(pos).getValue(MULTIBLOCK_COMPLETE))
				{
					CreateStructure(worldIn, pos);
					worldIn.setBlockAndUpdate(pos, worldIn.getBlockState(pos).setValue(MULTIBLOCK_COMPLETE, true));
				}

				NetworkHooks
						.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(worldIn.getBlockEntity(pos)), pos);
			} else
			{
				if (worldIn.getBlockState(pos).getValue(MULTIBLOCK_COMPLETE))
				{
					BreakStructure(worldIn, pos);
					worldIn.setBlockAndUpdate(pos, worldIn.getBlockState(pos).setValue(MULTIBLOCK_COMPLETE, false));
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
						TEMultiblockPart te = null;
						if (world.getBlockEntity(lowerLeftFront.offset(x, y, z)) != null && world
								.getBlockEntity(lowerLeftFront.offset(x, y, z)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getBlockEntity(lowerLeftFront.offset(x, y, z));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.offset(x, y, z)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.offset(x, y, z)).getBlock().getDescriptionId());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getDescriptionId());
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
						if (world.getBlockEntity(lowerLeftFront.offset(-x, y, -z)) != null && world
								.getBlockEntity(lowerLeftFront.offset(-x, y, -z)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getBlockEntity(lowerLeftFront.offset(-x, y, -z));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.offset(-x, y, -z)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.offset(-x, y, -z)).getBlock().getDescriptionId());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getDescriptionId());
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
						if (world.getBlockEntity(lowerLeftFront.offset(-z, y, x)) != null && world
								.getBlockEntity(lowerLeftFront.offset(-z, y, x)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getBlockEntity(lowerLeftFront.offset(-z, y, x));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.offset(-z, y, x)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.offset(-z, y, x)).getBlock().getDescriptionId());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getDescriptionId());
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
						if (world.getBlockEntity(lowerLeftFront.offset(z, y, -x)) != null && world
								.getBlockEntity(lowerLeftFront.offset(z, y, -x)) instanceof TEMultiblockPart)
							te = (TEMultiblockPart) world.getBlockEntity(lowerLeftFront.offset(z, y, -x));

						if (pattern[x][y][z].GetBlock() != world.getBlockState(lowerLeftFront.offset(z, y, -x)).getBlock()
								&& !(te != null && te.GetStructureId().equals(structurePattern) && Arrays
								.equals(te.GetPosition(), new int[] { x, y, z })))
						{
							isValid = false;
						}
						Log.LogInfo("block in world",
								world.getBlockState(lowerLeftFront.offset(z, y, -x)).getBlock().getDescriptionId());
						Log.LogInfo("block in pattern", pattern[x][y][z].GetBlock().getDescriptionId());
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

		switch (world.getBlockState(pos).getValue(FACING)) {
			case WEST: {
				BlockPos lowerLeftFront = pos.north();

				for (int x = 0; x < pattern.length; x++) {
					for (int y = 0; y < pattern[x].length; y++) {
						for (int z = 0; z < pattern[x][y].length; z++) {
							BlockState st = world.getBlockState(lowerLeftFront.offset(x, y, z));

							if (world.isEmptyBlock(lowerLeftFront.offset(x, y, z))
									|| world.getBlockState(lowerLeftFront.offset(x, y, z)).getBlock() == this)
								continue;

							world.setBlockAndUpdate(lowerLeftFront.offset(x, y, z),
									FABlocks.multiblockPart.ToBlock().defaultBlockState());
							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(x, y, z));
							assert te instanceof TEMultiblockPart;
							TEMultiblockPart part = (TEMultiblockPart) te;
							part.SetMultiblockInformation(structurePattern, x, y, z, x, y, z - 1, st);

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(x, y, z)).getBlock().getDescriptionId());
						}
					}
				}
				break;
			}
			case EAST: {
				BlockPos lowerLeftFront = pos.south();

				for (int x = 0; x < pattern.length; x++) {
					for (int y = 0; y < pattern[x].length; y++) {
						for (int z = 0; z < pattern[x][y].length; z++) {
							BlockState st = world.getBlockState(lowerLeftFront.offset(-x, y, -z));

							if (world.isEmptyBlock(lowerLeftFront.offset(-x, y, -z))
									|| world.getBlockState(lowerLeftFront.offset(-x, y, -z)).getBlock() == this)
								continue;

							world.setBlockAndUpdate(lowerLeftFront.offset(-x, y, -z),
									FABlocks.multiblockPart.ToBlock().defaultBlockState());
							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(-x, y, -z));
							assert te instanceof TEMultiblockPart;
							TEMultiblockPart part = (TEMultiblockPart) te;
							part.SetMultiblockInformation(structurePattern, x, y, z, -x, y, -z + 1, st);

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(-x, y, -z)).getBlock().getDescriptionId());
						}
					}
				}
				break;
			}
			case NORTH: {
				BlockPos lowerLeftFront = pos.east();

				for (int x = 0; x < pattern.length; x++) {
					for (int y = 0; y < pattern[x].length; y++) {
						for (int z = 0; z < pattern[x][y].length; z++) {
							BlockState st = world.getBlockState(lowerLeftFront.offset(-z, y, x));

							if (world.isEmptyBlock(lowerLeftFront.offset(-z, y, x))
									|| world.getBlockState(lowerLeftFront.offset(-z, y, x)).getBlock() == this)
								continue;

							world.setBlockAndUpdate(lowerLeftFront.offset(-z, y, x),
									FABlocks.multiblockPart.ToBlock().defaultBlockState());
							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(-z, y, x));
							assert te instanceof TEMultiblockPart;
							TEMultiblockPart part = (TEMultiblockPart) te;
							part.SetMultiblockInformation(structurePattern, x, y, z, 1 - z, y, x, st);

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(-z, y, x)).getBlock().getDescriptionId());
						}
					}
				}
				break;
			}
			case SOUTH: {
				BlockPos lowerLeftFront = pos.west();

				for (int x = 0; x < pattern.length; x++) {
					for (int y = 0; y < pattern[x].length; y++) {
						for (int z = 0; z < pattern[x][y].length; z++) {
							BlockState st = world.getBlockState(lowerLeftFront.offset(z, y, -x));

							if (world.isEmptyBlock(lowerLeftFront.offset(z, y, -x))
									|| world.getBlockState(lowerLeftFront.offset(z, y, -x)).getBlock() == this)
								continue;

							world.setBlockAndUpdate(lowerLeftFront.offset(z, y, -x),
									FABlocks.multiblockPart.ToBlock().defaultBlockState());
							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(z, y, -x));
							assert te instanceof TEMultiblockPart;
							TEMultiblockPart part = (TEMultiblockPart) te;
							part.SetMultiblockInformation(structurePattern, x, y, z, z - 1, y, -x, st);

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(z, y, -x)).getBlock().getDescriptionId());
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

		switch (world.getBlockState(pos).getValue(FACING)) {
			case WEST: {
				BlockPos lowerLeftFront = pos.north();

				for (int x = 0; x < pattern.length; x++) {
					for (int y = 0; y < pattern[x].length; y++) {
						for (int z = 0; z < pattern[x][y].length; z++) {
							if (world.isEmptyBlock(lowerLeftFront.offset(x, y, z)))
								continue;

							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(x, y, z));

							// Todo: remove null check, it shouldn't be null anyways.
							if (te == null || !(te instanceof TEMultiblockPart))
								continue;

							TEMultiblockPart part = (TEMultiblockPart) te;
							if (!part.GetStructureId().equals(structurePattern))
								return;

							world.setBlockAndUpdate(lowerLeftFront.offset(x, y, z),
									MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																								 .defaultBlockState());

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(x, y, z)).getBlock().defaultBlockState());
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
							if (world.isEmptyBlock(lowerLeftFront.offset(-x, y, -z)))
								continue;

							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(-x, y, -z));

							// Todo: remove null check, it shouldn't be null anyways.
							if (te == null || !(te instanceof TEMultiblockPart))
								continue;

							TEMultiblockPart part = (TEMultiblockPart) te;
							if (!part.GetStructureId().equals(structurePattern))
								return;

							world.setBlockAndUpdate(lowerLeftFront.offset(-x, y, -z),
									MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																								 .defaultBlockState());

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(-x, y, -z)).getBlock().getDescriptionId());
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
							if (world.isEmptyBlock(lowerLeftFront.offset(-z, y, x)))
								continue;

							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(-z, y, x));

							// Todo: remove null check, it shouldn't be null anyways.
							if (te == null || !(te instanceof TEMultiblockPart))
								continue;
							TEMultiblockPart part = (TEMultiblockPart) te;
							if (!part.GetStructureId().equals(structurePattern))
								return;

							world.setBlockAndUpdate(lowerLeftFront.offset(-z, y, x),
									MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																								 .defaultBlockState());

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(-z, y, x)).getBlock().getDescriptionId());
						}
					}
				}
				break;
			}
			case SOUTH: {
				BlockPos lowerLeftFront = pos.west();

				for (int x = 0; x < pattern.length; x++) {
					for (int y = 0; y < pattern[x].length; y++) {
						for (int z = 0; z < pattern[x][y].length; z++) {
							if (world.isEmptyBlock(lowerLeftFront.offset(z, y, -x)))
								continue;

							TileEntity te = world.getBlockEntity(lowerLeftFront.offset(z, y, -x));

							// Todo: remove null check, it shouldn't be null anyways.
							if (te == null || !(te instanceof TEMultiblockPart))
								continue;
							TEMultiblockPart part = (TEMultiblockPart) te;
							if (!part.GetStructureId().equals(structurePattern))
								return;

							world.setBlockAndUpdate(lowerLeftFront.offset(z, y, -x),
									MultiblockHandler.Get(structurePattern).GetPattern()[x][y][z].GetBlock()
																								 .defaultBlockState());

							Log.LogInfo("block in world",
									world.getBlockState(lowerLeftFront.offset(z, y, -x)).getBlock().getDescriptionId());
						}
					}
				}
				break;
			}
		}
	}

	public void SetStructureCompleted(World world, BlockPos pos, boolean completed)
	{
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(MULTIBLOCK_COMPLETE, completed));
	}
}