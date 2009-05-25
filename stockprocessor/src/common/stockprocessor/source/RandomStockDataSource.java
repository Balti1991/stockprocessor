/**
 * 
 */
package stockprocessor.source;


import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import stockprocessor.data.StockData;
import stockprocessor.source.AbstractStockDataSource;
import stockprocessor.source.ImportTimer;

/**
 * @author anti
 */
public class RandomStockDataSource extends AbstractStockDataSource<StockData<Integer>>
{
	public static final String INSTRUMENT = "RND";

	private int lastValue = 10000;

	private Date baseDate = new Date();

	private final int dateStep;

	/**
	 * @param dateStep in seconds
	 * @param timerStep
	 */
	public RandomStockDataSource(final int dateStep, final long timerStep)
	{
		this.dateStep = dateStep;

		new ImportTimer(getName(), timerStep)
		{
			@Override
			protected void timeTick()
			{
				publishNewStockData(INSTRUMENT, generateStockData());
			}
		};
	}

	protected StockData<Integer> generateStockData()
	{
		// set time
		baseDate = DateUtils.addSeconds(baseDate, dateStep);

		double random = Math.random();
		int delta = (int) ((random - 0.5) * 100);
		lastValue = (lastValue + (delta));

		// System.out.println(lastValue + " + " + delta + " [" + random + "/" +
		// (random - 0.5) + "]");

		return new StockData<Integer>(baseDate, lastValue, (long) (100 * Math.random()));
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getName()
	 */
	@Override
	public String getName()
	{
		return "Random data Source";
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getAvailableInstruments()
	 */
	@Override
	public String[] getAvailableInstruments()
	{
		return new String[]
			{INSTRUMENT};
	}
}
