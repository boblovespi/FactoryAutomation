package boblovespi.factoryautomation.common;

import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Willi on 11/8/2017.
 */
public interface CommonProxy
{
	void RegisterRenders();

	void PreInit();

	void Init();

	void AddChatMessage(ChatType type, TextComponentString string);
}
