package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.container.ContainerSteelmakingFurnace;
import boblovespi.factoryautomation.common.container.workbench.ContainerWorkbench;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by Willi on 11/12/2017.
 * gui handler
 */
// TODO: move to proper location
public class GuiHandler implements IGuiHandler
{
	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID < GuiID.values().length && ID >= 0)
		{
			try
			{
				return GuiID.values()[ID].GetContainerClass().getDeclaredConstructor(IInventory.class, TileEntity.class)
										 .newInstance(player.inventory, world.getTileEntity(new BlockPos(x, y, z)));
			} catch (Exception e)
			{
				Log.LogWarning("there was an exception! (server)");
				Log.LogWarning(e.getMessage());
				Log.LogWarning(e.getLocalizedMessage());
				System.out.println("e.getStackTrace() = " + Arrays.toString(e.getStackTrace()));
			}
		}
		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID < GuiID.values().length && ID >= 0)
		{
			try
			{
				return GuiID.values()[ID].GetGuiClass().getDeclaredConstructor(IInventory.class, TileEntity.class)
										 .newInstance(player.inventory, world.getTileEntity(new BlockPos(x, y, z)));
			} catch (Exception e)
			{
				Log.LogWarning("there was an exception! (client)");
				Log.LogWarning(e.getMessage());
				Log.LogWarning(e.getLocalizedMessage());
				System.out.println("e.getStackTrace() = " + Arrays.toString(e.getStackTrace()));
			}
		}
		return null;
	}

	public enum GuiID
	{
		BLAST_FURNACE(0, ContainerBlastFurnace.class, GuiBlastFurnace.class),
		STEELMAKING_FURNACE(1, ContainerSteelmakingFurnace.class, GuiSteelmakingFurnace.class),
		STONE_WORKBENCH(2, ContainerWorkbench.class, GuiWorkbench.class),
		WORKBENCH(3, ContainerWorkbench.class, GuiWorkbench.class),
		CHIP_CREATOR(4, ContainerBasicCircuitCreator.class, GuiBasicCircuitCreator.class);

		public final int id;
		private final Class<? extends Gui> gui;
		private final Class<? extends Container> container;

		GuiID(int id, Class<? extends Container> container, Class<? extends Gui> gui)
		{
			this.id = id;
			this.container = container;
			this.gui = gui;
		}

		public Class<? extends Container> GetContainerClass()
		{
			return container;
		}

		public Class<? extends Gui> GetGuiClass()
		{
			return gui;
		}
	}
}
