/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stockprocessor.handler.processor.DataProcessor;

/**
 * @author anti
 */
public class ProcessorManager
{
	private final Map<String, DataProcessor<?, ?>> processorMap = new HashMap<String, DataProcessor<?, ?>>();

	private final List<ProcessorManager> processorManagerList = new ArrayList<ProcessorManager>();

	public void registerDataProcessor(DataProcessor<?, ?> dataProcessor)
	{
		processorMap.put(dataProcessor.getName(), dataProcessor);
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

	public DataProcessor<?, ?> getInstance(String dataProcessorName)
	{
		DataProcessor<?, ?> dataProcessor;

		// get from base list
		dataProcessor = processorMap.get(dataProcessorName);
		if (dataProcessor != null)
			return dataProcessor;

		// try from external managers
		for (ProcessorManager processorManager : processorManagerList)
		{
			dataProcessor = processorManager.getInstance(dataProcessorName);
			if (dataProcessor != null)
				return dataProcessor;
		}

		// else...
		return null;// TODO exception
	}
}
