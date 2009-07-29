/**
 * 
 */
package stockprocessor.handler.processor.evaluator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import stockprocessor.broker.StockAction;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public class OscillationEvaluator extends AbstractEvaluatorProcessor
{
	public static final String PROCESSOR_NAME = "Oscillation Evaluator";

	private Double lastValue = null;

	public OscillationEvaluator()
	{
		setName(PROCESSOR_NAME);
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

		// logical output
		ParameterInformation logicalParameterInformation = new DefaultParameterInformation("Source", ParameterType.LOGICAL);
		list.add(logicalParameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return PROCESSOR_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.evaluator.AbstractEvaluatorProcessor
	 * #calculate(java.lang.String, stockprocessor.data.ShareData)
	 */
	@Override
	protected ShareData<StockAction> calculate(String input, ShareData<?> inputData)
	{
		if (!(inputData.getValue() instanceof Number))
			return null;

		double result = NumberUtils.toDouble(inputData.getValue().toString()); // FIXME

		if (lastValue == null)
		{
			lastValue = result;
			return null;
		}

		StockAction stockAction = StockAction.NOP;

		// crossing above zero line
		if (lastValue < 0 && result > 0)
			stockAction = StockAction.BUY;
		// crossing below above zero line
		if (lastValue > 0 && result < 0)
			stockAction = StockAction.SELL;

		lastValue = result;

		return new ShareData<StockAction>(inputData.getName(), stockAction, inputData.getVolume(), inputData.getTimeStamp());
	}
}
