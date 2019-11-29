package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TESolidFueledFirebox;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 10/28/2018.
 */
public class BrickFirebox extends FABaseBlock /*implements ITileEntityProvider*/
{
	public BrickFirebox()
	{
		super(Material.IRON, "brick_firebox", FAItemGroups.heat);
		TileEntityHandler.tiles.add(TESolidFueledFirebox.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		//		if (!worldIn.isRemote)
		//			playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.SOLID_FUELED_FIREBOX.id, worldIn, pos.getX(),
		//					pos.getY(), pos.getZ());
		return true;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	//	@Nullable
	//	@Override
	//	public TileEntity createNewTileEntity(World worldIn, int meta)
	//	{
	//		return new TEBrickFirebox();
	//	}
}
