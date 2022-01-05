package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TEWoodenTank;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class WoodenTank extends FABaseBlock implements EntityBlock
{
	public WoodenTank()
	{
		super("wooden_tank", false, BlockBehaviour.Properties.of(Material.WOOD).strength(1f), new Item.Properties().tab(FAItemGroups.fluid));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEWoodenTank(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;
		var te = world.getBlockEntity(pos);
		if (te instanceof TEWoodenTank tank)
		{
			if (!FluidUtil.tryEmptyContainerAndStow(player.getItemInHand(hand), tank.GetHandler(), null, 1000, player, true).isSuccess())
				FluidUtil.tryFillContainerAndStow(player.getItemInHand(hand), tank.GetHandler(), null, 1000, player, true);
		}
		return InteractionResult.SUCCESS;
	}
}
