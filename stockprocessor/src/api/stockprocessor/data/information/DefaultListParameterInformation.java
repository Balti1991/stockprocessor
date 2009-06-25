/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public class DefaultListParameterInformation<V extends Number> extends DefaultParamterInformation<V> implements ListParameterInformation<V>
{
	private final V[] values;

	private final String[] stringValues;

	/**
	 * @param defaultValue
	 * @param displayName
	 * @param values
	 * @param stringValues
	 */
	public DefaultListParameterInformation(V defaultValue, String displayName, V[] values, String[] stringValues)
	{
		super(displayName, defaultValue);

		this.values = values;
		this.stringValues = stringValues;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.data.information.ParameterInformation#type()
	 */
	@Override
	public ParameterType getType()
	{
		return ParameterType.LIST;
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
