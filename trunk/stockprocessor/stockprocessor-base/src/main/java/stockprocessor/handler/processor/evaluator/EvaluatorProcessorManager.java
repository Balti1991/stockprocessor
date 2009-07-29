/**
 * 
 */
package stockprocessor.handler.processor.evaluator;

import stockprocessor.handler.InstanceHandler;
import stockprocessor.handler.processor.DataProcessor;
import stockprocessor.manager.AbstractManager;
import stockprocessor.manager.ProcessorManager;

/**
 * @author anti
 */
public class EvaluatorProcessorManager extends AbstractManager<DataProcessor<?, ?>> implements ProcessorManager
{
	public EvaluatorProcessorManager()
	{
		registerInstanceHandler(new InstanceHandler<DataProcessor<?, ?>>()
		{
			@Override
			public DataProcessor<?, ?> getInstance()
			{
				return new OscillationEvaluator();
			}

			@Override
			public String getName()
			{
				return OscillationEvaluator.PROCESSOR_NAME;
			}
		});

		registerInstanceHandler(new InstanceHandler<DataProcessor<?, ?>>()
		{
			@Override
			public DataProcessor<?, ?> getInstance()
			{
				return new PercentageEvaluator(80, 20);
			}

			@Override
			public String getName()
			{
				return PercentageEvaluator.PROCESSOR_NAME;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.manager.Manager#getName()
	 */
	@Override
	public String getName()
	{
		return "Evaluator processors";
	}

}
