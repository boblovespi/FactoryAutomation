package boblovespi.factoryautomation.common;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TextComponent;

/**
 * Created by Willi on 11/8/2017.
 * common proxy interface
 */
public interface CommonProxy
{
	void RegisterRenders();

	void PreInit();

	void Init();

	void AddChatMessage(ChatType type, Component string);
}
