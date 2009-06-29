/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public class DefaultRangeParameterInformation<V extends Number> extends DefaultNumberParamterInformation<V> implements RangeParameterInformation<V>
{
	private final V end;

	private final V increment;

	private final V precision;

	private final V start;

	/**
	 * @param displayName
	 * @param defaultValue
	 * @param start
	 * @param end
	 * @param increment
	 * @param precision
	 */
	public DefaultRangeParameterInformation(String displayName, V defaultValue, V start, V end, V increment, V precision)
	{
		super(displayName, ParameterType.RANGE, defaultValue);

		this.end = end;
		this.increment = increment;
		this.precision = precision;
		this.start = start;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.data.information.RangeParameterInformation#end()
	 */
	@Override
	public V getEnd()
	{
		return end;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.data.information.RangeParameterInformation#increment
	 * ()
	 */
	@Override
	public V getIncrement()
	{
		return increment;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.data.information.RangeParameterInformation#precision
	 * ()
	 */
	@Override
	public V getPrecision()
	{
		return precision;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.data.information.RangeParameterInformation#start()
	 */
	@Override
	public V getStart()
	{
		return start;
	}
}
