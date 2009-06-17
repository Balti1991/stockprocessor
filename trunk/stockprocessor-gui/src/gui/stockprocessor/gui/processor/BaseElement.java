/**
 * 
 */
package stockprocessor.gui.processor;

import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import stockprocessor.data.StockData;
import stockprocessor.processor.StockDataReceiver;
import stockprocessor.source.AbstractStockDataSource;

/**
 * @author anti
 */
public class BaseElement extends AbstractStockDataSource<StockData<?>> implements Element, StockDataReceiver<StockData<?>>
{
	private final OHLCSeriesCollection datasetOHLC = new OHLCSeriesCollection();

	private final TimeSeriesCollection datasetTime = new TimeSeriesCollection();

	protected final StockDataReceiver<StockData<?>> stockDataReceiver;

	protected final String instrument;

	/**
	 * 
	 */
	public BaseElement(String instrument, StockDataReceiver<StockData<?>> stockDataReceiver)
	{
		this.instrument = instrument;
		this.stockDataReceiver = stockDataReceiver;
	}

	public void storeData(String instrument, StockData<?> stockData)
	{
		Object value = stockData.getValue();

		if (value instanceof Integer)
		{
			try
			{
				getDatasetTime().getSeries(0).add(getTimePeriod(stockData), ((Integer) value));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("Unknown stock value [" + value + "]");
		}
	}

	protected RegularTimePeriod getTimePeriod(StockData<?> stockData)
	{
		return new Second(stockData.getTime());
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public String getName()
	{
		return stockDataReceiver.getName() + ": " + instrument;
	}

	public TimeSeriesCollection getDatasetTime()
	{
		return datasetTime;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.gui.processor.Element#getDatasetOHLC()
	 */
	public OHLCSeriesCollection getDatasetOHLC()
	{
		return datasetOHLC;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.StockDataReceiver#newDataArrivedNotification
	 * (java.lang.String, stockprocessor.data.StockData)
	 */
	@Override
	public StockData<?> newDataArrivedNotification(String instrument, StockData<?> stockData)
	{
		StockData<?> data = stockDataReceiver.newDataArrivedNotification(instrument, stockData);

		if (data != null)
		{
			// visualize
			storeData(instrument, data);
			// publish
			publishNewStockData(getName(), data);
		}

		return data;
	}

	/****************
	 * data source
	 ****************/
	@Override
	public String[] getAvailableInstruments()
	{
		return new String[]
			{stockDataReceiver.getName()};
	}

}