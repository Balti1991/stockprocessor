/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.Candle;
import stockprocessor.data.ShareData;
import stockprocessor.handler.StockAction;

/**
 * @author anti
 */
public abstract class AbstractStockBroker<SD extends ShareData<?>> implements StockBroker<SD>
{
	private StockBrokerHouse stockBrokerHouse = null;

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.broker.StockBroker#setBrokerHouse(stockprocessor.broker
	 * .StockBrokerHouse)
	 */
	@Override
	public void setBrokerHouse(StockBrokerHouse stockBrokerHouse)
	{
		this.stockBrokerHouse = stockBrokerHouse;
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
		StockAction action = calculateAction(stockData);

		if (stockBrokerHouse != null)
		{
			int ownedAmount = stockBrokerHouse.getOwnedAmount(instrument);
			int amount = calculateAmount(stockData, action, ownedAmount);

			Object value = stockData.getValue();
			if (value instanceof Integer)
				stockBrokerHouse.transfer(action, instrument, (Integer) value, amount, null);
			if (value instanceof Candle)
				stockBrokerHouse.transfer(action, instrument, ((Candle) value).getClose(), amount, null);
		}

		return action;
	}

	/**
	 * calculate the action in situation
	 * 
	 * @param stockData
	 * @return
	 */
	protected abstract StockAction calculateAction(SD stockData);

	/**
	 * calculate the amount to trade in situation
	 * 
	 * @param stockData
	 * @param action
	 * @param ownedAmount
	 * @return
	 */
	protected abstract int calculateAmount(SD stockData, StockAction action, int ownedAmount);
}
