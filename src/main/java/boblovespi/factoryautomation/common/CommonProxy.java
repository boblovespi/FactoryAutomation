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
	void registerRenders();

	void preInit();

	void init();

	void addChatMessage(ChatType type, ITextComponent string);
}
