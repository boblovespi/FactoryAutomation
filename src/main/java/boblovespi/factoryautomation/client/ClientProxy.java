package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.client.tesr.TESRGearbox;
import boblovespi.factoryautomation.client.tesr.TESRPowerShaft;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Willi on 11/8/2017.
 * client proxy class
 */
@Mod.EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
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
}
