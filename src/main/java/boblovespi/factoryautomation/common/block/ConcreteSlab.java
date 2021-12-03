package boblovespi.factoryautomation.common.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

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
		super(Properties.of(Material.STONE).strength(10, 1000));
		//		super(Material.STONE);
		//		isDouble = isDoubleSlab;
		//		BlockState state = blockState.getBaseState();
		//		if (!isDouble())
		//			state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
		//
		//		registerDefaultState(state.withProperty(VARIANT, Variant.DEFAULT));
		//
		//		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		//		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		//		setHardness(10);
		//		setResistance(10000);

		FABlocks.blocks.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return "concrete_slab";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public void stepOn(Level level, BlockPos pos, Entity entity)
	{
		if (entity instanceof Player)
		{
			((Player) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 1));
		}
	}
}
