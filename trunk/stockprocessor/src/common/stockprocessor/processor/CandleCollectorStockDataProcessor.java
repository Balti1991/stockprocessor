/**
 * 
 */
package stockprocessor.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.Candle;
import stockprocessor.data.CandleStockData;
import stockprocessor.data.StockData;
import stockprocessor.data.information.DefaultRangeParameterInformation;
import stockprocessor.data.information.ParameterInformation;

/**
 * @author anti
 */
public class CandleCollectorStockDataProcessor implements StockDataProcessor<StockData<?>>
{
	/**
	 * default value = 1 day
	 */
	private static final long DEFAULT_VALUE = 1l * 24;

	/**
	 * 
	 */
	private static final String CANDLE_WIDTH_PARAMETER_NAME = "Candle width [hour]";

	public static final String CANDLE_DATA_NAME = "Candle data";

	private static final Log log = LogFactory.getLog(CandleCollectorStockDataProcessor.class);

	public static final ParameterInformation<Long> candleWidthParameter = new DefaultRangeParameterInformation<Long>(CANDLE_WIDTH_PARAMETER_NAME,
			DEFAULT_VALUE, 1l, Long.MAX_VALUE, 1l, 1l);

	@SuppressWarnings("unchecked")
	public static final List<ParameterInformation> optionalInputParameters = new ArrayList<ParameterInformation>();
	static
	{
		optionalInputParameters.add(candleWidthParameter);
	}

	// parameter candle width in hours
	private long candleWidth;

	// tmp candle
	private Candle candle;

	// tmp volume
	private long volume = 0;

	// tmp candles first data's date
	private Date startDate = null;

	private StockDataProcessor<StockData<?>> stockDataProcessor = null;

	/**
	 * @param candleWidth in milliseconds
	 */
	public CandleCollectorStockDataProcessor()
	{
		this.candleWidth = candleWidthParameter.getDefaultValue();
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.StockDataProcessor#getName()
	 */
	@Override
	public String getName()
	{
		StockDataProcessor<StockData<?>> dataProcessor = getStockDataProcessor();
		if (dataProcessor == null)
			return CANDLE_DATA_NAME;
		else
			return CANDLE_DATA_NAME + ": " + dataProcessor.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.StockDataProcessor#newDataArrivedNotification
	 * (hu.bogar.anti.stock.data.StockData)
	 */
	@Override
	public StockData<?> newDataArrivedNotification(String instrument, StockData<?> stockData)
	{
		log.debug("Received data [" + stockData + "] from [" + getName() + "]");

		StockData<?> notification = null;
		// not first and outside of the candle interval
		if (startDate != null && startDate.getTime() + getCandleWidthInMilis() < stockData.getTime().getTime())
		{
			// create previous candle & send
			CandleStockData candleStockData = new CandleStockData(candle, startDate, getCandleWidthInMilis(), volume);

			StockDataProcessor<StockData<?>> processor = getStockDataProcessor();
			if (processor != null)
			{
				notification = getStockDataProcessor().newDataArrivedNotification(instrument, candleStockData);
				log.debug("Sent candle data to processor [" + candleStockData + "] and received data [" + notification + "]");
			}
			else
			{
				notification = candleStockData;
				log.debug("Sending candle data [" + candleStockData + "]");
			}

			// clear date and volume
			startDate = null;
			volume = 0;
		}

		Object value = stockData.getValue();

		// first data in the interval
		if (startDate == null)
		{
			// store values
			if (value instanceof Integer)
			{
				Integer intValue = (Integer) value;
				candle = new Candle(intValue);
			}
			else if (value instanceof Candle)
			{
				Candle candleValue = (Candle) value;
				candle = candleValue;
			}

			// create first time
			startDate = new Date(((stockData.getTime().getTime() / getCandleWidthInMilis())) * getCandleWidthInMilis());
		}
		else
		{
			if (value instanceof Integer)
			{
				Integer intValue = (Integer) value;
				candle.addValue(intValue);
			}
			else if (value instanceof Candle)
			{
				Candle candleValue = (Candle) value;
				candle.addValue(candleValue);
			}
		}

		volume += stockData.getVolume();

		return notification;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.StockDataProcessor#getDescription()
	 */
	@Override
	public String getDescription()
	{
		StockDataProcessor<StockData<?>> processor = getStockDataProcessor();
		if (processor == null)
			return "Compress input sources data into candles";
		else
			return processor.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * @seehu.bogar.anti.stock.processor.StockDataProcessor#
	 * getInputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getInputParameterInformations()
	{
		return new ArrayList<ParameterInformation>();
	}

	/*
	 * (non-Javadoc)
	 * @seehu.bogar.anti.stock.processor.StockDataProcessor#
	 * getOptionalInputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOptionalInputParameterInformations()
	{
		return optionalInputParameters;
	}

	/*
	 * (non-Javadoc)
	 * @seestockprocessor.processor.StockDataProcessor#
	 * setOptionalInputParameterInformations(java.util.Map)
	 */
	@Override
	public void setOptionalInputParameterInformations(Map<String, Object> optInputParameters)
	{
		Object object = optInputParameters.get(CANDLE_WIDTH_PARAMETER_NAME);
		if (object != null)
		{
			this.candleWidth = ((Double) object).longValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * @seehu.bogar.anti.stock.processor.StockDataProcessor#
	 * getOutputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOutputParameterInformations()
	{
		return new ArrayList<ParameterInformation>();
	}

	/**
	 * @param stockDataProcessor the stockDataProcessor to set
	 */
	public void setStockDataProcessor(StockDataProcessor<StockData<?>> stockDataProcessor)
	{
		this.stockDataProcessor = stockDataProcessor;
	}

	/**
	 * @return the stockDataProcessor
	 */
	public StockDataProcessor<StockData<?>> getStockDataProcessor()
	{
		return stockDataProcessor;
	}

	/**
	 * @return the candleWidth
	 */
	public long getCandleWidthInMilis()
	{
		return candleWidth * 60 * 60 * 1000;
	}
}
