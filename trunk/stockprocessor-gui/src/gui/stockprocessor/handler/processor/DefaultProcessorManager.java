/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import stockprocessor.handler.processor.converter.CandleConverter;
import stockprocessor.handler.processor.converter.NumberConverter;

/**
 * @author anti
 */
public class DefaultProcessorManager extends ProcessorManager
{
	private final ArrayList<String> processorList;

	public DefaultProcessorManager()
	{
		processorList = new ArrayList<String>();
		processorList.add(CandleConverter.PROCESSOR_NAME);
		processorList.add(NumberConverter.PROCESSOR_NAME);
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
		if (StringUtils.equals(CandleConverter.PROCESSOR_NAME, stockDataProcessorName))
			return new CandleConverter();
		else if (StringUtils.equals(NumberConverter.PROCESSOR_NAME, stockDataProcessorName))
			return new NumberConverter();
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
