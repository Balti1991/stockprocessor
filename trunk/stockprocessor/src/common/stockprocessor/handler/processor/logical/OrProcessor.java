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
public class OrProcessor extends LogicalProcessor
{
	/*
	 * (non-Javadoc)
	 * @see stockprocessor.processor.DataProcessor#getName()
	 */
	@Override
	public String getName()
	{
		return "OR";
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.processor.DataProcessor#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return "Calculate logical OR";
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
		ParameterInformation logicalParameterInformation1 = createParameterInformation("Source 1", ParameterType.LOGICAL);
		list.add(logicalParameterInformation1);
		ParameterInformation logicalParameterInformation2 = createParameterInformation("Source 2", ParameterType.LOGICAL);
		list.add(logicalParameterInformation2);

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

		// TODO
		publishNewData(instrument, !inputData);
	}
}
