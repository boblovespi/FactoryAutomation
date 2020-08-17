package boblovespi.factoryautomation.common.container;

import net.minecraft.util.IIntArray;

import java.util.function.Supplier;

public class StringIntArray implements IIntArray
{
	private int actualLength;
	private char[] string;
	private Supplier<String> source;

	public StringIntArray(int size)
	{
		string = new char[size * 4];
		actualLength = 0;
	}

	@Override
	public int get(int index)
	{
		UpdateFromSource();
		if (index == 0)
			return actualLength;
		return string[index * 4 - 4] + ((int) string[index * 4 - 3] << 8) + ((int) string[index * 4 - 2] << 16) + (
				(int) string[index * 4 - 1] << 24);
	}

	@Override
	public void set(int index, int value)
	{
		if (index == 0)
		{
			actualLength = value;
			return;
		}
		string[index * 4 - 4] = (char) (value & 0x000000ff);
		string[index * 4 - 3] = (char) (value & 0x0000ff00 >> 8);
		string[index * 4 - 2] = (char) (value & 0x00ff0000 >> 16);
		string[index * 4 - 1] = (char) (value & 0xff000000 >> 24);
	}

	@Override
	public int size()
	{
		return string.length / 4 + 1;
	}

	public String GetString()
	{
		UpdateFromSource();
		char[] chars = new char[actualLength];
		System.arraycopy(string, 0, chars, 0, actualLength);
		return String.copyValueOf(chars);
	}

	public void SetString(String toSet)
	{
		if (toSet.length() > string.length)
			return;
		char[] chars = toSet.toCharArray();
		actualLength = toSet.length();
		System.arraycopy(chars, 0, string, 0, string.length);
	}

	public void SetSource(Supplier<String> source)
	{
		if (this.source == null)
		{
			this.source = source;
		}
	}

	private void UpdateFromSource()
	{
		if (source != null)
			SetString(source.get());
	}
}
