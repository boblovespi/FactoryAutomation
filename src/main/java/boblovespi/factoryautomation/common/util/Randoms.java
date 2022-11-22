package boblovespi.factoryautomation.common.util;

import net.minecraft.util.RandomSource;

import java.util.Random;

/**
 * Created by Willi on 6/30/2018.
 */
public enum Randoms
{
	MAIN

	;public final RandomSource r;

	Randoms()
	{
		r = RandomSource.create();
	}
}
