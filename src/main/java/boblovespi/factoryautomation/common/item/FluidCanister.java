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
//	public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player,
//			@Nonnull EnumHand hand)
//	{
//		return ActionResult.newResult(EnumActionResult.PASS, player.getItemInHand(hand));
//	}
//
//	/**
//	 * Called when a Block is right-clicked with this Item
//	 */
//	@Override
//	public EnumActionResult useOn(PlayerEntity player, World levelIn, BlockPos pos, EnumHand hand,
//			Direction facing, float hitX, float hitY, float hitZ)
//	{
//		return super.useOn(player, levelIn, pos, hand, facing, hitX, hitY, hitZ);
//	}
//}
