/**
 * 
 */
package stockprocessor.data.information;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author anti
 */
public abstract class DefaultParamterInformation<V extends Number> implements NumberParameterInformation<V>
{
	private final V defaultValue;

	private final String displayName;

	/**
	 * @param displayName
	 * @param defaultValue
	 */
	public DefaultParamterInformation(String displayName, V defaultValue)
	{
		this.defaultValue = defaultValue;
		this.displayName = displayName;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.data.information.ParameterInformation#defaultValue()
	 */
	@Override
	public V getDefaultValue()
	{
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.data.information.ParameterInformation#displayName()
	 */
	@Override
	public String getDisplayName()
	{
		return displayName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append(displayName).append(defaultValue).toString();
	}
}
