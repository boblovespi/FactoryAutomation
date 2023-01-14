package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.tesr.*;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Willi on 11/8/2017.
 * client proxy class
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FactoryAutomation.MODID)
@OnlyIn(Dist.CLIENT)
public class ClientProxy implements CommonProxy
{
	@Override
	public void RegisterRenders()
	{
		BlockEntityRenderers.register(TileEntityHandler.tePowerShaft.get(), TESRPowerShaft::new);
		BlockEntityRenderers.register(TileEntityHandler.teGearbox.get(), TESRGearbox::new);
		BlockEntityRenderers.register(TileEntityHandler.teMotor.get(), TESRMotor::new);
		BlockEntityRenderers.register(TileEntityHandler.tePlacedBucket.get(), TESRPlacedBucket::new);
		BlockEntityRenderers.register(TileEntityHandler.teHandCrank.get(), TESRHandCrank::new);
		BlockEntityRenderers.register(TileEntityHandler.teChoppingBlock.get(), TESRChoppingBlock::new);
		BlockEntityRenderers.register(TileEntityHandler.teCampfire.get(), TESRCampfire::new);
		BlockEntityRenderers.register(TileEntityHandler.teStoneCastingVessel.get(), TESRStoneCastingVessel::new);
		BlockEntityRenderers.register(TileEntityHandler.teMillstone.get(), TESRMillstone::new);
		BlockEntityRenderers.register(TileEntityHandler.tePaperBellows.get(), TESRBellows.Paper::new);
		BlockEntityRenderers.register(TileEntityHandler.teLeatherBellows.get(), TESRBellows.Leather::new);
		BlockEntityRenderers.register(TileEntityHandler.teBevelGear.get(), TESRBevelGear::new);
		BlockEntityRenderers.register(TileEntityHandler.teTripHammerController.get(), TESRTripHammer::new);
		BlockEntityRenderers.register(TileEntityHandler.teTumblingBarrel.get(), TESRTumblingBarrel::new);
		BlockEntityRenderers.register(TileEntityHandler.teBrickMaker.get(), TESRBrickMaker::new);

		// block render layers
		ItemBlockRenderTypes.setRenderLayer(FABlocks.bevelGear, RenderType.cutoutMipped());
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
		Minecraft.getInstance().gui.getChat().addMessage(string);
	}
}
