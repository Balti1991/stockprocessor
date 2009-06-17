/**
 * 
 */
package stockprocessor.gui.processor;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.ohlc.OHLCSeries;

import stockprocessor.data.Candle;
import stockprocessor.data.StockData;
import stockprocessor.processor.StockDataReceiver;

/**
 * @author anti
 */
public class ChartElement extends BaseElement
{
	public ChartElement(String instrument, StockDataReceiver<StockData<?>> stockDataReceiver)
	{
		super(instrument, stockDataReceiver);

		// create datasets
		getDatasetOHLC().addSeries(new OHLCSeries(getName()));
		getDatasetTime().addSeries(new TimeSeries(getName(), Second.class));
	}

	@Override
	public void storeData(String instrument, StockData<?> stockData)
	{
		Object value = stockData.getValue();

		if (value instanceof Candle)
		{
			Candle candle = (Candle) value;

			try
			{
				getDatasetOHLC().getSeries(0).add(getTimePeriod(stockData), candle.getOpen(), candle.getMax(), candle.getMin(), candle.getClose());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			super.storeData(instrument, stockData);
		}
	}
}
