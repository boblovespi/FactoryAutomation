package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.energy.electricity.IEnergyBlock;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.core.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 12/21/2017.
 * solar panels!
 */
public class SolarPanel extends FABaseBlock implements IEnergyBlock
{
	private static final AABB BOUNDING_BOX = new AABB(0, 0, 0, 1, 0.21875, 1);

	public SolarPanel()
	{
		super(
				"solar_panel", false,
				Properties.of(Material.METAL).strength(2).harvestTool(ToolType.PICKAXE).harvestLevel(0),
				new Item.Properties().tab(FAItemGroups.electrical));
	}

	/**
	 * Checks whether or not a cable can connect to the given side and state
	 *
	 * @param state The state of the machine
	 * @param side  The side power is being connected to
	 * @param level The level access
	 * @param pos   The position of the block
	 * @return Whether or not a cable can attach to the given side and state
	 */
	@Override
	public boolean CanConnectCable(BlockState state, Direction side, BlockGetter level, BlockPos pos)
	{
		return side != null && side.get2DDataValue() >= 0;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TileEntitySolarPanel();
	}

//	@Override
//	public ActionResultType use(BlockState state, World levelIn, BlockPos pos, PlayerEntity playerIn,
//					   Hand hand, BlockRayTraceResult rayTraceResult)
//	{
//		TileEntity entity;
//		if ((entity = levelIn.getBlockEntity(pos)) instanceof TileEntitySolarPanel)
//		{
//			TileEntitySolarPanel entity1 = (TileEntitySolarPanel) (entity);
//			entity1.ForceUpdate();
//			if (!worldIn.isClientSide)
//				FactoryAutomation.proxy.AddChatMessage(
//						ChatType.GAME_INFO, new StringTextComponent(
//								"Power: " + entity1.AmountProduced() + " | Power generated - used: " + entity1
//										.ActualAmountProduced()));
//			Log.LogInfo("Can see sky", levelIn.canSeeSkyFromBelowWater(pos));
//			Log.LogInfo("Sunlight factor", levelIn.getSunAngle(0));
//			Log.LogInfo("Can block above see sky", levelIn.canSeeSkyFromBelowWater(pos.up()));
//			Log.LogInfo("Power generated", entity1.AmountProduced());
//			Log.LogInfo("Actual power generated", entity1.ActualAmountProduced());
//		}
//		return true;
//	}

	/**
	 * Called throughout the code as a replacement for block instanceof BlockContainer
	 * Moving this to the Block base class allows for mods that wish to extend vanilla
	 * blocks, and also want to have a tile entity on that block, may.
	 * <p>
	 * Return true from this function to specify this block has a tile entity.
	 *
	 * @param state State of the current block
	 * @return True if block has a tile entity, false otherwise
	 */
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
}
