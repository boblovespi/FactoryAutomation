package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.crafter.ChipCreator;
import boblovespi.factoryautomation.common.block.crafter.workbench.IronWorkbench;
import boblovespi.factoryautomation.common.block.crafter.workbench.StoneWorkbench;
import boblovespi.factoryautomation.common.block.fluid.FluidFinite;
import boblovespi.factoryautomation.common.block.machine.*;
import boblovespi.factoryautomation.common.block.mechanical.CreativeMechanicalSource;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.block.powercable.Cable;
import boblovespi.factoryautomation.common.block.processing.Treetap;
import boblovespi.factoryautomation.common.block.resource.GemOre;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.block.resource.OreData;
import boblovespi.factoryautomation.common.fluid.Fluids;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static boblovespi.factoryautomation.common.config.ConfigFields.blockMiningLevelCat;
import static boblovespi.factoryautomation.common.item.tools.ToolMaterials.COPPER;
import static boblovespi.factoryautomation.common.item.tools.ToolMaterials.STEEL;

/**
 * Created by Willi on 11/9/2017.
 */
@Mod.EventBusSubscriber
public class FABlocks
{
	private static final AtomicBoolean isInit = new AtomicBoolean(false);

	public static List<Block> blocks;

	// multiblock controllers

	public static FABlock steelmakingFurnaceController;
	public static FABlock blastFurnaceController;
	public static FABlock tripHammerController;

	// misc - need to organize

	public static FABlock concrete;
	public static FABlock riceCrop;
	public static ConcreteSlab concreteSlab;
	public static ConcreteSlab concreteDoubleSlab;

	public static FABlock multiblockPart;
	public static FABlock cable;
	public static FABlock solarPanel;
	public static FABlock treetap;
	public static FABlock placedBucket;
	public static FABlock factorySign;

	// will be removed soon
	public static MultiTypeBlock<MetalOres> metalOres;

	public static FABlock powerShaft;
	public static FABlock gearbox;
	public static FABlock jawCrusher;
	public static FABlock creativeMechanicalSource;
	public static FABlock motor;
	public static MultiTypeBlock<Metals> metalBlock;
	public static MultiTypeBlock<Metals> metalPlateBlock;
	//public static MultiTypeBlock<Metals> metalPatternedPlateBlock;
	public static FABlock ironPatternedPlateBlock;

	//The soo many pillars update
	public static FABlock pillar_bronze, pillar_copper, pillar_iron, pillar_magmatic_brass, pillar_pig_iron, pillar_steel, pillar_tin;
	public static FABlock altpillar_bronze, altpillar_copper, altpillar_iron, altpillar_magmatic_brass, altpillar_pig_iron, altpillar_steel, altpillar_tin;

	// ores

	public static MultiTypeBlock<Ore.Grade> limoniteOre;
	public static MultiTypeBlock<Ore.Grade> magnetiteOre;
	public static FABlock siliconQuartzOre;

	// workbenches

	public static FABlock stoneWorkbench;
	public static FABlock ironWorkbench;
	public static FABlock chipCreator;

	// fluids

	public static FABlock steam;
	public static FABlock rubberSap;

	// decoration blocks

	public static FABlock bronzeCauldron;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		blocks = new ArrayList<>(100);

		// multiblock controllers

		blastFurnaceController = new BlastFurnaceController();
		steelmakingFurnaceController = new SteelmakingFurnaceController();
		tripHammerController = new TripHammerController();

		// misc - need to organize

		concrete = new Concrete();
		riceCrop = new RiceCrop();

		concreteSlab = new ConcreteSlab.Half();
		concreteDoubleSlab = new ConcreteSlab.Double();
		FAItems.items
				.add(new ItemSlab(concreteSlab.ToBlock(), concreteSlab.ToBlockSlab(), concreteDoubleSlab.ToBlockSlab())
						.setUnlocalizedName(concreteSlab.UnlocalizedName())
						.setRegistryName(concreteSlab.RegistryName()));
		multiblockPart = new MultiblockComponent();

		cable = new Cable();
		solarPanel = new SolarPanel();
		metalOres = new MetalOre();

		powerShaft = new PowerShaft();
		gearbox = new Gearbox();

		jawCrusher = new JawCrusher();

		creativeMechanicalSource = new CreativeMechanicalSource();

		motor = new Motor();

		treetap = new Treetap();
		placedBucket = new PlacedBucket();

		metalBlock = new MetalBlock("metal_block");
		metalBlock.Init(n -> n.setHardness(5).setResistance(30).setHarvestLevel("pickaxe", COPPER));
		metalPlateBlock = new MetalBlock("metal_plate_block");
		metalPlateBlock.Init(n -> n.setHardness(5).setResistance(30).setHarvestLevel("pickaxe", COPPER));
		//metalPatternedPlateBlock = new MetalBlock("metal_patterned_plate_block");
		//metalPatternedPlateBlock.Init(n -> n.setHardness(5).setResistance(30).setHarvestLevel("pickaxe", COPPER));
		ironPatternedPlateBlock = new FABaseBlock(Material.IRON, "patterned_plate_block_iron", CreativeTabs.BUILDING_BLOCKS)
				.Init(n -> n.setHardness(1f).setResistance(40).setHarvestLevel("pickaxe", 3));

		blocks.remove(metalBlock.GetBlock(Metals.IRON).ToBlock());
		blocks.remove(metalBlock.GetBlock(Metals.GOLD).ToBlock());

		FAItems.items.remove(metalBlock.GetBlock(Metals.IRON).GetItem().ToItem());
		FAItems.items.remove(metalBlock.GetBlock(Metals.GOLD).GetItem().ToItem());

		factorySign = new FABaseBlock(Material.IRON, "factory_sign_block", CreativeTabs.BUILDING_BLOCKS)
				.Init(n -> n.setHardness(1f).setResistance(10).setHarvestLevel("pickaxe", 1));

		//The soo many pillars update

		pillar_bronze = 		new Pillar("pillar_bronze", Metals.BRONZE);
		pillar_copper = 		new Pillar("pillar_copper", Metals.COPPER);
		pillar_iron = 			new Pillar("pillar_iron", Metals.IRON);
		pillar_magmatic_brass = new Pillar("pillar_magmatic_brass", Metals.MAGMATIC_BRASS);
		pillar_pig_iron = 		new Pillar("pillar_pig_iron", Metals.PIG_IRON);
		pillar_steel = 			new Pillar("pillar_steel", Metals.STEEL);
		pillar_tin = 			new Pillar("pillar_tin", Metals.TIN);

		altpillar_bronze = 			new PillarAlt("pillar_block_bronze", Metals.BRONZE);
		altpillar_copper = 			new PillarAlt("pillar_block_copper", Metals.COPPER);
		altpillar_iron = 			new PillarAlt("pillar_block_iron", Metals.IRON);
		altpillar_magmatic_brass = 	new PillarAlt("pillar_block_magmatic_brass", Metals.MAGMATIC_BRASS);
		altpillar_pig_iron = 		new PillarAlt("pillar_block_pig_iron", Metals.PIG_IRON);
		altpillar_steel = 			new PillarAlt("pillar_block_steel", Metals.STEEL);
		altpillar_tin = 			new PillarAlt("pillar_block_tin", Metals.TIN);

		// ores

		limoniteOre = new Ore("limonite_ore", blockMiningLevelCat.limoniteOre)
				.Init(n -> n.setHardness(2.5f).setResistance(14));
		magnetiteOre = new Ore("magnetite_ore", blockMiningLevelCat.ironOre)
				.Init(n -> n.setHardness(3f).setResistance(16));
		siliconQuartzOre = new GemOre(
				"ore_silicon_quartz",
				new OreData(FAItems.siliconQuartz.ToItem()).SetDropChance(n -> 1).SetXpChance((r, n) -> 12)
														   .SetMiningLevel(STEEL).SetHardness(2.5f).SetResistance(14));

		// workbenches

		stoneWorkbench = new StoneWorkbench();
		ironWorkbench = new IronWorkbench();
		chipCreator = new ChipCreator();

		// fluids

		steam = new FluidFinite(Fluids.steam, Material.WATER, "steam");
		rubberSap = new FluidFinite(Fluids.rubberSap, Materials.SAP, "rubber_sap");

		// decoration blocks

		bronzeCauldron = new BronzeCauldron();
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
