package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.api.energy.electricity.IEnergyBlock;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/21/2017.
 * solar panels!
 */
public class SolarPanel extends FABaseBlock implements IEnergyBlock, EntityBlock
{
	private static final AABB BOUNDING_BOX = new AABB(0, 0, 0, 1, 0.21875, 1);

	public SolarPanel()
	{
		super(
				"solar_panel", false,
				Properties.of(Material.METAL).strength(2).requiresCorrectToolForDrops(),
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileEntitySolarPanel(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
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
}
