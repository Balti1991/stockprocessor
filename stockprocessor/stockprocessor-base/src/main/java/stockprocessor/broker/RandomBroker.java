/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.ShareData;

/**
 * @author anti
 */
public class RandomBroker extends AbstractStockBroker<ShareData<?>>
{
	/*
	 * (non-Javadoc)
	 * @see stockprocessor.broker.StockBroker#getName()
	 */
	@Override
	public String getName()
	{
		return "RND Broker";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.broker.AbstractStockBroker#calculateAction(stockprocessor
	 * .data.StockData)
	 */
	@Override
	protected StockAction calculateAction(ShareData<?> stockData)
	{
		double random = Math.random();

		if (random > 0.88)
		{
			return StockAction.BUY;
		}
		if (random < 0.11)
		{
			return StockAction.SELL;
		}

		return StockAction.NOP;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.broker.AbstractStockBroker#calculateAmount(stockprocessor
	 * .data.StockData, stockprocessor.processor.StockAction, int)
	 */
	@Override
	protected int calculateAmount(ShareData<?> stockData, StockAction action, int ownedAmount)
	{
		switch (action)
		{
		case BUY:
			// close short position or buy new
			return (ownedAmount < 0) ? -ownedAmount : 1;
		case SELL:
			// close long position or create new short
			return (ownedAmount > 0) ? ownedAmount : 1;

		default:
			return 0;
		}
	}
}
