package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.tesr.*;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
		BlockEntityRenderers.register(TileEntityHandler.tePowerShaft, TESRPowerShaft::new);
		BlockEntityRenderers.register(TileEntityHandler.teGearbox, TESRGearbox::new);
		BlockEntityRenderers.register(TileEntityHandler.teMotor, TESRMotor::new);
		BlockEntityRenderers.register(TileEntityHandler.tePlacedBucket, TESRPlacedBucket::new);
		BlockEntityRenderers.register(TileEntityHandler.teHandCrank, TESRHandCrank::new);
		BlockEntityRenderers.register(TileEntityHandler.teChoppingBlock, TESRChoppingBlock::new);
		BlockEntityRenderers.register(TileEntityHandler.teCampfire, TESRCampfire::new);
		BlockEntityRenderers.register(TileEntityHandler.teStoneCastingVessel, TESRStoneCastingVessel::new);
		BlockEntityRenderers.register(TileEntityHandler.teMillstone, TESRMillstone::new);
		BlockEntityRenderers.register(TileEntityHandler.tePaperBellows, TESRBellows.Paper::new);
		BlockEntityRenderers.register(TileEntityHandler.teLeatherBellows, TESRBellows.Leather::new);
		BlockEntityRenderers.register(TileEntityHandler.teBevelGear, TESRBevelGear::new);
		BlockEntityRenderers.register(TileEntityHandler.teTripHammerController, TESRTripHammer::new);
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
