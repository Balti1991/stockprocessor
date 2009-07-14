/**
 * 
 */
package stockprocessor.data.information;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author anti
 */
public class DefaultNumberParamterInformation<V extends Number> extends DefaultParameterInformation implements NumberParameterInformation<V>
{
	private final V defaultValue;

	/**
	 * @param name
	 * @param type
	 */
	public DefaultNumberParamterInformation(String name, ParameterType type, V defaultValue)
	{
		super(name, type);
		this.defaultValue = defaultValue;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append(getDisplayName()).append(defaultValue).toString();
	}

}
