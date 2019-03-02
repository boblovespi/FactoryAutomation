package boblovespi.factoryautomation.common.util.patchouli;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.multiblock.MultiblockPart;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

/**
 * Created by Willi on 1/1/2019.
 */
public class PatchouliHelper
{
	public static void RegisterMultiblock(ResourceLocation id, MultiblockStructurePattern multiblock)
	{
		MultiblockPart[][][] pattern = multiblock.GetPattern();
		String[][] stringPattern = new String[pattern.length][];

		// PatchouliAPI.instance.makeMultiblock()
	}

	public static void RegisterMultiblocks()
	{
		IMultiblock stoneFoundry = PatchouliAPI.instance
				.makeMultiblock(new String[][] { new String[] { "a_" }, new String[] { "0b" } }, 'a',
						FABlocks.stoneCrucible, '0', Blocks.FURNACE, 'b', FABlocks.stoneCastingVessel);
		PatchouliAPI.instance
				.registerMultiblock(new ResourceLocation(FactoryAutomation.MODID, "stone_foundry"), stoneFoundry);
	}

	public static ItemStack GetGuidebook()
	{
		return PatchouliAPI.instance.getBookStack(FactoryAutomation.MODID+":book");
	}
}
