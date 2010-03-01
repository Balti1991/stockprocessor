/**
 * 
 */
package stockprocessor.handler.processor.evaluator;

import java.util.ArrayList;
import java.util.List;

import stockprocessor.broker.StockAction;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public class MovingEvaluator extends AbstractEvaluatorProcessor
{
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
	 * @see stockprocessor.handler.DataHandler#getName()
	 */
	@Override
	public String getName()
	{
		return "Moving Evaluator";
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return "Moving Evaluator";
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
		// TODO Auto-generated method stub
		return null;
	}
}
