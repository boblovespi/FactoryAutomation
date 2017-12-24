package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.machine.BlastFurnaceController;
import boblovespi.factoryautomation.common.block.machine.MultiblockPart;
import boblovespi.factoryautomation.common.block.machine.SolarPanel;
import boblovespi.factoryautomation.common.block.powercable.Cable;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Willi on 11/9/2017.
 */
@Mod.EventBusSubscriber
public class FABlocks
{
	private static final AtomicBoolean isInit = new AtomicBoolean(false);

	public static List<Block> blocks;

	public static FABlock concrete;
	public static FABlock riceCrop;
	public static ConcreteSlab concreteSlab;
	public static ConcreteSlab concreteDoubleSlab;
	public static FABlock blastFurnaceController;
	public static FABlock multiblockPart;
	public static FABlock cable;
	public static FABlock solarPanel;
	public static FABlock metalOres;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		blocks = new ArrayList<>(100);

		concrete = new Concrete();
		riceCrop = new RiceCrop();

		concreteSlab = new ConcreteSlab.Half();
		concreteDoubleSlab = new ConcreteSlab.Double();
		FAItems.items.add(new ItemSlab(concreteSlab.ToBlock(),
									   concreteSlab.ToBlockSlab(),
									   concreteDoubleSlab.ToBlockSlab())
								  .setUnlocalizedName(
										  concreteSlab.UnlocalizedName())
								  .setRegistryName(
										  concreteSlab.RegistryName()));

		blastFurnaceController = new BlastFurnaceController();
		multiblockPart = new MultiblockPart();

		cable = new Cable();
		solarPanel = new SolarPanel();
		metalOres = new MetalOre();
	}

	public static void RegisterRenders()
	{
		RegisterRender(concrete, 0);
	}

	private static void RegisterRender(FABlock block, int meta)
	{
		Log.LogInfo(
				"The other file path", FactoryAutomation.MODID + ":" + block
						.GetMetaFilePath(meta));

		ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID,
									 block.GetMetaFilePath(meta)), "inventory");

		Log.LogInfo("The other model resource location", loc.toString());
		//
		//		if (block.IsItemBlock())
		//		{
		//			Log.LogInfo("Is a itemblock, registering item");
		//			Item blockItem = Item.getItemFromBlock(block.ToBlock());
		//			Log.LogInfo("itemblock unlocalized name",
		//					blockItem.getUnlocalizedName());
		//			ModelLoader.setCustomModelResourceLocation(blockItem, meta, loc);
		//		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		Init();

		if (blocks == null)
			Log.LogWarning("Blocks is null!");
		if (event == null || event.getRegistry() == null)
			Log.LogWarning("Event is null!");
		assert event != null;
		blocks.forEach(event.getRegistry()::register);
	}
}
