package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Willi on 12/23/2017.
 */
public abstract class MultiStateBlock<T extends Enum<T> & IMultiTypeEnum & IStringSerializable> extends Block
		implements FABlock
{
	protected final Class<T> blockTypes;
	private final String registeryName;

	public /*final*/ PropertyEnum<T> TYPE;
	private String resourceFolder;

	public MultiStateBlock(Material blockMaterialIn, MapColor blockMapColorIn, String registeryName, Class<T> types,
			String resourceFolder, CreativeTabs ct)
	{
		super(blockMaterialIn, blockMapColorIn);
		this.registeryName = registeryName;
		blockTypes = types;

		this.resourceFolder = resourceFolder;
		TYPE = PropertyEnum.create("type", types);

		setCreativeTab(ct);

		setDefaultState(blockState.getBaseState().withProperty(TYPE, blockTypes.getEnumConstants()[0]));
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		FABlocks.blocks.add(this);
	}

	public MultiStateBlock(Material materialIn, String registeryName, Class<T> types, String resourceFolder)
	{
		this(materialIn, materialIn.getMaterialMapColor(), registeryName, types, resourceFolder,
				CreativeTabs.BUILDING_BLOCKS);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for (int i = 0; i < blockTypes.getEnumConstants().length; i++)
		{
			items.add(new ItemStack(this, 1, i));
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TYPE, blockTypes.getEnumConstants()[meta]);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE).GetId();
	}

	@Override
	public String UnlocalizedName()
	{
		return registeryName;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		String folder = (resourceFolder == null || resourceFolder.isEmpty()) ? "" : resourceFolder + "/";

		T[] types = blockTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (meta == i)
				return folder + UnlocalizedName() + "_" + types[i].getName();

		return folder + UnlocalizedName() + "_" + types[0].getName();
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if (TYPE == null)
			SetType();
		return new BlockStateContainer(this, TYPE);
	}

	protected abstract void SetType();

	/**
	 * Gets the {@link IBlockState} to place
	 *
	 * @param world  The world the block is being placed in
	 * @param pos    The position the block is being placed at
	 * @param facing The side the block is being placed on
	 * @param hitX   The X coordinate of the hit vector
	 * @param hitY   The Y coordinate of the hit vector
	 * @param hitZ   The Z coordinate of the hit vector
	 * @param meta   The metadata of {@link ItemStack} as processed by {@link Item#getMetadata(int)}
	 * @param placer The entity placing the block
	 * @param hand   The player hand used to place this block
	 * @return The state to be placed in the world
	 */
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return GetStateFromTag(placer.getActiveItemStack().getOrCreateSubCompound("blockdata"));
	}

	/**
	 * Called when a user uses the creative pick block button on this block
	 *
	 * @param state
	 * @param target The full target the player is looking at
	 * @param world
	 * @param pos
	 * @param player @return A ItemStack to add to the player's inventory, empty itemstack if nothing should be added.
	 */
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player)
	{
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
		NBTHelper.GetTag(stack).setTag("blockdata", GetTagFromState(state));
		return stack;
	}

	public abstract NBTTagCompound GetTagFromState(IBlockState state);

	public abstract IBlockState GetStateFromTag(NBTTagCompound tag);

	/**
	 * This gets a complete list of items dropped from this block.
	 *
	 * @param drops   add all items this block drops to this drops list
	 * @param world   The current world
	 * @param pos     Block position in world
	 * @param state   Current state
	 * @param fortune Breakers fortune level
	 */
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
		NBTHelper.GetTag(stack).setTag("blockdata", GetTagFromState(state));
		drops.add(stack);
	}
}
