/**
 * 
 */
package stockprocessor.processor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stockprocessor.processor.StockDataProcessor;

/**
 * @author anti
 */
public class ProcessorManager
{
	private final Map<String, StockDataProcessor<?>> processorMap = new HashMap<String, StockDataProcessor<?>>();

	private final List<ProcessorManager> processorManagerList = new ArrayList<ProcessorManager>();

	public void registerStockDataProcessor(StockDataProcessor<?> stockDataProcessor)
	{
		processorMap.put(stockDataProcessor.getName(), stockDataProcessor);
	}

	public void registerProcessorManager(ProcessorManager processorManager)
	{
		processorManagerList.add(processorManager);
	}

	public List<String> getAvailableProcessors()
	{
		// create base list
		ArrayList<String> list = new ArrayList<String>();

		// add from external processor managers
		for (ProcessorManager processorManager : processorManagerList)
		{
			list.addAll(processorManager.getAvailableProcessors());
		}

		// add directly registered processors
		list.addAll(processorMap.keySet());

		// finish
		return list;
	}

	public StockDataProcessor<?> getInstance(String stockDataProcessorName)
	{
		StockDataProcessor<?> stockDataProcessor;

		// get from base list
		stockDataProcessor = processorMap.get(stockDataProcessorName);
		if (stockDataProcessor != null)
			return stockDataProcessor;

		// try from external managers
		for (ProcessorManager processorManager : processorManagerList)
		{
			stockDataProcessor = processorManager.getInstance(stockDataProcessorName);
			if (stockDataProcessor != null)
				return stockDataProcessor;
		}

		// else...
		return null;// TODO exception
	}
}
