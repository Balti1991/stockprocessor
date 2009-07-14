/**
 * 
 */
package stockprocessor.handler.processor.relational;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.processor.AbstractDataProcessor;

/**
 * @author anti
 */
public abstract class AbstractRelationalProcessor<V> extends AbstractDataProcessor<V, Boolean>
{
	private static final String INPUT_A = "Input A";

	private static final String INPUT_B = "Input B";

	private V lastA = null;

	private V lastB = null;

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
		ParameterInformation logicalParameterInformation = new DefaultParameterInformation(getName(), ParameterType.LOGICAL);
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
	public synchronized void newDataArrivedNotification(String input, V inputData)
	{
		// skip empty
		if (inputData == null)
			return;

		// calculate from new data
		if (StringUtils.equals(INPUT_A, input))
			lastA = inputData;
		if (StringUtils.equals(INPUT_B, input))
			lastB = inputData;

		// wait for both inputs
		if (lastA == null || lastB == null)
			return;

		Boolean result = calculate(lastA, lastB);

		// publish result
		publishNewData(getName(), result);
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
		ParameterInformation logicalParameterInformation1 = new DefaultParameterInformation(INPUT_A, ParameterType.LOGICAL);
		list.add(logicalParameterInformation1);
		ParameterInformation logicalParameterInformation2 = new DefaultParameterInformation(INPUT_B, ParameterType.LOGICAL);
		list.add(logicalParameterInformation2);

		return list;
	}

	/**
	 * @param input
	 * @param inputDataA
	 * @param inputDataB
	 * @return
	 */
	protected abstract Boolean calculate(V inputDataA, V inputDataB);
}
