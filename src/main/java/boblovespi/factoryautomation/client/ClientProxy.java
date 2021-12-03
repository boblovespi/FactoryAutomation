package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.tesr.*;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
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
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FactoryAutomation.MODID)
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
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.tePowerShaft, TESRPowerShaft::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teGearbox, TESRGearbox::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teMotor, TESRMotor::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.tePlacedBucket, TESRPlacedBucket::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teHandCrank, TESRHandCrank::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teChoppingBlock, TESRChoppingBlock::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teCampfire, TESRCampfire::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teStoneCastingVessel, TESRStoneCastingVessel::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teMillstone, TESRMillstone::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.tePaperBellows, TESRBellows.Paper::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teLeatherBellows, TESRBellows.Leather::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityHandler.teBevelGear, TESRBevelGear::new);
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
	public void AddChatMessage(ChatType type, Component string)
	{
		Minecraft.getInstance().gui.handleChat(type, string, Minecraft.getInstance().player.getUUID());
	}
}
