package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.energy.electricity.IEnergyBlock;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 12/21/2017.
 * solar panels!
 */
public class SolarPanel extends FABaseBlock implements IEnergyBlock, ITileEntityProvider
{
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.21875, 1);

	public SolarPanel()
	{
		super(Material.DRAGON_EGG, "solar_panel", null);
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
	public boolean CanConnectCable(BlockState state, Direction side, IBlockAccess world, BlockPos pos)
	{
		return side != null && side.getHorizontalIndex() >= 0;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	@ParametersAreNonnullByDefault
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntitySolarPanel();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn,
			EnumHand hand, Direction facing, float hitX, float hitY, float hitZ)
	{
		TileEntity entity;
		if ((entity = worldIn.getTileEntity(pos)) instanceof TileEntitySolarPanel)
		{
			TileEntitySolarPanel entity1 = (TileEntitySolarPanel) (entity);
			entity1.ForceUpdate();
			if (!worldIn.isRemote)
				FactoryAutomation.proxy.AddChatMessage(
						ChatType.GAME_INFO, new TextComponentString(
								"Power: " + entity1.AmountProduced() + " | Power generated - used: " + entity1
										.ActualAmountProduced()));
			Log.LogInfo("Can see sky", worldIn.canBlockSeeSky(pos));
			Log.LogInfo("Sunlight factor", worldIn.getSunBrightnessFactor(0));
			Log.LogInfo("Can block above see sky", worldIn.canBlockSeeSky(pos.up()));
			Log.LogInfo("Power generated", entity1.AmountProduced());
			Log.LogInfo("Actual power generated", entity1.ActualAmountProduced());
		}
		return true;
	}

	/**
	 * @param state Unused
	 * @return true if the state occupies all of its 1x1x1 cube
	 */
	@Override
	public boolean isFullBlock(BlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	@Override
	public boolean isFullCube(BlockState state)
	{
		return false;
	}
}
