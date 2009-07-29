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
public class PercentageEvaluator extends AbstractEvaluatorProcessor
{
	public static final String PROCESSOR_NAME = "Percentage Evaluator";

	// upper margin
	private final double upper;

	// lower margin
	private final double lower;

	private boolean lastUp = false;

	private boolean lastDown = false;

	/**
	 * @param upper
	 * @param lower
	 */
	public PercentageEvaluator(double upper, double lower)
	{
		this.upper = upper;
		this.lower = lower;

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

		StockAction stockAction = StockAction.NOP;

		// fire signal
		if (result > lower && lastDown)
		{
			lastDown = false;
			stockAction = StockAction.BUY;
		}
		else if (result < upper && lastUp)
		{
			lastUp = false;
			stockAction = StockAction.SELL;
		}

		if (result < lower)
		{
			lastDown = true;
		}
		else if (result > upper)
		{
			lastUp = true;
		}

		return new ShareData<StockAction>(inputData.getName(), stockAction, inputData.getVolume(), inputData.getTimeStamp());
	}
}
