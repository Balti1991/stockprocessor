/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author anti
 */
public class DefaultProcessorManager extends ProcessorManager
{
	private final ArrayList<String> processorList;

	public DefaultProcessorManager()
	{
		processorList = new ArrayList<String>();
		processorList.add(CandleCollectorStockDataProcessor.PROCESSOR_NAME);
		processorList.add(NumberStockDataProcessor.PROCESSOR_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.ProcessorManager#getInstance(java.lang.
	 * String)
	 */
	@Override
	public DataProcessor<?, ?> getInstance(String stockDataProcessorName)
	{
		if (StringUtils.equals(CandleCollectorStockDataProcessor.PROCESSOR_NAME, stockDataProcessorName))
			return new CandleCollectorStockDataProcessor();
		else if (StringUtils.equals(NumberStockDataProcessor.PROCESSOR_NAME, stockDataProcessorName))
			return new NumberStockDataProcessor();
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.ProcessorManager#getAvailableProcessors()
	 */
	@Override
	public List<String> getAvailableProcessors()
	{
		return processorList;
	}

}
