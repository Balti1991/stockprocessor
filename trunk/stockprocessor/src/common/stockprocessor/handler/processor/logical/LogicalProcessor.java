/**
 * 
 */
package stockprocessor.handler.processor.logical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.processor.AbstractDataProcessor;

/**
 * @author anti
 */
public abstract class LogicalProcessor extends AbstractDataProcessor<Boolean, Boolean>
{
	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.AbstractDataProcessor#createOutputParameters()
	 */
	@Override
	protected List<ParameterInformation> createOutputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		// logical output
		ParameterInformation logicalParameterInformation = createParameterInformation(getName(), ParameterType.LOGICAL);
		list.add(logicalParameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.AbstractDataProcessor#createOptionalParameters()
	 */
	@Override
	protected List<ParameterInformation> createOptionalParameters()
	{
		return new ArrayList<ParameterInformation>();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.DataProcessor#setOptionalParameterInformations
	 * (java.util.Map)
	 */
	@Override
	public void setOptionalParameters(Map<String, Object> optionalParameters)
	{
		// NOP
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#newDataArrivedNotification
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public synchronized void newDataArrivedNotification(String input, Boolean inputData)
	{
		// skip empty
		if (inputData == null)
			return;

		// calculate from new data
		Boolean result = calculate(input, inputData);

		// publish result
		publishNewData(getName(), result);
	}

	/**
	 * @param input
	 * @param inputData
	 * @return
	 */
	protected abstract Boolean calculate(String input, Boolean inputData);
}
