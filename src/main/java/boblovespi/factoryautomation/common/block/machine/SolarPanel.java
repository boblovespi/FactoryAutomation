package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.api.energy.electricity.IEnergyBlock;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/21/2017.
 * solar panels!
 */
public class SolarPanel extends FABaseBlock implements IEnergyBlock
{
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.21875, 1);

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
	 * @param world The world access
	 * @param pos   The position of the block
	 * @return Whether or not a cable can attach to the given side and state
	 */
	@Override
	public boolean CanConnectCable(BlockState state, Direction side, IBlockReader world, BlockPos pos)
	{
		return side != null && side.getHorizontalIndex() >= 0;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileEntitySolarPanel();
	}

	//	@Override
	//	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn,
	//			EnumHand hand, Direction facing, float hitX, float hitY, float hitZ)
	//	{
	//		TileEntity entity;
	//		if ((entity = worldIn.getTileEntity(pos)) instanceof TileEntitySolarPanel)
	//		{
	//			TileEntitySolarPanel entity1 = (TileEntitySolarPanel) (entity);
	//			entity1.ForceUpdate();
	//			if (!worldIn.isRemote)
	//				FactoryAutomation.proxy.AddChatMessage(
	//						ChatType.GAME_INFO, new TextComponentString(
	//								"Power: " + entity1.AmountProduced() + " | Power generated - used: " + entity1
	//										.ActualAmountProduced()));
	//			Log.LogInfo("Can see sky", worldIn.canBlockSeeSky(pos));
	//			Log.LogInfo("Sunlight factor", worldIn.getSunBrightnessFactor(0));
	//			Log.LogInfo("Can block above see sky", worldIn.canBlockSeeSky(pos.up()));
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
