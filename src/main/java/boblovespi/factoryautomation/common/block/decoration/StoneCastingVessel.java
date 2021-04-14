package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible.MetalForms;

/**
 * Created by Willi on 12/22/2018.
 */
public class StoneCastingVessel extends FABaseBlock
{
	public static final EnumProperty<CastingVesselStates> MOLD = EnumProperty.create("mold", CastingVesselStates.class);
	private static final VoxelShape BOUNDING_BOX_NO_SAND = VoxelShapes
			.or(Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(0, 0, 1, 1, 8, 15),
					Block.makeCuboidShape(15, 0, 1, 16, 8, 15), Block.makeCuboidShape(1, 0, 0, 15, 8, 1),
					Block.makeCuboidShape(1, 0, 15, 15, 8, 16)).simplify();
	private static final VoxelShape BOUNDING_BOX_SAND = VoxelShapes
			.or(BOUNDING_BOX_NO_SAND, Block.makeCuboidShape(1, 0, 1, 15, 7, 15));

	public StoneCastingVessel()
	{
		super("stone_casting_vessel", false,
				Properties.of(Material.STONE).strength(1.5f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().tab(FAItemGroups.primitive));
		registerDefaultState(stateDefinition.getBaseState().with(MOLD, CastingVesselStates.EMPTY));
		TileEntityHandler.tiles.add(TEStoneCastingVessel.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(MOLD);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if (state.get(MOLD) == CastingVesselStates.EMPTY)
			return BOUNDING_BOX_NO_SAND;
		return BOUNDING_BOX_SAND;
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
		return new TEStoneCastingVessel();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		if (!world.isRemote)
		{
			TEStoneCastingVessel te = (TEStoneCastingVessel) world.getTileEntity(pos);

			if (player.getHeldItem(hand).getItem() == Items.STICK && player instanceof ServerPlayerEntity && te != null && te.HasSpace())
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(world.getTileEntity(pos)), pos);
			} else
			{
				if (te != null)
					te.TakeOrPlace(player.getHeldItem(hand), player);
			}
		}
		return ActionResultType.SUCCESS;
	}

	public enum CastingVesselStates implements IStringSerializable
	{
		EMPTY(MetalForms.NONE),
		SAND(MetalForms.NONE),
		INGOT(MetalForms.INGOT),
		NUGGET(MetalForms.NUGGET),
		SHEET(MetalForms.SHEET),
		COIN(MetalForms.COIN),
		ROD(MetalForms.ROD),
		GEAR(MetalForms.GEAR);
		public final MetalForms metalForm;

		CastingVesselStates(MetalForms metalForm)
		{
			this.metalForm = metalForm;
		}

		@Override
		public String getString()
		{
			return name().toLowerCase();
		}

		@Override
		public String toString()
		{
			return getString();
		}
	}
}