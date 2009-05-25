/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.processor.Action;

/**
 * @author anti
 */
public interface StockBroker
{
	/**
	 * gives a bay trigger for the broker, to execute a default bay transaction
	 */
	public void bay();

	/**
	 * gives a sell trigger for the broker, to execute a default sell
	 * transaction
	 */
	public void sell();

	/**
	 * gives a bay or sell trigger for the broker, to execute the transaction
	 */
	public void transfer(Action action, int price, int amount, Integer stopLimit);
}
