package stockprocessor.handler.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.receiver.DataReceiver;

/**
 * @author anti
 */
public abstract class AbstractDataSource<O> implements DataSource<O>
{
	private static final Log log = LogFactory.getLog(AbstractDataSource.class);

	private List<ParameterInformation> outputParameters = null;

	private final Map<String, List<DataReceiver<O>>> dataReceivers = new HashMap<String, List<DataReceiver<O>>>();

	private final List<DataReceiver<O>> fullDataReceivers = new ArrayList<DataReceiver<O>>();

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.source.DataSource#getOutputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOutputParameterInformations()
	{
		if (outputParameters == null)
		{
			outputParameters = createOutputParameters();
		}

		return outputParameters;
	}

	protected abstract List<ParameterInformation> createOutputParameters();

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.StockDataSource#registerDataProcessor(hu.bogar
	 * .anti.stock.processor.StockDataProcessor)
	 */
	@Override
	public void registerDataReceiver(String instrument, DataReceiver<O> dataReceiver)
	{
		if (instrument == null)
		{
			fullDataReceivers.add(dataReceiver);
			return;
		}

		List<DataReceiver<O>> receivers = dataReceivers.get(instrument);

		if (receivers == null)
		{
			receivers = new ArrayList<DataReceiver<O>>();
			dataReceivers.put(instrument, receivers);
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
	public void removeDataReceiver(String instrument, DataReceiver<O> dataReceiver)
	{
		if (instrument == null)
		{
			// delete from all
			for (String tmpInstrument : dataReceivers.keySet())
			{
				removeDataReceiver(tmpInstrument, dataReceiver);
			}
			fullDataReceivers.remove(dataReceiver);
		}
		else
		{
			// single delete
			List<DataReceiver<O>> receivers = dataReceivers.get(instrument);
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
	 * @param data
	 */
	protected void publishNewData(String instrument, O data)
	{
		// skipp empty data
		if (data == null)
			return;

		notifyReceivers(fullDataReceivers, instrument, data);
		notifyReceivers(dataReceivers.get(instrument), instrument, data);
	}

	/**
	 * @param receivers
	 * @param instrument
	 * @param data
	 */
	private void notifyReceivers(List<DataReceiver<O>> receivers, String instrument, O data)
	{
		if (receivers != null)
		{
			log.debug("New data for [" + instrument + "]:" + data);

			for (DataReceiver<O> stockDataReceiver : receivers)
			{
				try
				{
					stockDataReceiver.newDataArrivedNotification(instrument, data);
					log.debug("Notified processor: [" + stockDataReceiver + "]");
				}
				catch (RuntimeException e)
				{
					log.warn("error notifying processor [" + stockDataReceiver + "] with data [" + data + "]", e);
				}
			}
		}
	}
}
