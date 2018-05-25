package boblovespi.factoryautomation.common.util;

import java.io.Serializable;

/**
 * Created by Willi on 5/20/2018.
 * <p>
 * a pair class since javafx hates me
 * </p>
 */
public class Pair<K, V> implements Serializable
{

	private K key;
	private V value;

	public Pair(K key, V value)
	{
		this.key = key;
		this.value = value;
	}

	public Pair()
	{
	}

	public K getKey()
	{
		return key;
	}

	public void setKey(K key)
	{
		this.key = key;
	}

	public V getValue()
	{
		return value;
	}

	public void setValue(V value)
	{
		this.value = value;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Pair && key.equals(((Pair) obj).getKey()) && value.equals(((Pair) obj).getValue());
	}

	@Override
	public String toString()
	{
		return "( " + key.toString() + " , " + value.toString() + " )";
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
}
