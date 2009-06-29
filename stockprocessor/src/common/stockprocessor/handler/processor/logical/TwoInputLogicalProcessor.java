/**
 * 
 */
package stockprocessor.handler.processor.logical;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public abstract class TwoInputLogicalProcessor extends LogicalProcessor
{
	private static final String INPUT_A = "Input A";

	private static final String INPUT_B = "Input B";

	private Boolean lastA = null;

	private Boolean lastB = null;

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

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.logical.LogicalProcessor#calculate(java
	 * .lang.String, java.lang.Boolean)
	 */
	@Override
	protected Boolean calculate(String input, Boolean inputData)
	{
		if (StringUtils.equals(INPUT_A, input))
			lastA = inputData;
		if (StringUtils.equals(INPUT_B, input))
			lastB = inputData;

		// wait for both input
		if (lastA == null || lastB == null)
			return null;

		return calculate(input, lastA, lastB);
	}

	/**
	 * @param input
	 * @param inputDataA
	 * @param inputDataB
	 * @return
	 */
	protected abstract Boolean calculate(String input, Boolean inputDataA, Boolean inputDataB);
}
