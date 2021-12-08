package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible.MetalForms;

import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible.MetalForms;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 12/22/2018.
 */
public class StoneCastingVessel extends FABaseBlock
{
	public static final EnumProperty<CastingVesselStates> MOLD = EnumProperty.create("mold", CastingVesselStates.class);
	private static final VoxelShape BOUNDING_BOX_NO_SAND = Shapes
			.or(Block.box(1, 0, 1, 15, 1, 15), Block.box(0, 0, 1, 1, 8, 15),
					Block.box(15, 0, 1, 16, 8, 15), Block.box(1, 0, 0, 15, 8, 1),
					Block.box(1, 0, 15, 15, 8, 16)).optimize();
	private static final VoxelShape BOUNDING_BOX_SAND = Shapes
			.or(BOUNDING_BOX_NO_SAND, Block.box(1, 0, 1, 15, 7, 15));

	public StoneCastingVessel()
	{
		super("stone_casting_vessel", false,
				Properties.of(Material.STONE).strength(1.5f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().tab(FAItemGroups.primitive));
		registerDefaultState(stateDefinition.any().setValue(MOLD, CastingVesselStates.EMPTY));
		TileEntityHandler.tiles.add(TEStoneCastingVessel.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MOLD);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
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
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TEStoneCastingVessel();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit)
	{
		if (!world.isClientSide)
		{
			TEStoneCastingVessel te = (TEStoneCastingVessel) world.getBlockEntity(pos);

			if (player.getItemInHand(hand).getItem() == Items.STICK && player instanceof ServerPlayer && te != null && te.HasSpace())
			{
				NetworkHooks.openGui((ServerPlayer) player, TEHelper.GetContainer(world.getBlockEntity(pos)), pos);
			} else
			{
				if (te != null)
					te.TakeOrPlace(player.getItemInHand(hand), player);
			}
		}
		return InteractionResult.SUCCESS;
	}

	public enum CastingVesselStates implements StringRepresentable
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
		public String getSerializedName()
		{
			return name().toLowerCase();
		}

		@Override
		public String toString()
		{
			return getSerializedName();
		}
	}
}