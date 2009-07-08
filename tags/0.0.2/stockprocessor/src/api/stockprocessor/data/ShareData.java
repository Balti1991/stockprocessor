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
public class ShareData<V>
{
	private final String name;

	private final V value;

	private final long volume;

	private final Date timeStamp;

	/**
	 * @param name
	 * @param value
	 * @param volume
	 * @param timeStamp
	 */
	public ShareData(String name, V value, long volume, Date timeStamp)
	{
		super();
		this.name = name;
		this.value = value;
		this.volume = volume;
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the value
	 */
	public V getValue()
	{
		return value;
	}

	/**
	 * @return the volume
	 */
	public long getVolume()
	{
		return volume;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp()
	{
		return timeStamp;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("time", getTimeStamp()).append("value", getValue()).append(
				"volume", getVolume()).toString();
	}
}
