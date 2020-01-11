package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiGuidebook;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.client.tesr.*;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.guidebook.entry.GuidebookEntry;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.Guidebook;
import boblovespi.factoryautomation.common.tileentity.TEPlacedBucket;
import boblovespi.factoryautomation.common.tileentity.mechanical.*;
import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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
		ClientRegistry.bindTileEntitySpecialRenderer(TEPowerShaft.class, new TESRPowerShaft());
		ClientRegistry.bindTileEntitySpecialRenderer(TEGearbox.class, new TESRGearbox());
		ClientRegistry.bindTileEntitySpecialRenderer(TEMotor.class, new TESRMotor());
		ClientRegistry.bindTileEntitySpecialRenderer(TEPlacedBucket.class, new TESRPlacedBucket());
		ClientRegistry.bindTileEntitySpecialRenderer(TEHandCrank.class, new TESRHandCrank());
		ClientRegistry.bindTileEntitySpecialRenderer(TEChoppingBlock.class, new TESRChoppingBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TECampfire.class, new TESRCampfire());
		ClientRegistry.bindTileEntitySpecialRenderer(TEStoneCastingVessel.class, new TESRStoneCastingVessel());
		ClientRegistry.bindTileEntitySpecialRenderer(TEMillstone.class, new TESRMillstone());
		ClientRegistry.bindTileEntitySpecialRenderer(TEPaperBellows.class, new TESRBellows.Paper());
		ClientRegistry.bindTileEntitySpecialRenderer(TELeatherBellows.class, new TESRBellows.Leather());
		ClientRegistry.bindTileEntitySpecialRenderer(TEBevelGear.class, new TESRBevelGear());
	}

	@Override
	public void PreInit()
	{
		// ModelLoaderRegistry.registerLoader(new ObjModelLoader());
		OBJLoader.INSTANCE.addDomain("factoryautomation");
		// FAConfig.ClientPreInit();
	}

	@Override
	public void Init()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(FactoryAutomation.instance, new GuiHandler());
	}

	@Override
	public void AddChatMessage(ChatType type, TextComponentString string)
	{
		Minecraft.getMinecraft().ingameGUI.addChatMessage(type, string);
	}

	@Override
	public void OpenGuidebook(World world, PlayerEntity player, GuidebookEntry entry, Guidebook.ExtraInfo extraInfo)
	{
		if (entry != null)
			GuiGuidebook.SetPage(entry, extraInfo.pageNum);
		player.openGui(FactoryAutomation.instance, GuiHandler.GuiID.GUIDEBOOK.id, world, 0, 0, 0);
	}
}
