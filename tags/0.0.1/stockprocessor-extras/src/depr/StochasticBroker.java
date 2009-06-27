/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.StockData;
import stockprocessor.processor.StockAction;

/**
 * @author anti
 */
public class StochasticBroker<SD extends StockData<?>> implements StockBroker<SD>
{
	/*
	 * (non-Javadoc)
	 * @see stockprocessor.broker.StockBroker#getName()
	 */
	@Override
	public String getName()
	{
		return "Stochastic Broker";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.broker.StockBroker#newDataArrivedNotification(java.lang
	 * .String, stockprocessor.data.StockData)
	 */
	@Override
	public StockAction newDataArrivedNotification(String instrument, SD stockData)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
