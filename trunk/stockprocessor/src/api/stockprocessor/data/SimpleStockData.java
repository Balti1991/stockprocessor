/**
 * 
 */
package stockprocessor.data;

import java.util.Date;

/**
 * @author anti
 */
public class SimpleStockData extends StockData<Integer>
{
	/**
	 * @param time
	 * @param value
	 * @param volume
	 */
	public SimpleStockData(Date time, Integer value, long volume)
	{
		super(time, value, volume);
	}
}
