/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.ShareData;
import stockprocessor.handler.StockAction;

/**
 * @author anti
 */
public interface StockBroker<SD extends ShareData<?>>
{
	public String getName();

	public StockAction newDataArrivedNotification(String instrument, SD stockData);

	public void setBrokerHouse(StockBrokerHouse stockBrokerHouse);
}
