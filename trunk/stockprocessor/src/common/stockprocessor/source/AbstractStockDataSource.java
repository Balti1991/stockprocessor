package stockprocessor.source;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.StockData;
import stockprocessor.processor.StockDataReceiver;
import stockprocessor.source.StockDataSource;

/**
 * @author anti
 */
public abstract class AbstractStockDataSource<SD extends StockData<?>> implements StockDataSource<SD>
{
	private static final Log log = LogFactory.getLog(AbstractStockDataSource.class);

	private final Map<String, List<StockDataReceiver<SD>>> stockDataReceivers = new HashMap<String, List<StockDataReceiver<SD>>>();

	// private final List<StockDataProcessor<SD>> stockDataProcessors = new
	// ArrayList<StockDataProcessor<SD>>();

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.StockDataSource#registerDataProcessor(hu.bogar
	 * .anti.stock.processor.StockDataProcessor)
	 */
	@Override
	public void registerDataReceiver(String instrument, StockDataReceiver<SD> dataReceiver)
	{
		List<StockDataReceiver<SD>> receivers = stockDataReceivers.get(instrument);

		if (receivers == null)
		{
			receivers = new ArrayList<StockDataReceiver<SD>>();
			stockDataReceivers.put(instrument, receivers);
		}

		// if not yet on list
		if (!receivers.contains(dataReceiver))
		{
			// add it
			receivers.add(dataReceiver);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.StockDataSource#removeDataProcessor(hu.bogar
	 * .anti.stock.processor.StockDataProcessor)
	 */
	@Override
	public void removeDataReceiver(String instrument, StockDataReceiver<SD> dataReceiver)
	{
		if (instrument == null)
		{
			// delete from all
			for (String tmpInstrument : stockDataReceivers.keySet())
			{
				removeDataReceiver(tmpInstrument, dataReceiver);
			}
		}
		else
		{
			// single delete
			List<StockDataReceiver<SD>> receivers = stockDataReceivers.get(instrument);
			if (receivers != null)
			{
				// if registered
				if (receivers.contains(dataReceiver))
				{
					// remove it
					receivers.remove(dataReceiver);
				}
			}
		}
	}

	/**
	 * populate stock message to listeners
	 * 
	 * @param stockData
	 */
	protected void publishNewStockData(String instrument, SD stockData)
	{
		// skipp empty data
		if (stockData == null)
			return;

		// single delete
		List<StockDataReceiver<SD>> receivers = stockDataReceivers.get(instrument);
		if (receivers != null)
		{
			log.debug("New data for [" + instrument + "]:" + stockData);

			for (StockDataReceiver<SD> stockDataReceiver : receivers)
			{
				try
				{
					stockDataReceiver.newDataArrivedNotification(instrument, stockData);
					log.debug("Notified processor: [" + stockDataReceiver + "]");
				}
				catch (RuntimeException e)
				{
					log.warn("error notifying processor [" + stockDataReceiver + "] with data [" + stockData + "]", e);
				}
			}
		}
	}
}
