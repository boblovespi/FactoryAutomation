package boblovespi.factoryautomation.common.item;

/**
 * Created by Willi on 4/26/2018.
 */
public class FluidCanister {}
//extends UniversalBucket implements FAItem
//{
//	private String name;
//
//	public FluidCanister(String name, int capacity)
//	{
//		super(capacity, new ItemStack(FAItems.fluidCanister.ToItem()), true);
//		this.name = name;
//		setUnlocalizedName(UnlocalizedName());
//		setRegistryName(RegistryName());
//		FAItems.items.add(this);
//	}
//
//	@Override
//	public String UnlocalizedName()
//	{
//		return name;
//	}
//
//	@Override
//	public String GetMetaFilePath(int meta)
//	{
//		return RegistryName();
//	}
//
//	@Override
//	public Item ToItem()
//	{
//		return this;
//	}
//
//	/**
//	 * Called when the equipped item is right clicked.
//	 */
//	@Nonnull
//	@Override
//	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player,
//			@Nonnull EnumHand hand)
//	{
//		return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
//	}
//
//	/**
//	 * Called when a Block is right-clicked with this Item
//	 */
//	@Override
//	public EnumActionResult onItemUse(PlayerEntity player, World worldIn, BlockPos pos, EnumHand hand,
//			Direction facing, float hitX, float hitY, float hitZ)
//	{
//		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
//	}
//}
