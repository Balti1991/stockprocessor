/**
 * 
 */
package stockprocessor.data;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author anti
 */
public class StockData<V>
{
	private final V value;

	private final long volume;

	private final Date time;

	/**
	 * @param value
	 * @param volume
	 */
	public StockData(Date time, V value, long volume)
	{
		super();

		this.time = time;
		this.value = value;
		this.volume = volume;
	}

	public Date getTime()
	{
		return time;
	}

	public V getValue()
	{
		return value;
	}

	public long getVolume()
	{
		return volume;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("time", getTime()).append("value", getValue()).append("volume",
				getVolume()).toString();
	}
}
