package boblovespi.factoryautomation.common.multiblock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willi on 11/11/2017.
 */
public class MultiblockHandler
{
	private static final Map<String, MultiblockStructurePattern> REGISTERY = new HashMap<>();


	public static MultiblockStructurePattern Get(String key)
	{
		return REGISTERY.getOrDefault(key, null);
	}

	public static void Register(String key, MultiblockStructurePattern pattern)
	{
		REGISTERY.putIfAbsent(key, pattern);
	}
}
