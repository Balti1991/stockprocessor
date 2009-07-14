/**
 * 
 */
package stockprocessor.manager;

import stockprocessor.handler.processor.DataProcessor;

/**
 * @author anti
 */
public class XmlProcessorManager extends AbstractManager<DataProcessor<?, ?>> implements ProcessorManager
{
	/**
	 * 
	 */
	public static final String NAME = "XML processors";

	public XmlProcessorManager()
	{
		// NOP
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.manager.Manager#getName()
	 */
	@Override
	public String getName()
	{
		return NAME;
	}
}
