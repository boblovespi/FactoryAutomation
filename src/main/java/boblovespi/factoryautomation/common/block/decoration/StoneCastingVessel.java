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
			.or(Block.box(1, 0, 1, 15, 1, 15), Block.box(0, 0, 1, 1, 8, 15),
					Block.box(15, 0, 1, 16, 8, 15), Block.box(1, 0, 0, 15, 8, 1),
					Block.box(1, 0, 15, 15, 8, 16)).optimize();
	private static final VoxelShape BOUNDING_BOX_SAND = VoxelShapes
			.or(BOUNDING_BOX_NO_SAND, Block.box(1, 0, 1, 15, 7, 15));

	public StoneCastingVessel()
	{
		super("stone_casting_vessel", false,
				Properties.of(Material.STONE).strength(1.5f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().tab(FAItemGroups.primitive));
		registerDefaultState(stateDefinition.any().with(MOLD, CastingVesselStates.EMPTY));
		TileEntityHandler.tiles.add(TEStoneCastingVessel.class);
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "processing/" + registryName();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(MOLD);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader levelIn, BlockPos pos, ISelectionContext context)
	{
		if (state.getValue(MOLD) == CastingVesselStates.EMPTY)
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
	public TileEntity createTileEntity(BlockState state, IBlockReader level)
	{
		return new TEStoneCastingVessel();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		if (!level.isClientSide)
		{
			TEStoneCastingVessel te = (TEStoneCastingVessel) level.getBlockEntity(pos);

			if (player.getItemInHand(hand).getItem() == Items.STICK && player instanceof ServerPlayerEntity && te != null && te.hasSpace())
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(level.getBlockEntity(pos)), pos);
			} else
			{
				if (te != null)
					te.TakeOrPlace(player.getItemInHand(hand), player);
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
		public String getName()
		{
			return name().toLowerCase();
		}

		@Override
		public String toString()
		{
			return getName();
		}
	}
}