/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public interface RangeParameterInformation<V extends Number> extends NumberParameterInformation<V>
{
	/**
	 * @return
	 */
	public V getStart();

	/**
	 * @return
	 */
	public V getEnd();

	/**
	 * @return
	 */
	public V getIncrement();

	/**
	 * @return
	 */
	public V getPrecision();
}
