/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public class DefaultListParameterInformation<V extends Number> extends DefaultNumberParamterInformation<V> implements ListParameterInformation<V>
{
	private final V[] values;

	private final String[] stringValues;

	/**
	 * @param defaultValue
	 * @param displayName
	 * @param values
	 * @param stringValues
	 */
	public DefaultListParameterInformation(String displayName, V defaultValue, V[] values, String[] stringValues)
	{
		super(displayName, ParameterType.LIST, defaultValue);

		this.values = values;
		this.stringValues = stringValues;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.data.information.ListParameterInformation#stringValue
	 * ()
	 */
	@Override
	public String[] getStringValues()
	{
		return stringValues;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.data.information.ListParameterInformation#value()
	 */
	@Override
	public V[] getValues()
	{
		return values;
	}
}
