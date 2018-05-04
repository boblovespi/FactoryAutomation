package boblovespi.factoryautomation.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Willi on 5/3/2018.
 * annotate a public static method to call it when refreshing the config
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncOnConfigChange
{
	Priority priority();

	enum Priority implements Comparable<Priority>
	{
		INIT_FIELDS, FIRST, LAST
	}
}
