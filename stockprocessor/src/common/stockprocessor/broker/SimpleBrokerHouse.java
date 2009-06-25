/**
 * 
 */
package stockprocessor.broker;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.handler.StockAction;
import stockprocessor.util.Pair;

/**
 * @author anti
 */
public class SimpleBrokerHouse implements StockBrokerHouse
{
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(SimpleBrokerHouse.class);

	/**
	 * map with owned instruments: the amount & the price
	 */
	protected final Map<String, Pair<Integer, Double>> ownedInstruments = new HashMap<String, Pair<Integer, Double>>();

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.broker.StockBrokerHouse#transfer(stockprocessor.processor
	 * .StockAction, java.lang.String, int, int, java.lang.Integer)
	 */
	@Override
	public void transfer(StockAction action, String instrument, int price, int amount, Integer stopLimit)
	{
		// prepare
		amount = Math.abs(amount);

		if (!ownedInstruments.containsKey(instrument))
		{
			ownedInstruments.put(instrument, new Pair<Integer, Double>(0, 0d));
		}

		Pair<Integer, Double> current = ownedInstruments.get(instrument);

		// get current amount & price
		int ownedAmount = (current.getFirst() == null) ? 0 : current.getFirst();
		double ownedPrice = (current.getSecond() == null) ? 0 : current.getSecond();

		double profit = 0;

		// new current amount & price
		int newAmount;
		double newPrice;

		switch (action)
		{
		case BUY:
			// if in short position
			if (ownedAmount < 0)
			{
				// the closed position
				int close = Math.min(Math.abs(ownedAmount), Math.abs(amount));

				// calculate profit
				profit = close * (ownedPrice - price);

				// recreate input data
				ownedAmount = ownedAmount + close;
				amount = amount - close;
			}

			// calculate new position
			newAmount = ownedAmount + amount;
			// calculate new price
			newPrice = newAmount != 0 ? (ownedAmount * ownedPrice + amount * price) / newAmount : 0;

			break;
		case SELL:
			// if in long position
			if (ownedAmount > 0)
			{
				// the closed position
				int close = Math.min(Math.abs(ownedAmount), Math.abs(amount));

				// calculate profit
				profit = close * (price - ownedPrice);

				// recreate input data
				ownedAmount = ownedAmount - close;
				amount = amount - close;
			}

			// calculate new position
			newAmount = ownedAmount - amount;
			newPrice = newAmount != 0 ? (ownedAmount * ownedPrice - amount * price) / newAmount : 0;

			// calculate profit for long position closing
			if (ownedAmount > 0)
				profit = Math.min(Math.abs(ownedAmount), Math.abs(amount)) * (price - ownedPrice);
			break;

		default:
			return;
		}

		if (newAmount == 0)
		{
			// if position is closed, remove from map
			ownedInstruments.remove(instrument);
		}
		else
		{
			// store
			current.setFirst(newAmount);
			current.setSecond(newPrice);
		}

		if (log.isInfoEnabled())
		{
			log.info("Shares " + instrument + " changed on action [" + action + "] to " + newAmount + " new avarage price is " + newPrice
					+ (profit == 0 ? "" : " Profit: " + profit));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.broker.StockBrokerHouse#getOwned(java.lang.String)
	 */
	@Override
	public int getOwnedAmount(String instrument)
	{
		Pair<Integer, Double> pair = ownedInstruments.get(instrument);
		if (pair != null)
			return pair.getFirst();

		return 0;
	}
}
