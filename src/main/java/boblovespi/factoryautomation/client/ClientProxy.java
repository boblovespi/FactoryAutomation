package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.client.tesr.*;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import boblovespi.factoryautomation.common.tileentity.mechanical.*;
import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Willi on 11/8/2017.
 * client proxy class
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ClientProxy implements CommonProxy
{
	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		FAItems.RegisterItemRenders();
		FABlocks.RegisterRenders();
	}

	@Override
	public void RegisterRenders()
	{
		ClientRegistry.bindTileEntityRenderer(TEPowerShaft.class, new TESRPowerShaft());
		ClientRegistry.bindTileEntityRenderer(TEGearbox.class, new TESRGearbox());
		ClientRegistry.bindTileEntityRenderer(TEMotor.class, new TESRMotor());
		ClientRegistry.bindTileEntityRenderer(TEPlacedBucket.class, new TESRPlacedBucket());
		ClientRegistry.bindTileEntityRenderer(TEHandCrank.class, new TESRHandCrank());
		ClientRegistry.bindTileEntityRenderer(TEChoppingBlock.class, new TESRChoppingBlock());
		ClientRegistry.bindTileEntityRenderer(TECampfire.class, new TESRCampfire());
		ClientRegistry.bindTileEntityRenderer(TEStoneCastingVessel.class, new TESRStoneCastingVessel());
		ClientRegistry.bindTileEntityRenderer(TEMillstone.class, new TESRMillstone());
		ClientRegistry.bindTileEntityRenderer(TEPaperBellows.class, new TESRBellows.Paper());
		ClientRegistry.bindTileEntityRenderer(TELeatherBellows.class, new TESRBellows.Leather());
		ClientRegistry.bindTileEntityRenderer(TEBevelGear.class, new TESRBevelGear());
	}

	@Override
	public void PreInit()
	{
		// ModelLoaderRegistry.registerLoader(new ObjModelLoader());
		// OBJLoader.INSTANCE.addDomain("factoryautomation");
		// FAConfig.ClientPreInit();
	}

	@Override
	public void Init()
	{
		// NetworkRegistry.INSTANCE.registerGuiHandler(FactoryAutomation.instance, new GuiHandler());
	}

	@Override
	public void AddChatMessage(ChatType type, ITextComponent string)
	{
		Minecraft.getInstance().ingameGUI.addChatMessage(type, string);
	}
}
