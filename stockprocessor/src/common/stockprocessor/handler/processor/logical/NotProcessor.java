/**
 * 
 */
package stockprocessor.handler.processor.logical;

import java.util.ArrayList;
import java.util.List;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public class NotProcessor extends LogicalProcessor
{
	/*
	 * (non-Javadoc)
	 * @see stockprocessor.processor.DataProcessor#getName()
	 */
	@Override
	public String getName()
	{
		return "NOT";
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.processor.DataProcessor#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return "Calculate logical NOT";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.AbstractDataProcessor#createInputParameters()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		// logical output
		ParameterInformation logicalParameterInformation = createParameterInformation("Source", ParameterType.LOGICAL);
		list.add(logicalParameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.DataReceiver#newDataArrivedNotification(java
	 * .lang.String, java.lang.Object)
	 */
	@Override
	public void newDataArrivedNotification(String instrument, Boolean inputData)
	{
		if (inputData == null)
			return;

		publishNewData(instrument, !inputData);
	}
}
