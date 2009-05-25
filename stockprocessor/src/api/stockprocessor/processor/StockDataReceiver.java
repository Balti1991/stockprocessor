/**
 * 
 */
package stockprocessor.processor;

import stockprocessor.data.StockData;

/**
 * @author anti
 */
public interface StockDataReceiver<SD extends StockData<?>>
{
	public String getName();

	public StockData<?> newDataArrivedNotification(String instrument, SD stockData);
}
