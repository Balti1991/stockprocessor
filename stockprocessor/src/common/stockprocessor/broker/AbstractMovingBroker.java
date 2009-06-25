/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.StockData;
import stockprocessor.handler.StockAction;

/**
 * @author anti
 */
public abstract class AbstractMovingBroker<SD extends StockData<?>> extends AbstractStockBroker<SD>
{
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

		// TODO

		StockAction stockAction = StockAction.NOP;

		return stockAction;
	}

	/**
	 * @param stockData
	 * @return
	 */
	protected abstract double processData(SD stockData);
}
