/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.data.ShareData;
import stockprocessor.handler.StockAction;

/**
 * @author anti
 */
public abstract class AbstractPercentageBroker<SD extends ShareData<?>> extends AbstractStockBroker<SD>
{
	// upper margin
	private final double upper;

	// lower margin
	private final double lower;

	private boolean lastUp = false;

	private boolean lastDown = false;

	/**
	 * @param upper
	 * @param lower
	 */
	public AbstractPercentageBroker(double upper, double lower)
	{
		this.upper = upper;
		this.lower = lower;
	}

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

		// fire signal
		if (result > lower && lastDown)
		{
			lastDown = false;
			stockAction = StockAction.BUY;
		}
		else if (result < upper && lastUp)
		{
			lastUp = false;
			stockAction = StockAction.SELL;
		}

		if (result < lower)
		{
			lastDown = true;
		}
		else if (result > upper)
		{
			lastUp = true;
		}

		return stockAction;
	}

	/**
	 * @param stockData
	 * @return
	 */
	protected abstract double processData(SD stockData);
}
