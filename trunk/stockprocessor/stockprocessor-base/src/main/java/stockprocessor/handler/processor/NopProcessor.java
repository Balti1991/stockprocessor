/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public class NopProcessor<D> extends AbstractDataProcessor<D, D>
{
	/**
	 * 
	 */
	public static final String OUTPUT = "Output";

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

		ParameterInformation parameterInformation = new DefaultParameterInformation("Input", ParameterType.STOCK_DATA);
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

		ParameterInformation parameterInformation = new DefaultParameterInformation(OUTPUT, ParameterType.STOCK_DATA_NUMBER);
		list.add(parameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#newDataArrivedNotification
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void newDataArrivedNotification(String input, D inputData)
	{
		publishNewData(OUTPUT, inputData);
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

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return "Repeats data";
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getName()
	 */
	@Override
	public String getName()
	{
		return "NOP";
	}
}
