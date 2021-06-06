package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Willi on 11/8/2017.
 */
public class Log
{
	private static Logger logger;

	public static Logger getLogger()
	{
		if (logger == null)
			logger = LogManager.getFormatterLogger(FactoryAutomation.MODID);
		return logger;
	}

	public static void logDebug(String s, Object o)
	{
		logger.debug(s + ": " + o.toString());
	}

	public static void logDebug(String s)
	{
		logger.debug(s);
	}

	public static void logInfo(String s, Object o)
	{
		logger.info(s + ": " + o.toString());
	}

	public static void logInfo(String s)
	{
		logger.info(s);
	}

	public static void logWarning(String s)
	{
		logger.warn(s);
	}

	public static void logError(String s)
	{
		logger.error(s);
	}
}
