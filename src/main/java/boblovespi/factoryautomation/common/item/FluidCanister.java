package boblovespi.factoryautomation.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.UniversalBucket;

import javax.annotation.Nonnull;

/**
 * Created by Willi on 4/26/2018.
 */
public class FluidCanister extends UniversalBucket implements FAItem
{
	private String name;

	public FluidCanister(String name, int capacity)
	{
		super(capacity, new ItemStack(FAItems.fluidCanister.ToItem()), true);
		this.name = name;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		FAItems.items.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return name;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return RegistryName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player,
			@Nonnull EnumHand hand)
	{
		return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
