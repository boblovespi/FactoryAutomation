package boblovespi.factoryautomation.common;

import boblovespi.factoryautomation.common.guidebook.entry.GuidebookEntry;
import boblovespi.factoryautomation.common.item.Guidebook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

/**
 * Created by Willi on 11/8/2017.
 * common proxy interface
 */
public interface CommonProxy
{
	void RegisterRenders();

	void PreInit();

	void Init();

	void AddChatMessage(ChatType type, TextComponent string);

	void OpenGuidebook(World world, PlayerEntity player, GuidebookEntry page, Guidebook.ExtraInfo extraInfo);
}
