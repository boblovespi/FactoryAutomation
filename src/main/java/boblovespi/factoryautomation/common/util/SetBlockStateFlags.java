package boblovespi.factoryautomation.common.util;

/**
 * Created by Willi on 4/4/2018.
 */
public final class SetBlockStateFlags
{
	public static final int FORCE_BLOCK_UPDATE = 1;
	public static final int SEND_TO_CLIENT = 2;
	public static final int PREVENT_RERENDER = 4;
	public static final int FORCE_RERENDER_ON_MAIN_THREAD = 8;
	public static final int PREVENT_OBSERVER_SEEING_CHANGE = 16;
	public static final int UPDATE_AND_SEND_CLIENT = FORCE_BLOCK_UPDATE | SEND_TO_CLIENT;
	public static final int UPDATE_NO_RERENDER = UPDATE_AND_SEND_CLIENT | PREVENT_RERENDER;
}
