package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 11/26/2017.
 */
@SuppressWarnings({"DanglingJavadoc", "CommentedOutCode", "SpellCheckingInspection"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultiblockComponent extends FABaseBlock implements EntityBlock
{
	public MultiblockComponent()
	{
		super("multiblock_part", true, Properties.of(Material.METAL).strength(1.5f)
				.isRedstoneConductor(MultiblockComponent::isRedstoneConductor), new Item.Properties());
		//		setUnlocalizedName(UnlocalizedName());
		//		setRegistryName(RegistryName());
		//		setLightOpacity(0);
		//		setHardness(1.5f);
		//		setHarvestLevel("pickaxe", 0);
		//		FABlocks.blocks.add(this);
	}

	// Todo: translate this to 1.16.5 with mojmaps.
	public static boolean isRedstoneConductor(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return false;
	}

	/**
	 * Check if the face of a block should block rendering.
	 * <p>
	 * Faces which are fully opaque should return true, faces with transparency
	 * or faces which do not span the full size of the block should return false.
	 */
	//	@Override
	//	public boolean isSideInvisible(BlockState state, IEnviromentBlockReader level, BlockPos pos, Direction face)
	//	{
	//		return false;
	//	}
	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type,
								EntityType<?> entityType)
	{
		return false;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEMultiblockPart(pos, state);
	}

	//	public void getDrops(NonNullList<ItemStack> drops, ServerWorld level, BlockPos pos, BlockState state, int fortune)
	//	{
	//		Random rand = level != null ? level.random : RANDOM;
	//		TileEntity te = level.getBlockEntity(pos);
	//
	//		assert te != null && te instanceof TEMultiblockPart;
	//
	//		TEMultiblockPart part = (TEMultiblockPart) te;
	//		MultiblockStructurePattern structure = MultiblockHandler.Get(part.GetStructureId());
	//		int[] loc = part.GetPosition();
	//		Block block = structure.GetPattern()[loc[0]][loc[1]][loc[2]].GetBlock();
	//		Item item = block.getItemDropped(block.getDefaultState(), rand, fortune);
	//		if (item != Items.AIR)
	//			drops.add(new ItemStack(item));
	//	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		//		Log.LogInfo("something broke!");
		//		Log.LogInfo("blockPos", pos);
		//
		//		TEMultiblockPart part = (TEMultiblockPart) level.getBlockEntity(pos);
		//
		//		Log.LogInfo("part is null", part == null);
		//
		//		if (part != null)
		//		{
		//			MultiblockStructurePattern structure = MultiblockHandler.Get(part.GetStructureId());
		//			int[] offset = part.GetOffset();
		//
		//			BlockPos controllerLoc = pos.add(-offset[0], -offset[1], -offset[2]);
		//
		//			Log.LogInfo("offset", Arrays.toString(offset));
		//
		//			Block controller = level.getBlockState(controllerLoc).getBlock();
		//			Log.LogInfo("is controller", controller instanceof IMultiblockStructureController);
		//
		//			if (controller instanceof IMultiblockStructureController)
		//			{
		//				((IMultiblockStructureController) controller).BreakStructure(world, controllerLoc);
		//				((IMultiblockStructureController) controller).SetStructureCompleted(world, controllerLoc, false);
		//			}
		//		}
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	//	@Override
	//	public boolean isOpaqueCube(BlockState state)
	//	{
	//		return false;
	//	}
	//
	//	@Override
	//	public boolean isFullCube(BlockState state)
	//	{
	//		return false;
	//	}
}
