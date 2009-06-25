/**
 * 
 */
package stockprocessor.cli;

import stockprocessor.handler.StockAction;

/**
 * Helper to evaluate technical analysis results and generate stock action
 * (BUY&SELL signals)
 * 
 * @author anti
 */
public class StockBrokerHelper
{
	public static final StockBrokerHelper instance = new StockBrokerHelper();

	private StockBrokerHelper()
	{
	}

	/**
	 * Generate StockAction from *MovingAverage result
	 * 
	 * @param result
	 * @return
	 */
	public StockAction evaluateMovingAverage(double result)
	{
		if (result > 0)
			return StockAction.BUY;
		else if (result < 0)
			return StockAction.SELL;
		else
			return StockAction.NOP;
	}

	/**
	 * Generate StockAction from Stochastic result
	 * 
	 * @param resultK
	 * @param resultD
	 * @return
	 */
	public StockAction evaluateStochastic(double resultK, double resultD)
	{
		if (resultK > resultD)
			return StockAction.BUY;
		else if (resultK < resultD)
			return StockAction.SELL;
		else
			return StockAction.NOP;
	}

	/**
	 * @param result
	 * @return
	 */
	public StockAction evaluateTrix(double result)
	{
		if (result > 0.8)
			return StockAction.BUY;
		else if (result < 0.2)
			return StockAction.SELL;
		else
			return StockAction.NOP;
	}

	/**
	 * @param result
	 * @return
	 */
	public StockAction evaluatRsi(double result)
	{
		if (result > 0.8)
			return StockAction.BUY;
		else if (result < 0.2)
			return StockAction.SELL;
		else
			return StockAction.NOP;
	}
}
