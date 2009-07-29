/**
 * 
 */
package stockprocessor.handler.processor.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.broker.StockAction;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.processor.AbstractDataProcessor;

/**
 * @author anti
 */
public class StockActionConverter extends AbstractDataProcessor<Object, ShareData<StockAction>>
{
	private static final String OUTPUT_INSTRUMENT = "StockAction";

	private static final Log log = LogFactory.getLog(StockActionConverter.class);

	public static final String PROCESSOR_NAME = "Stock action";

	public StockActionConverter()
	{
		setName(PROCESSOR_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#newDataArrivedNotification
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void newDataArrivedNotification(String input, Object inputData)
	{
		if (inputData instanceof ShareData<?>)
		{
			ShareData<?> shareData = (ShareData<?>) inputData;
			if (shareData.getValue() instanceof StockAction)
			{
				ShareData<StockAction> action = (ShareData<StockAction>) inputData;
				publishNewData(OUTPUT_INSTRUMENT, action);
			}
		}
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
	 * stockprocessor.handler.processor.AbstractDataProcessor#createInputParameters
	 * ()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = new DefaultParameterInformation("Input", ParameterType.STOCK_ACTION);
		list.add(parameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @seestockprocessor.handler.processor.AbstractDataProcessor#
	 * createOptionalParameters()
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
	 * stockprocessor.handler.processor.AbstractDataProcessor#createOutputParameters
	 * ()
	 */
	@Override
	protected List<ParameterInformation> createOutputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = new DefaultParameterInformation(OUTPUT_INSTRUMENT, ParameterType.STOCK_ACTION);
		list.add(parameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#setOptionalParameters(java
	 * .util.Map)
	 */
	@Override
	public void setOptionalParameters(Map<String, Object> optionalParameters)
	{
		// NOP
	}
}
