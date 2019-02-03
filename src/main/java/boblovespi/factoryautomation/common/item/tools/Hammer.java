package boblovespi.factoryautomation.common.item.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by Willi on 5/13/2018.
 */
public class Hammer extends WorkbenchToolItem
{
	public Hammer(String name, float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn,
			Set<Block> effectiveBlocksIn)
	{
		super(name, attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
		setHarvestLevel("hammer", materialIn.getHarvestLevel());
	}

	public Hammer(String name, float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn)
	{
		super(name, attackDamageIn, attackSpeedIn, materialIn);
		setHarvestLevel("hammer", materialIn.getHarvestLevel());
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player,
			@Nullable IBlockState blockState)
	{
		if (blockState != null)
		{
			if (blockState.getBlock() == Blocks.STONE
					&& blockState.getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE)
			{
				return toolMaterial.getHarvestLevel();
			}
		}
		if (this.getToolClasses(stack).contains(toolClass))
			return toolMaterial.getHarvestLevel();
		return -1;
	}
}
