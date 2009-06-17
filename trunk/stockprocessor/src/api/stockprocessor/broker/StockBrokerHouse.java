/**
 * 
 */
package stockprocessor.broker;

import stockprocessor.processor.StockAction;

/**
 * @author anti
 */
public interface StockBrokerHouse
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
	public void transfer(StockAction action, int price, int amount, Integer stopLimit);
}
