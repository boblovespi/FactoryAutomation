package boblovespi.factoryautomation.client.gui.component;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.SoundEvent;

/**
 * Created by Willi on 7/31/2018.
 */
public class GuiButtonSound extends GuiButton
{
	private final SoundEvent sound;

	public GuiButtonSound(int id, int x, int y, int width, int height, String text, SoundEvent sound)
	{
		super(id, x, y, width, height, text);
		this.sound = sound;
	}

	@Override
	public void playPressSound(SoundHandler handler)
	{
		handler.playSound(PositionedSoundRecord.getMasterRecord(sound, 1.0F));
	}
}
