package boblovespi.factoryautomation.common.item.types;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.StringRepresentable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 11/9/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum Metals implements StringRepresentable, IMultiTypeEnum
{
	IRON(0, "iron", 1538, 0xFFEAEEF2, 447, 7870),
	GOLD(1, "gold", 10000, 0xFFFAF437, 129, 19300),
	COPPER(2, "copper", 1084, 0xFFFF973D, 385, 8933),
	TIN(3, "tin", 232, 0xFFF7E8E8, 7310, 227),
	BRONZE(4, "bronze", 950, 0xFFFFB201, 355, 8780),
	STEEL(5, "steel", 10000, 0xFF000000, 434, 7854),
	PIG_IRON(6, "pig_iron", 10000, 0xFF000000, 415, 7850),
	MAGMATIC_BRASS(7, "magmatic_brass", 10000, 0xFF000000, 0, 0),
	SILVER(8, "silver", 10000, 0xFF000000, 235, 10500),
	LEAD(9, "lead", 10000, 0xFF000000, 129, 11340),
	NICKEL(10, "nickel", 10000, 0xFF000000, 0, 0),
	ALUMINUM(11, "aluminum", 10000, 0xFF000000, 0, 0),
	ALUMINUM_BRONZE(12, "aluminum_bronze", 10000, 0xFF000000, 0, 0);
	private final int id;
	private final String name;
	public final int meltTemp;
	public final int color;
	public final float massHeatCapacity;
	public final float density;


	/**
	 * @param id               a unique id
	 * @param name             the name of the metal
	 * @param meltTemp         the temperature at which the metal melts at STP
	 * @param color            the color of the metal
	 * @param massHeatCapacity the mass heat capacity of the metal in J/(kg*K) source: http://cecs.wright.edu/people/faculty/sthomas/htappendix01.pdf
	 * @param density          the density of the metal in kg/m3
	 */
	Metals(int id, String name, int meltTemp, int color, float massHeatCapacity, float density)
	{
		this.id = id;
		this.name = name;
		this.meltTemp = meltTemp;
		this.color = color;
		this.massHeatCapacity = massHeatCapacity;
		this.density = density;
	}

	public static Metals GetFromName(String name)
	{
		return Metals.valueOf(name.toUpperCase());
	}

	@Override
	public String getSerializedName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int GetId()
	{
		return id;
	}
}
