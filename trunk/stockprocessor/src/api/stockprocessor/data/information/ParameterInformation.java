/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public interface ParameterInformation
{
	/**
	 * Parameter Type represents the value of parameter:
	 * <ul>
	 * <li><b>LIST</b> - selection from a list</li>
	 * <li><b>RANGE</b> - selection from a range</li>
	 * <li><b>ENUM</b> - enum-list</li>
	 * <li><b>LOGICAL</b> - logical (boolean)</li>
	 * <li><b>STOCK_ACTION</b> - stock source</li>
	 * <li><b>STOCK_DATA</b> - stock source</li>
	 * <li><b>STOCK_DATA_INTEGER</b> - Integer valued stock source</li>
	 * <li><b>STOCK_DATA_CANDLE</b> - Candle valued stock source</li>
	 * </ul>
	 * 
	 * @author anti
	 */
	public enum ParameterType
	{
		LIST, RANGE, ENUM, LOGICAL, STOCK_ACTION, STOCK_DATA, STOCK_DATA_NUMBER, STOCK_DATA_CANDLE;
	}

	/**
	 * parameters base type
	 * 
	 * @return
	 */
	public ParameterType getType();

	/**
	 * the name to display
	 * 
	 * @return
	 */
	public String getDisplayName();
}
