package boblovespi.factoryautomation.common.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Concrete slab block.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ConcreteSlab extends SlabBlock implements FABlock
{
	//Todo: Finish
	public ConcreteSlab()
	{
		super(Properties.generate(Material.ROCK).strength(10, 1000));
		//		super(Material.STONE);
		//		isDouble = isDoubleSlab;
		//		BlockState state = blockState.getBaseState();
		//		if (!isDouble())
		//			state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
		//
		//		registerDefaultState(state.withProperty(VARIANT, Variant.DEFAULT));
		//
		//		setUnlocalizedName(UnlocalizedName());
		setRegistryName(registryName());
		//		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		//		setHardness(10);
		//		setResistance(10000);

		FABlocks.blocks.add(this);
	}

	@Override
	public String unlocalizedName()
	{
		return "concrete_slab";
	}

	@Override
	public Block toBlock()
	{
		return this;
	}

	@Override
	public void onEntityWalk(World dimensionIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof PlayerEntity) {
			((PlayerEntity) entityIn).addPotionEffect(new EffectInstance(Effects.SPEED, 10, 1));
		}
	}
}
