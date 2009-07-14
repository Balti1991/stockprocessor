/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public interface ListParameterInformation<V extends Number> extends NumberParameterInformation<V>
{
	/**
	 * the value list
	 * 
	 * @return
	 */
	public V[] getValues();

	/**
	 * the human readable form of values
	 * 
	 * @return
	 */
	public String[] getStringValues();
}
