/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.Candle;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public class NumberStockDataProcessor extends AbstractDataProcessor<ShareData<?>, ShareData<Number>>
{
	private static final String OUTPUT_INSTRUMENT = "NumberData";

	private static final Log log = LogFactory.getLog(NumberStockDataProcessor.class);

	public static final String PROCESSOR_NAME = "Number data";

	/**
	 * @param candleWidth in milliseconds
	 */
	public NumberStockDataProcessor()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.AbstractDataProcessor#getName()
	 */
	@Override
	public String getName()
	{
		return PROCESSOR_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.DataProcessor#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return "Convert input sources data into numbers";
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
		log.debug("Received data [" + stockData + "] from [" + getName() + "]");

		Object value = stockData.getValue();
		if (value instanceof Integer)
		{
			Integer intValue = (Integer) value;
			publishNewData(OUTPUT_INSTRUMENT, new ShareData<Number>(instrument, intValue, stockData.getVolume(), stockData.getTimeStamp()));
		}
		else if (value instanceof Candle)
		{
			Candle candleValue = (Candle) value;
			publishNewData(OUTPUT_INSTRUMENT, new ShareData<Number>(instrument, candleValue.getClose(), stockData.getVolume(), stockData
					.getTimeStamp()));
		}

	}

	/*
	 * (non-Javadoc)
	 * @seestockprocessor.processor.DataProcessor#
	 * setOptionalInputParameterInformations(java.util.Map)
	 */
	@Override
	public void setOptionalParameters(Map<String, Object> optionalParameters)
	{
		// NOP
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.AbstractDataProcessor#createInputParameters()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = createParameterInformation("Input", ParameterType.STOCK_DATA);
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

		ParameterInformation parameterInformation = createParameterInformation(OUTPUT_INSTRUMENT, ParameterType.STOCK_DATA_INTEGER);
		list.add(parameterInformation);

		return list;
	}
}
