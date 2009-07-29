/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.ShareData;
import stockprocessor.util.Pair;

/**
 * @author anti
 */
public abstract class AbstractSignalLineBroker<SD extends ShareData<?>> extends AbstractStockBroker<SD>
{
	private Boolean lastBelow = null;

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.broker.AbstractStockBroker#calculateAction(stockprocessor
	 * .data.StockData)
	 */
	@Override
	public StockAction calculateAction(SD stockData)
	{
		Pair<Double, Double> result = processData(stockData);

		Boolean currentBelow = null;
		if (result.getFirst() < result.getSecond())
			currentBelow = false;
		if (result.getFirst() > result.getSecond())
			currentBelow = true;

		StockAction stockAction = StockAction.NOP;
		if (lastBelow && !currentBelow)
			stockAction = StockAction.BUY;
		if (!lastBelow && currentBelow)
			stockAction = StockAction.SELL;

		// store
		lastBelow = currentBelow;

		return stockAction;
	}

	/**
	 * @param stockData
	 * @return First: main line, Second: signal line
	 */
	protected abstract Pair<Double, Double> processData(SD stockData);
}
