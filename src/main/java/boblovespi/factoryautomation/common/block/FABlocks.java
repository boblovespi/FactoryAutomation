package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.crafter.ChipCreator;
import boblovespi.factoryautomation.common.block.crafter.workbench.IronWorkbench;
import boblovespi.factoryautomation.common.block.crafter.workbench.StoneWorkbench;
import boblovespi.factoryautomation.common.block.decoration.BrickCastingVessel;
import boblovespi.factoryautomation.common.block.decoration.BronzeCauldron;
import boblovespi.factoryautomation.common.block.decoration.BronzeFence;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel;
import boblovespi.factoryautomation.common.block.fluid.*;
import boblovespi.factoryautomation.common.block.machine.*;
import boblovespi.factoryautomation.common.block.mechanical.*;
import boblovespi.factoryautomation.common.block.powercable.Cable;
import boblovespi.factoryautomation.common.block.processing.*;
import boblovespi.factoryautomation.common.block.resource.*;
import boblovespi.factoryautomation.common.fluid.Fluids;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.item.types.WoodTypes;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static boblovespi.factoryautomation.common.config.ConfigFields.blockMiningLevelCat;
import static boblovespi.factoryautomation.common.item.FAItems.Building;
import static boblovespi.factoryautomation.common.item.FAItems.Prop;
import static boblovespi.factoryautomation.common.item.tools.ToolMaterial.STEEL;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

/**
 * Created by Willi on 11/9/2017.
 */
@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FABlocks
{
	private static final AtomicBoolean isInit = new AtomicBoolean(false);

	public static List<Block> blocks;

	// multiblock controllers

	public static FABlock steelmakingFurnaceController;
	public static FABlock blastFurnaceController;
	public static FABlock tripHammerController;
	public static FABlock brickKiln;
	public static FABlock waterwheel;

	// misc - need to organize

	public static FABlock concrete;
	public static FABlock riceCrop;
	public static ConcreteSlab concreteSlab;
	// public static ConcreteSlab concreteDoubleSlab;

	public static FABlock multiblockPart;
	public static FABlock cable;
	public static FABlock solarPanel;
	public static FABlock treetap;
	public static FABlock placedBucket;
	public static FABlock factorySign;
	public static FABlock solidfueledfirebox;
	// all the chopping blocks
	public static FABlock woodChoppingBlock;
	public static List<FABlock> woodChoppingBlocks;
	public static FABlock campfire;
	public static FABlock brickMakerFrame;
	public static FABlock ironCharcoalMix;

	// metal blocks

	public static MultiTypeBlock<Metals> metalBlock;
	public static MultiTypeBlock<Metals> metalPlateBlock;
	//public static MultiTypeBlock<Metals> metalPatternedPlateBlock;
	public static FABlock ironPatternedPlateBlock;

	//The soo many pillars update
	public static FABlock pillarBronze, pillarCopper, pillarIron, pillarMagmaticBrass, pillarPigIron, pillarSteel, pillarTin;
	public static FABlock altpillarBronze, altpillarCopper, altpillarIron, altpillarMagmaticBrass, altpillarPigIron, altpillarSteel, altpillarTin;

	// ores

	public static MultiTypeBlock<MetalOres> metalOres;
	public static MultiTypeBlock<Ore.Grade> limoniteOre;
	public static MultiTypeBlock<Ore.Grade> magnetiteOre;
	public static FABlock siliconQuartzOre;
	public static FABlock rock;
	public static FABlock flintRock;
	public static FABlock blackSand;

	// raw chunks
	public static FABlock cassiteriteRawBlock;
	public static FABlock limoniteRawBlock;

	// workbenches

	public static FABlock stoneWorkbench;
	public static FABlock ironWorkbench;
	public static FABlock chipCreator;

	// fluids

	public static FABlock steam;
	public static FABlock rubberSap;
	public static FABlock moltenNetherMetal;

	// decoration blocks

	public static FABlock bronzeCauldron;
	public static FABlock bronzeFence;
	public static FABlock slagGlass;

	// smelting

	public static FABlock stoneCrucible;
	public static FABlock stoneCastingVessel;
	public static FABlock brickCrucible;
	public static FABlock brickCastingVessel;
	public static FABlock brickFirebox;
	public static FABlock paperBellows;
	public static FABlock leatherBellows;

	// mechanical

	public static List<FABlock> powerShafts;
	public static FABlock powerShaft;
	public static FABlock powerShaftWood;
	public static FABlock gearbox;
	public static FABlock bevelGear;
	public static FABlock splitterIron;

	public static FABlock jawCrusher;
	public static FABlock creativeMechanicalSource;
	public static FABlock motor;
	public static FABlock handCrank;
	public static FABlock millstone;
	public static FABlock horseEngine;
	public static FABlock screwPump;
	public static FABlock tumblingBarrel;

	// transfer

	public static FABlock ironPipe;
	public static FABlock woodPipe;
	public static FABlock pump;
	public static FABlock woodenTank;

	// resource blocks

	public static FABlock greenSand;
	public static FABlock charcoalPile;
	public static FABlock logPile;
	public static FABlock terraclayBrickBlock;
	public static FABlock terraclayBlock;
	public static FABlock ironBloom;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		FAItems.Init();

		blocks = new ArrayList<>(100);

		// multiblock controllers

		blastFurnaceController = new BlastFurnaceController();
		steelmakingFurnaceController = new SteelmakingFurnaceController();
		tripHammerController = new TripHammerController();
		// brickKiln = new BrickKiln();
		waterwheel = new Waterwheel();

		// misc - need to organize

		concrete = new Concrete();
		riceCrop = new RiceCrop();

		concreteSlab = new ConcreteSlab();
		// concreteDoubleSlab = new ConcreteSlab.Double();
		FAItems.items.add(new FAItemBlock(concreteSlab, Building()));
		multiblockPart = new MultiblockComponent();

		cable = new Cable();
		solarPanel = new SolarPanel();
		metalOres = new MetalOre();

		powerShaft = new PowerShaft("power_shaft", 25, 25);
		powerShaftWood = new PowerShaft("power_shaft_wood", 10, 10);
		powerShafts = List.of(powerShaftWood, powerShaft);

		gearbox = new Gearbox();
		bevelGear = new BevelGear();
		splitterIron = new Splitter("splitter_iron", 25, 25);

		jawCrusher = new JawCrusher();

		creativeMechanicalSource = new CreativeMechanicalSource();

		motor = new Motor();

		treetap = new Treetap();
		placedBucket = new PlacedBucket();

		metalBlock = new MetalBlock("metal_block",
				of(Material.METAL).strength(5, 30).requiresCorrectToolForDrops());
		metalPlateBlock = new MetalBlock("metal_plate_block",
				of(Material.METAL).strength(5, 30).requiresCorrectToolForDrops());

		ironPatternedPlateBlock = new FABaseBlock("patterned_plate_block_iron", false,
				of(Material.METAL).strength(1, 40).requiresCorrectToolForDrops(),
				Building());

		blocks.remove(metalBlock.GetBlock(Metals.IRON).ToBlock());
		blocks.remove(metalBlock.GetBlock(Metals.GOLD).ToBlock());
		blocks.remove(metalBlock.GetBlock(Metals.COPPER).ToBlock());

		FAItems.items.remove(metalBlock.GetBlock(Metals.IRON).GetItem().ToItem());
		FAItems.items.remove(metalBlock.GetBlock(Metals.GOLD).GetItem().ToItem());
		FAItems.items.remove(metalBlock.GetBlock(Metals.COPPER).GetItem().ToItem());

		factorySign = new FABaseBlock("factory_sign_block", false,
				of(Material.METAL).strength(1, 10),
				Building());

		solidfueledfirebox = new SolidFueledFirebox();

		// chopping blocks!
		// woodChoppingBlock = new ChoppingBlock(Material.WOOD, "wood_chopping_block", 10).Init(n -> n.setHardness(4.0f));
		woodChoppingBlocks = new ArrayList<>(6);
		// woodChoppingBlocks.add(woodChoppingBlock);
		for (int i = 0; i < 6; i++)
		{
			FABlock tempChoppingBlock = new ChoppingBlock("wood_chopping_block_" + WoodTypes.values()[i].GetName(), 10,
					of(Material.WOOD, WoodTypes.values()[i].GetColor()).strength(4).sound(SoundType.WOOD));
			woodChoppingBlocks.add(tempChoppingBlock);
		}

		campfire = new Campfire();
		brickMakerFrame = new BrickMaker();
		ironCharcoalMix = new IronCharcoalMix();

		//The soo many pillars update

		pillarBronze = new Pillar("pillar_bronze", Metals.BRONZE);
		pillarCopper = new Pillar("pillar_copper", Metals.COPPER);
		pillarIron = new Pillar("pillar_iron", Metals.IRON);
		pillarMagmaticBrass = new Pillar("pillar_magmatic_brass", Metals.MAGMATIC_BRASS);
		pillarPigIron = new Pillar("pillar_pig_iron", Metals.PIG_IRON);
		pillarSteel = new Pillar("pillar_steel", Metals.STEEL);
		pillarTin = new Pillar("pillar_tin", Metals.TIN);

		altpillarBronze = new PillarAlt("pillar_block_bronze", Metals.BRONZE);
		altpillarCopper = new PillarAlt("pillar_block_copper", Metals.COPPER);
		altpillarIron = new PillarAlt("pillar_block_iron", Metals.IRON);
		altpillarMagmaticBrass = new PillarAlt("pillar_block_magmatic_brass", Metals.MAGMATIC_BRASS);
		altpillarPigIron = new PillarAlt("pillar_block_pig_iron", Metals.PIG_IRON);
		altpillarSteel = new PillarAlt("pillar_block_steel", Metals.STEEL);
		altpillarTin = new PillarAlt("pillar_block_tin", Metals.TIN);

		// ores

		limoniteOre = new Ore("limonite_ore", blockMiningLevelCat.limoniteOre,
				of(Material.STONE).strength(2.5f, 14), Building());
		// .Init(n -> n.setHardness(2.5f).setResistance(14));
		magnetiteOre = new Ore("magnetite_ore", blockMiningLevelCat.ironOre,
				of(Material.STONE).strength(3f, 16), Building());
		// .Init(n -> n.setHardness(3f).setResistance(16));
		siliconQuartzOre = new GemOre("ore_silicon_quartz",
				new OreData(FAItems.siliconQuartz.ToItem()).SetDropChance(n -> 1).SetXpChance((r, n) -> 12)
						.SetMiningLevel(STEEL).SetHardness(2.5f).SetResistance(14));

		rock = new Rock();

		// TODO: add obsidian flake rock
		flintRock = new OreSample("flint_rock", new ItemStack[] {new ItemStack(Items.FLINT)});
		blackSand = new Sand("black_sand", 0x1D1C1C, of(Material.SAND, MaterialColor.COLOR_BLACK)
				.strength(0.5F).sound(SoundType.SAND).requiresCorrectToolForDrops(), Building());

		cassiteriteRawBlock = new FABaseBlock("cassiterite_raw_block", false, of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(5.0F, 6.0F), Building());
		limoniteRawBlock = new FABaseBlock("limonite_raw_block", false, of(Material.STONE, MaterialColor.TERRACOTTA_ORANGE).requiresCorrectToolForDrops().strength(5.0F, 6.0F), Building());

		// workbenches

		stoneWorkbench = new StoneWorkbench();
		ironWorkbench = new IronWorkbench();
		chipCreator = new ChipCreator();

		// fluids

		steam = new FluidFinite(Fluids.steam.still, Material.WATER, "steam");
		rubberSap = new FluidFinite(Fluids.rubberSap.still, Materials.SAP, "rubber_sap");
		moltenNetherMetal = new FluidFinite(Fluids.moltenNetherMetal.still, Material.LAVA, "molten_nether_metal");

		// decoration blocks

		bronzeCauldron = new BronzeCauldron();
		bronzeFence = new BronzeFence();
		stoneCrucible = new StoneCrucible();
		stoneCastingVessel = new StoneCastingVessel();
		slagGlass = new SlagGlass();

		// smelting

		brickCrucible = new BrickCrucible();
		brickCastingVessel = new BrickCastingVessel();
		brickFirebox = new BrickFirebox();
		paperBellows = new PaperBellows();
		leatherBellows = new LeatherBellows();

		// mechanical

		handCrank = new HandCrank();
		millstone = new Millstone();
		horseEngine = new HorseEngine();
		screwPump = new ScrewPump();
		tumblingBarrel = new TumblingBarrel();

		// transfer

		ironPipe = new Pipe("iron_pipe", of(Material.METAL).strength(5f).requiresCorrectToolForDrops().sound(SoundType.METAL), Building());
		woodPipe = new Pipe("wood_pipe", of(Material.WOOD).strength(2f).sound(SoundType.WOOD), Building());
		pump = new Pump("iron_pump");
		woodenTank = new WoodenTank();

		// resource blocks

		greenSand = new FABaseBlock("green_sand", false, of(Material.CLAY)
				.strength(0.4f).sound(SoundType.GRAVEL), Prop().tab(FAItemGroups.metallurgy));
		charcoalPile = new CharcoalPile();
		logPile = new LogPile();
		terraclayBrickBlock = new FABaseBlock("terraclay_brick_block", false,
				of(Material.STONE).strength(2).requiresCorrectToolForDrops(), Building());
		terraclayBlock = new FABaseBlock("terraclay_block", false,
				of(Material.STONE).strength(0.8f).requiresCorrectToolForDrops(),
				Building());
		ironBloom = new IronBloom();
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
		// blocks.forEach(n -> System.out.println(n.getRegistryName()));
		blocks.forEach(event.getRegistry()::register);
	}
}
