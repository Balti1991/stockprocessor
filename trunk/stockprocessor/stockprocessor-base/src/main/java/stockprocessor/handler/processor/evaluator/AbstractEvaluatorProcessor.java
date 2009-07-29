/**
 * 
 */
package stockprocessor.handler.processor.evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import stockprocessor.broker.StockAction;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.processor.AbstractDataProcessor;

/**
 * @author anti
 */
public abstract class AbstractEvaluatorProcessor extends AbstractDataProcessor<ShareData<?>, ShareData<StockAction>>
{
	public static final String OUTPUT_NAME = "Action";

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
		ParameterInformation logicalParameterInformation = new DefaultParameterInformation(OUTPUT_NAME, ParameterType.STOCK_ACTION);
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
	public void newDataArrivedNotification(String input, ShareData<?> inputData)
	{
		// skip empty
		if (inputData == null)
			return;

		// calculate from new data
		ShareData<StockAction> result = calculate(input, inputData);

		// publish result
		publishNewData(OUTPUT_NAME, result);
	}

	/**
	 * @param input
	 * @param inputData
	 * @return
	 */
	protected abstract ShareData<StockAction> calculate(String input, ShareData<?> inputData);
}
