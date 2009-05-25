/**
 * 
 */
package stockprocessor.gui.processor;


import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import stockprocessor.data.Candle;
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

	private final StockDataReceiver<StockData<?>> stockDataReceiver;

	private final String instrument;

	public BaseElement(String instrument, StockDataReceiver<StockData<?>> stockDataReceiver)
	{
		this.instrument = instrument;
		this.stockDataReceiver = stockDataReceiver;

		// create datasets
		getDatasetOHLC().addSeries(new OHLCSeries(getName()));
		getDatasetTime().addSeries(new TimeSeries(getName(), Second.class));
	}

	public void storeData(String instrument, StockData<?> stockData)
	{
		Object value = stockData.getValue();

		if (value instanceof Candle)
		{
			Candle candle = (Candle) value;

			try
			{
				getDatasetOHLC().getSeries(0).add(new Second(stockData.getTime()), candle.getOpen(), candle.getMax(), candle.getMin(),
						candle.getClose());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (value instanceof Integer)
		{
			try
			{
				// FIXME
				getDatasetTime().getSeries(0).add(new Second(stockData.getTime()), ((Integer) value));
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}

	public String getName()
	{
		return stockDataReceiver.getName() + ": " + instrument;
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
	 * @see hu.bogar.anti.stock.gui.processor.Element#getDatasetTime()
	 */
	public TimeSeriesCollection getDatasetTime()
	{
		return datasetTime;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.StockDataReceiver#newDataArrivedNotification
	 * (java.lang.String, hu.bogar.anti.stock.data.StockData)
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

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getAvailableInstruments()
	 */
	@Override
	public String[] getAvailableInstruments()
	{
		return new String[]
			{stockDataReceiver.getName()};
	}
}
