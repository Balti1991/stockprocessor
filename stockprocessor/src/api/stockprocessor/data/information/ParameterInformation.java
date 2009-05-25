/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public interface ParameterInformation<V extends Number>
{
	public enum ParameterType
	{
		LIST, RANGE;
	}

	/**
	 * parameters base type
	 * 
	 * @return
	 */
	public ParameterType type();

	/**
	 * the name to display
	 * 
	 * @return
	 */
	public String getDisplayName();

	/**
	 * the default value
	 * 
	 * @return
	 */
	public V getDefaultValue();
}
