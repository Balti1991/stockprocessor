/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public interface NumberParameterInformation<V extends Number> extends ParameterInformation
{
	/**
	 * the default value
	 * 
	 * @return
	 */
	public V getDefaultValue();
}
