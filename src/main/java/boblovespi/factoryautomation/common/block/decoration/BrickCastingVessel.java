package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
				Properties.create(Material.ROCK).hardnessAndResistance(1.5f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().group(FAItemGroups.metallurgy));
		// super(Material.ROCK, "brick_casting_vessel", FAItemGroups.metallurgy);
		setDefaultState(stateContainer.getBaseState().with(MOLD, CastingVesselStates.EMPTY));
		TileEntityHandler.tiles.add(TEStoneCastingVessel.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
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
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TEStoneCastingVessel();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		//		if (!world.isRemote)
		//		{
		//			if (player.getHeldItem(hand).getItem() == Items.STICK)
		//			{
		//				player.openGui(FactoryAutomation.instance, GuiHandler.GuiID.STONE_CASTING_VESSEL.id, world, pos.getX(),
		//						pos.getY(), pos.getZ());
		//			} else
		//			{
		//				TileEntity te = world.getTileEntity(pos);
		//				if (te instanceof TEStoneCastingVessel)
		//					((TEStoneCastingVessel) te).TakeOrPlace(player.getHeldItem(hand), player);
		//			}
		//		}
		return ActionResultType.SUCCESS;
	}
}