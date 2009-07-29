/**
 * 
 */
package stockprocessor.handler.processor.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.Candle;
import stockprocessor.data.CandleShareData;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.DefaultRangeParameterInformation;
import stockprocessor.data.information.NumberParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.processor.AbstractDataProcessor;

/**
 * @author anti
 */
public class CandleConverter extends AbstractDataProcessor<ShareData<?>, CandleShareData>
{
	/**
	 * 
	 */
	private static final String OUTPUT_INSTRUMENT = "CandleData";

	private static final Log log = LogFactory.getLog(CandleConverter.class);

	public static final String PROCESSOR_NAME = "Candle data";

	/**
	 * default value = 1 day
	 */
	private static final long DEFAULT_VALUE = 1l * 24;

	private static final String CANDLE_WIDTH_PARAMETER_NAME = "Candle width [hour]";

	// parameter candle width in hours
	private long candleWidth = DEFAULT_VALUE;

	// tmp candle
	private Candle candle;

	// tmp volume
	private long volume = 0;

	// tmp candles first data's date
	private Date startDate = null;

	/**
	 * @param candleWidth in milliseconds
	 */
	public CandleConverter()
	{
		setName(PROCESSOR_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.DataProcessor#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return "Compress input sources data into candles";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.DataProcessor#newDataArrivedNotification
	 * (hu.bogar.anti.stock.data.StockData)
	 */
	@Override
	public void newDataArrivedNotification(String instrument, ShareData<?> stockData)
	{
		log.debug("Received [" + instrument + "] instrument data [" + stockData + "] on [" + getName() + "]");

		// not first and outside of the candle interval
		if (startDate != null && startDate.getTime() + getCandleWidthInMilis() < stockData.getTimeStamp().getTime())
		{
			// create previous candle & send
			CandleShareData candleStockData = new CandleShareData(instrument, candle, getCandleWidthInMilis(), volume, startDate);
			publishNewData(OUTPUT_INSTRUMENT, candleStockData);
			log.debug("Sent " + instrument + " data [" + candleStockData + "]");

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
			startDate = new Date(((stockData.getTimeStamp().getTime() / getCandleWidthInMilis())) * getCandleWidthInMilis());
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
	}

	/**
	 * @return the candleWidth
	 */
	public long getCandleWidthInMilis()
	{
		return candleWidth * 60 * 60 * 1000;
	}

	/*
	 * (non-Javadoc)
	 * @seestockprocessor.processor.DataProcessor#
	 * setOptionalInputParameterInformations(java.util.Map)
	 */
	@Override
	public void setOptionalParameters(Map<String, Object> optionalParameters)
	{
		Object object = optionalParameters.get(CANDLE_WIDTH_PARAMETER_NAME);
		if (object != null)
		{
			this.candleWidth = ((Double) object).longValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.AbstractDataProcessor#createInputParameters()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = new DefaultParameterInformation("Input", ParameterType.STOCK_DATA);
		list.add(parameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.AbstractDataProcessor#createOptionalParameters()
	 */
	@Override
	protected List<ParameterInformation> createOptionalParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		NumberParameterInformation<Long> candleWidthParameter = new DefaultRangeParameterInformation<Long>(CANDLE_WIDTH_PARAMETER_NAME,
				DEFAULT_VALUE, 1l, Long.MAX_VALUE, 1l, 1l);
		list.add(candleWidthParameter);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.AbstractDataProcessor#createOutputParameters()
	 */
	@Override
	protected List<ParameterInformation> createOutputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = new DefaultParameterInformation(OUTPUT_INSTRUMENT, ParameterType.STOCK_DATA_CANDLE);
		list.add(parameterInformation);

		return list;
	}
}
