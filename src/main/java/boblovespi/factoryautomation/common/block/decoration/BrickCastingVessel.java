package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/22/2018.
 */
public class BrickCastingVessel extends FABaseBlock
{
	public static final EnumProperty<CastingVesselStates> MOLD = EnumProperty.create("mold", CastingVesselStates.class);

	public BrickCastingVessel()
	{
		super("brick_casting_vessel", false,
				Properties.of(Material.STONE).strength(1.5f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().tab(FAItemGroups.metallurgy));
		// super(Material.STONE, "brick_casting_vessel", FAItemGroups.metallurgy);
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
		TileEntity te = level.getBlockEntity(pos);
		if (te instanceof TEStoneCastingVessel && !level.isClientSide)
		{
			TEStoneCastingVessel vessel = (TEStoneCastingVessel) te;
			if (player.getItemInHand(hand).getItem() == Items.STICK)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, vessel, pos);
			} else
			{
				vessel.TakeOrPlace(player.getItemInHand(hand), player);
			}
		}
		return ActionResultType.SUCCESS;
	}
}