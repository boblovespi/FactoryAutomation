package boblovespi.factoryautomation.common.multiblock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willi on 11/11/2017.
 */
public class MultiblockHandler
{
	private static final Map<String, MultiblockStructurePattern> REGISTRY = new HashMap<>();

	public static MultiblockStructurePattern Get(String key)
	{
		return REGISTRY.getOrDefault(key, null);
	}

	public static void Register(String key, MultiblockStructurePattern pattern)
	{
		REGISTRY.putIfAbsent(key, pattern);
	}
}
