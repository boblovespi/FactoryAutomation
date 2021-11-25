package boblovespi.factoryautomation.common.item.ores;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.stream.Collectors;

import static boblovespi.factoryautomation.common.item.ores.OreForms.Grade.*;
import static boblovespi.factoryautomation.common.item.ores.OreForms.Stage.*;

/**
 * Created by Willi on 8/12/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum OreForms implements IMultiTypeEnum, IStringSerializable
{
	CHUNK(NORMAL, Stage.CHUNK),
	NORMAL_GRAVEL(NORMAL, GRAVEL),
	RICH_GRAVEL(RICH, GRAVEL),
	NORMAL_FINE_GRAVEL(NORMAL, FINE_GRAVEL),
	RICH_FINE_GRAVEL(RICH, FINE_GRAVEL),
	CONCENTRATED_FINE_GRAVEL(CONCENTRATED, FINE_GRAVEL),
	NORMAL_DUST(NORMAL, DUST),
	RICH_DUST(RICH, DUST),
	CONCENTRATED_DUST(CONCENTRATED, DUST),
	PURIFIED_DUST(PURIFIED, DUST),
	RICH_PELLET(RICH, PELLET),
	CONCENTRATED_PELLET(CONCENTRATED, PELLET),
	PURIFIED_PELLET(PURIFIED, PELLET);
	public static final OreForms[] DEFAULT_FORMS = Arrays.stream(values()).filter(n -> n.stage.ordinal() < 5)
														 .collect(Collectors.toList()).toArray(new OreForms[] {});
	public final Grade grade;
	public final Stage stage;

	OreForms(Grade grade, Stage stage)
	{
		this.grade = grade;
		this.stage = stage;
	}

	@Override
	public int GetId()
	{
		return ordinal();
	}

	@Override
	public String getSerializedName()
	{
		return name().toLowerCase();
	}

	@Override
	public String toString()
	{
		return getSerializedName();
	}

	public enum Grade
	{
		NORMAL, RICH, CONCENTRATED, PURIFIED,
	}

	public enum Stage
	{
		CHUNK, GRAVEL, FINE_GRAVEL, DUST, PELLET,
	}

}
