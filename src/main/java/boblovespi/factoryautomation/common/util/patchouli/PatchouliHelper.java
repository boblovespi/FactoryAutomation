package boblovespi.factoryautomation.common.util.patchouli;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.multiblock.MultiblockPart;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

/**
 * Created by Willi on 1/1/2019.
 */
public class PatchouliHelper
{
	private static final ResourceLocation BOOK = new ResourceLocation(FactoryAutomation.MODID, "book");

	public static void RegisterMultiblock(ResourceLocation id, MultiblockStructurePattern multiblock)
	{
		MultiblockPart[][][] pattern = multiblock.GetPattern();
		String[][] stringPattern = new String[pattern.length][];

		// PatchouliAPI.instance.makeMultiblock()
	}

	public static void RegisterMultiblocks()
	{
		IMultiblock stoneFoundry = PatchouliAPI.get()
				.makeMultiblock(new String[][] { new String[] { "a_" }, new String[] { "0b" } }, 'a',
						FABlocks.stoneCrucible, '0', Blocks.FURNACE, 'b', FABlocks.stoneCastingVessel);
		PatchouliAPI.get()
				.registerMultiblock(new ResourceLocation(FactoryAutomation.MODID, "stone_foundry"), stoneFoundry);
	}

	public static ItemStack GetGuidebook()
	{
		return PatchouliAPI.get().getBookStack(BOOK);
	}
}
