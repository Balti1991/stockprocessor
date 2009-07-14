/**
 * 
 */
package stockprocessor.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import stockprocessor.handler.processor.DataProcessor;
import stockprocessor.handler.processor.converter.CandleConverter;
import stockprocessor.handler.processor.converter.NumberConverter;

/**
 * @author anti
 */
public class DefaultProcessorManager extends AbstractManager<DataProcessor<?, ?>>
{
	public static final DefaultProcessorManager INSTANCE = new DefaultProcessorManager();

	private static final ArrayList<Manager<DataProcessor<?, ?>>> processorManagerList = new ArrayList<Manager<DataProcessor<?, ?>>>();

	private DefaultProcessorManager()
	{
		registerInstanceHandler(new InstanceHandler<DataProcessor<?, ?>>()
		{
			@Override
			public DataProcessor<?, ?> getInstance()
			{
				return new CandleConverter();
			}

			@Override
			public String getName()
			{
				return CandleConverter.PROCESSOR_NAME;
			}
		});

		registerInstanceHandler(new InstanceHandler<DataProcessor<?, ?>>()
		{
			@Override
			public DataProcessor<?, ?> getInstance()
			{
				return new NumberConverter();
			}

			@Override
			public String getName()
			{
				return NumberConverter.PROCESSOR_NAME;
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
		return "Base Processors";
	}

	/**
	 * @param processorManager
	 */
	public void registerProcessorManager(Manager<DataProcessor<?, ?>> processorManager)
	{
		processorManagerList.add(processorManager);
	}

	public Manager<DataProcessor<?, ?>> getProcessorManager(String name)
	{
		for (Manager<DataProcessor<?, ?>> manager : processorManagerList)
		{
			if (StringUtils.equals(manager.getName(), name))
				return manager;
		}

		return null;
	}

	public List<String> getAvailableManagers()
	{
		List<String> managers = new ArrayList<String>();
		managers.add(getName());

		for (Manager<DataProcessor<?, ?>> manager : processorManagerList)
		{
			managers.add(manager.getName());
		}

		return managers;
	}

	public List<String> getAvailableInstances(String dataProcessorName)
	{
		// create base list
		ArrayList<String> list = new ArrayList<String>();

		if (StringUtils.equals(getName(), dataProcessorName))
		{
			// add directly registered processors
			list.addAll(super.getAvailableInstances());
		}
		else
		{
			// add from external processor managers
			for (Manager<DataProcessor<?, ?>> processorManager : processorManagerList)
			{
				if (StringUtils.equals(processorManager.getName(), dataProcessorName))
				{
					list.addAll(processorManager.getAvailableInstances());
					break;
				}
			}
		}
		// finish
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.manager.AbstractManager#getAvailableInstances()
	 */
	@Override
	public List<String> getAvailableInstances()
	{
		// create base list
		ArrayList<String> list = new ArrayList<String>();

		// add from external processor managers
		for (Manager<DataProcessor<?, ?>> processorManager : processorManagerList)
		{
			list.addAll(processorManager.getAvailableInstances());
		}

		// add directly registered processors
		list.addAll(super.getAvailableInstances());

		// finish
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.manager.AbstractManager#getInstance(java.lang.String)
	 */
	@Override
	public DataProcessor<?, ?> getInstance(String dataProcessorName)
	{
		DataProcessor<?, ?> dataProcessor;

		// get from base list
		dataProcessor = super.getInstance(dataProcessorName);
		if (dataProcessor != null)
			return dataProcessor;

		// else try from external managers
		for (Manager<DataProcessor<?, ?>> processorManager : processorManagerList)
		{
			if (processorManager.getAvailableInstances().contains(dataProcessorName))
			{
				dataProcessor = processorManager.getInstance(dataProcessorName);
				if (dataProcessor != null)
					return dataProcessor;
			}
		}

		// else...
		return null;// TODO exception
	}

	public DataProcessor<?, ?> getInstance(String managerName, String dataProcessorName)
	{
		if (StringUtils.equals(getName(), managerName))
		{
			// get from base list
			return super.getInstance(dataProcessorName);
		}

		// else try from external managers
		for (Manager<DataProcessor<?, ?>> processorManager : processorManagerList)
		{
			if (StringUtils.equals(processorManager.getName(), managerName))
				return processorManager.getInstance(dataProcessorName);
		}

		// else...
		return null;// TODO exception
	}
}
