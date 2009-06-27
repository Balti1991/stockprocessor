/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.ShareData;
import stockprocessor.handler.StockAction;

/**
 * @author anti
 */
public abstract class AbstractOscillationBroker<SD extends ShareData<?>> extends AbstractStockBroker<SD>
{
	private Double lastValue = null;

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.broker.AbstractStockBroker#calculateAction(stockprocessor
	 * .data.StockData)
	 */
	@Override
	public StockAction calculateAction(SD stockData)
	{
		double result = processData(stockData);

		StockAction stockAction = StockAction.NOP;

		// crossing above zero line
		if (lastValue < 0 && result > 0)
			stockAction = StockAction.BUY;
		// crossing below above zero line
		if (lastValue > 0 && result < 0)
			stockAction = StockAction.SELL;

		lastValue = result;

		return stockAction;
	}

	/**
	 * @param stockData
	 * @return
	 */
	protected abstract double processData(SD stockData);
}
