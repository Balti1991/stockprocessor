/**
 * 
 */
package stockprocessor.handler.receiver;

import java.util.List;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.receiver.DataReceiver;

/**
 * @author anti
 */
public abstract class AbstractDataReceiver<I> implements DataReceiver<I>
{
	private List<ParameterInformation> inputParameters = null;

	private List<ParameterInformation> optParameters = null;

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.DataProcessor#getInputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getInputParameterInformations()
	{
		if (inputParameters == null)
		{
			inputParameters = createInputParameters();
		}

		return inputParameters;
	}

	protected abstract List<ParameterInformation> createInputParameters();

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.DataProcessor#getOptionalParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOptionalParameterInformations()
	{
		if (optParameters == null)
		{
			optParameters = createOptionalParameters();
		}

		return optParameters;
	}

	protected abstract List<ParameterInformation> createOptionalParameters();
}
