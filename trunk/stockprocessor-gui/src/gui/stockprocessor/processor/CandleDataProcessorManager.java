/**
 * 
 */
package stockprocessor.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author anti
 */
public class CandleDataProcessorManager extends ProcessorManager
{
	private final ArrayList<String> processorList;

	public CandleDataProcessorManager()
	{
		processorList = new ArrayList<String>();
		processorList.add(CandleCollectorStockDataProcessor.CANDLE_DATA_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.ProcessorManager#getInstance(java.lang.
	 * String)
	 */
	@Override
	public StockDataProcessor<?> getInstance(String stockDataProcessorName)
	{
		if (StringUtils.equals(CandleCollectorStockDataProcessor.CANDLE_DATA_NAME, stockDataProcessorName))
			return new CandleCollectorStockDataProcessor();
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
