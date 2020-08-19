package boblovespi.factoryautomation.common;

import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
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

	void AddChatMessage(ChatType type, ITextComponent string);
}
