package stockprocessor.handler.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stockprocessor.handler.source.DataSource;
import stockprocessor.handler.source.RandomStockDataSource;

/**
 * TODO register & unregister sources
 * 
 * @author anti
 */
public class SourceManager
{
	private final Map<String, DataSource<?>> sourceMap = new HashMap<String, DataSource<?>>();

	private final List<SourceManager> sourceManagerList = new ArrayList<SourceManager>();

	/**
	 * 
	 */
	public SourceManager()
	{
		registerStockDataSource(new RandomStockDataSource(5, 5 * 1000)); // RND
		// registerStockDataSource(new EbrokerHtmlStockDataSource()); // ebroker
		// registerStockDataSource(new PortfolioStockDataSource()); // portfolio
	}

	public void registerStockDataSource(DataSource<?> stockDataSource)
	{
		sourceMap.put(stockDataSource.getName(), stockDataSource);
	}

	public void registerSourceManager(SourceManager sourceManager)
	{
		sourceManagerList.add(sourceManager);
	}

	public List<String> getAvailableSources()
	{
		// create base list
		ArrayList<String> list = new ArrayList<String>();

		// add from external source managers
		for (SourceManager sourceManager : sourceManagerList)
		{
			list.addAll(sourceManager.getAvailableSources());
		}

		// add directly registered sources
		list.addAll(sourceMap.keySet());

		// finish
		return list;
	}

	public DataSource<?> getInstance(String stockDataSourceName)
	{
		DataSource<?> stockDataSource;

		// get from base list
		stockDataSource = sourceMap.get(stockDataSourceName);

		if (stockDataSource != null)
			return stockDataSource;

		// try from external managers
		for (SourceManager sourceManager : sourceManagerList)
		{
			stockDataSource = sourceManager.getInstance(stockDataSourceName);
			if (stockDataSource != null)
				return stockDataSource;
		}

		// else...
		return null;// TODO exception
	}
}
