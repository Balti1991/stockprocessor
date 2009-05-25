/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public abstract class DefaultParamterInformation<V extends Number> implements ParameterInformation<V>
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
}
