package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/22/2018.
 */
public class BrickCastingVessel extends FABaseBlock implements EntityBlock
{
	public static final EnumProperty<CastingVesselStates> MOLD = EnumProperty.create("mold", CastingVesselStates.class);

	public BrickCastingVessel()
	{
		super("brick_casting_vessel", false,
				Properties.of(Material.STONE).strength(2f).requiresCorrectToolForDrops(), new Item.Properties().tab(FAItemGroups.metallurgy));
		// super(Material.STONE, "brick_casting_vessel", FAItemGroups.metallurgy);
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

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEStoneCastingVessel(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
								 InteractionHand hand, BlockHitResult hit)
	{
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TEStoneCastingVessel && !world.isClientSide)
		{
			TEStoneCastingVessel vessel = (TEStoneCastingVessel) te;
			if (player.getItemInHand(hand).getItem() == Items.STICK)
			{
				NetworkHooks.openGui((ServerPlayer) player, vessel, pos);
			} else
			{
				vessel.TakeOrPlace(player.getItemInHand(hand), player);
			}
		}
		return InteractionResult.SUCCESS;
	}
}