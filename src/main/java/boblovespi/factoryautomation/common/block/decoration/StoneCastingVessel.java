package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
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
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.5d, 1);

	public StoneCastingVessel()
	{
		super("stone_casting_vessel", false,
				Properties.create(Material.ROCK).hardnessAndResistance(1.5f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().group(FAItemGroups.primitive));
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

	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	public boolean isFullCube(BlockState state)
	{
		return false;
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
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (!world.isRemote)
		{
			if (player.getHeldItem(hand).getItem() == Items.STICK && player instanceof ServerPlayerEntity)
			{
				// player.openGui(FactoryAutomation.instance, GuiHandler.GuiID.STONE_CASTING_VESSEL.id, world, pos.getX(),
				// 		pos.getY(), pos.getZ());
				NetworkHooks.openGui((ServerPlayerEntity) player, TEHelper.GetContainer(world.getTileEntity(pos)), pos);
			} else
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TEStoneCastingVessel)
					((TEStoneCastingVessel) te).TakeOrPlace(player.getHeldItem(hand), player);
			}
		}
		return true;
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