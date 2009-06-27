/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.StockData;
import stockprocessor.handler.StockAction;

/**
 * @author anti
 */
public interface StockBroker<SD extends StockData<?>>
{
	public String getName();

	public StockAction newDataArrivedNotification(String instrument, SD stockData);

	public void setBrokerHouse(StockBrokerHouse stockBrokerHouse);
}
