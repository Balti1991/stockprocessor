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
public class CandleCollectorStockDataProcessor implements StockDataProcessor<StockData<Integer>>
{
	/**
	 * 
	 */
	private static final String CANDLE_WIDTH_PARAMETER_NAME = "Candle width";

	public static final String CANDLE_DATA_NAME = "Candle data";

	private static final Log log = LogFactory.getLog(CandleCollectorStockDataProcessor.class);

	public static final ParameterInformation<Long> candleWidthParameter = new DefaultRangeParameterInformation<Long>(CANDLE_WIDTH_PARAMETER_NAME,
			1l * 60 * 60 * 24, 1l, Long.MAX_VALUE, 1l, 1l);

	@SuppressWarnings("unchecked")
	public static final List<ParameterInformation> optionalInputParameters = new ArrayList<ParameterInformation>();
	static
	{
		optionalInputParameters.add(candleWidthParameter);
	}

	// parameter
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
	public StockData<?> newDataArrivedNotification(String instrument, StockData<Integer> stockData)
	{
		log.debug("Received data [" + stockData + "] from [" + getName() + "]");

		StockData<?> notification = null;
		// not first and outside of the candle interval
		if (startDate != null && startDate.getTime() + candleWidth < stockData.getTime().getTime())
		{
			// create previous candle & send
			CandleStockData candleStockData = new CandleStockData(candle, startDate, candleWidth, volume);

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

		// first data in the interval
		if (startDate == null)
		{
			// store values
			candle = new Candle(stockData.getValue(), stockData.getValue(), stockData.getValue(), stockData.getValue());

			// create first time
			startDate = new Date(((stockData.getTime().getTime() / candleWidth)) * candleWidth);
		}
		else
		{
			// check minimum
			if (stockData.getValue() < candle.getMin())
				candle = new Candle(candle.getOpen(), candle.getClose(), stockData.getValue(), candle.getMax());

			// check maximum
			if (candle.getMax() < stockData.getValue())
				candle = new Candle(candle.getOpen(), candle.getClose(), candle.getMin(), stockData.getValue());
		}

		// always store the closing and volume informations
		candle = new Candle(candle.getOpen(), stockData.getValue(), candle.getMin(), candle.getMax());
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
			this.candleWidth = (Long) object;
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
}
