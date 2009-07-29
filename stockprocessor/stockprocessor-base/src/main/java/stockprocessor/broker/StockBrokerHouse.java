/**
 * 
 */
package stockprocessor.broker;


/**
 * @author anti
 */
public interface StockBrokerHouse
{
	/**
	 * gives a bay or sell trigger for the broker, to execute the transaction
	 * 
	 * @param action
	 * @param instrumentum
	 * @param price
	 * @param amount
	 * @param stopLimit
	 */
	public void transfer(StockAction action, String instrument, int price, int amount, Integer stopLimit);

	public int getOwnedAmount(String instrument);
}
