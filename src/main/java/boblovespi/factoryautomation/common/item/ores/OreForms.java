package boblovespi.factoryautomation.common.item.ores;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Arrays;
import java.util.stream.Collectors;

import static boblovespi.factoryautomation.common.item.ores.OreForms.Grade.*;
import static boblovespi.factoryautomation.common.item.ores.OreForms.Stage.*;

/**
 * Created by Willi on 8/12/2018.
 */
public enum OreForms implements IMultiTypeEnum, IStringSerializable
{
	POOR_COARSE_GRAVEL(POOR, COARSE_GRAVEL),
	NORMAL_COARSE_GRAVEL(NORMAL, COARSE_GRAVEL),
	RICH_COARSE_GRAVEL(RICH, COARSE_GRAVEL),
	POOR_GRAVEL(POOR, GRAVEL),
	NORMAL_GRAVEL(NORMAL, GRAVEL),
	RICH_GRAVEL(RICH, GRAVEL),
	POOR_FINE_GRAVEL(POOR, FINE_GRAVEL),
	NORMAL_FINE_GRAVEL(NORMAL, FINE_GRAVEL),
	RICH_FINE_GRAVEL(RICH, FINE_GRAVEL),
	PURIFIED_FINE_GRAVEL(PURIFIED, FINE_GRAVEL),
	POOR_DUST(POOR, DUST),
	NORMAL_DUST(NORMAL, DUST),
	RICH_DUST(RICH, DUST),
	PURIFIED_DUST(PURIFIED, DUST),
	EXTREMELY_PURIFIED_DUST(EXTREMELY_PURIFIED, DUST),
	RICH_PELLET(RICH, PELLET),
	PURIFIED_PELLET(PURIFIED, PELLET),
	EXTREMELY_PURIFIED_PELLET(EXTREMELY_PURIFIED, PELLET);
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
	public String getName()
	{
		return name().toLowerCase();
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public enum Grade
	{
		POOR, NORMAL, RICH, PURIFIED, EXTREMELY_PURIFIED,
	}

	public enum Stage
	{
		COARSE_GRAVEL, GRAVEL, FINE_GRAVEL, DUST, PELLET,
	}

}
