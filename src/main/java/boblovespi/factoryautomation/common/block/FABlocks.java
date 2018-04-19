package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.machine.*;
import boblovespi.factoryautomation.common.block.mechanical.CreativeMechanicalSource;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.block.powercable.Cable;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.block.workbench.IronWorkbench;
import boblovespi.factoryautomation.common.block.workbench.StoneWorkbench;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
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

	// will be removed soon
	public static MultiTypeBlock<MetalOres> metalOres;
	public static FABlock steelmakingFurnaceController;
	public static FABlock powerShaft;
	public static FABlock jawCrusher;
	public static FABlock creativeMechanicalSource;
	public static FABlock motor;
	public static MultiTypeBlock<Metals> metalBlock;

	// ores

	public static FABlock limoniteOre;

	// workbenches

	public static FABlock stoneWorkbench;
	public static FABlock ironWorkbench;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		blocks = new ArrayList<>(100);

		concrete = new Concrete();
		riceCrop = new RiceCrop();

		concreteSlab = new ConcreteSlab.Half();
		concreteDoubleSlab = new ConcreteSlab.Double();
		FAItems.items
				.add(new ItemSlab(concreteSlab.ToBlock(), concreteSlab.ToBlockSlab(), concreteDoubleSlab.ToBlockSlab())
						.setUnlocalizedName(concreteSlab.UnlocalizedName())
						.setRegistryName(concreteSlab.RegistryName()));

		blastFurnaceController = new BlastFurnaceController();
		multiblockPart = new MultiblockComponent();

		cable = new Cable();
		solarPanel = new SolarPanel();
		metalOres = new MetalOre();

		steelmakingFurnaceController = new SteelmakingFurnaceController();

		powerShaft = new PowerShaft();

		jawCrusher = new JawCrusher();

		creativeMechanicalSource = new CreativeMechanicalSource();

		motor = new Motor();

		metalBlock = new MetalBlock();
		blocks.remove(metalBlock.GetBlock(Metals.IRON).ToBlock());
		blocks.remove(metalBlock.GetBlock(Metals.GOLD).ToBlock());

		FAItems.items.remove(metalBlock.GetBlock(Metals.IRON).GetItem().ToItem());
		FAItems.items.remove(metalBlock.GetBlock(Metals.GOLD).GetItem().ToItem());

		// ores

		limoniteOre = new Ore("limonite_ore", 1);

		// workbenches

		stoneWorkbench = new StoneWorkbench();
		ironWorkbench = new IronWorkbench();
	}

	public static void RegisterRenders()
	{
		RegisterRender(concrete, 0);
	}

	private static void RegisterRender(FABlock block, int meta)
	{
		Log.LogInfo("The other file path", FactoryAutomation.MODID + ":" + block.GetMetaFilePath(meta));

		ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID, block.GetMetaFilePath(meta)), "inventory");

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
