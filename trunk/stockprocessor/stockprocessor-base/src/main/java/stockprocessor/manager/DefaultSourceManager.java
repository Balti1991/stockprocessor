package stockprocessor.manager;

import java.util.ArrayList;
import java.util.List;

import stockprocessor.handler.source.DataSource;

/**
 * @author anti
 */
public class DefaultSourceManager extends AbstractManager<DataSource<?>>
{
	public static final DefaultSourceManager INSTANCE = new DefaultSourceManager();

	private static final ArrayList<Manager<DataSource<?>>> sourceManagerList = new ArrayList<Manager<DataSource<?>>>();

	/**
	 * 
	 */
	private DefaultSourceManager()
	{
		// registerInstanceHandler(new InstanceHandler<DataSource<?>>()
		// {
		// @Override
		// public DataSource<?> getInstance()
		// {
		// return new RandomStockDataSource(5, 5 * 1000);
		// }
		//
		// @Override
		// public String getName()
		// {
		// return RandomStockDataSource.NAME;
		// }
		// });
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

	public void registerSourceManager(SourceManager sourceManager)
	{
		sourceManagerList.add(sourceManager);
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.source.SourceManagerIF#getAvailableSources()
	 */
	@Override
	public List<String> getAvailableInstances()
	{
		// create base list
		ArrayList<String> list = new ArrayList<String>();

		// add from external processor managers
		for (Manager<DataSource<?>> processorManager : sourceManagerList)
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
	public DataSource<?> getInstance(String dataSourceName)
	{
		DataSource<?> sataSource;

		// check base
		sataSource = super.getInstance(dataSourceName);
		if (sataSource != null)
			return sataSource;

		// try from external managers
		for (Manager<DataSource<?>> sourceManager : sourceManagerList)
		{
			sataSource = sourceManager.getInstance(dataSourceName);
			if (sataSource != null)
				return sataSource;
		}

		// else...
		return null;// TODO exception
	}
}
