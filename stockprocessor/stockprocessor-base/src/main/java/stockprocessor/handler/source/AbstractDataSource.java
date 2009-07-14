package stockprocessor.handler.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.receiver.DataReceiver;
import stockprocessor.util.Pair;

/**
 * @author anti
 */
public abstract class AbstractDataSource<O> implements DataSource<O>
{
	private static final Log log = LogFactory.getLog(AbstractDataSource.class);

	private List<ParameterInformation> outputParameters = null;

	private final Map<String, List<Pair<DataReceiver<O>, String>>> dataReceivers = new HashMap<String, List<Pair<DataReceiver<O>, String>>>();

	private final List<Pair<DataReceiver<O>, String>> fullDataReceivers = new ArrayList<Pair<DataReceiver<O>, String>>();

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.source.DataSource#getOutputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOutputParameters()
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
	public void registerDataReceiver(String instrument, DataReceiver<O> dataReceiver, String input)
	{
		Pair<DataReceiver<O>, String> pair = new Pair<DataReceiver<O>, String>(dataReceiver, input);
		if (instrument == null)
		{
			fullDataReceivers.add(pair);

			if (log.isDebugEnabled())
			{
				log
						.debug("Registered on [" + getName() + "] to full receiver list the data receiver: [" + dataReceiver.getName() + "/" + input
								+ "]");
			}

			return;
		}

		List<Pair<DataReceiver<O>, String>> receivers = dataReceivers.get(instrument);

		if (receivers == null)
		{
			receivers = new ArrayList<Pair<DataReceiver<O>, String>>();
			dataReceivers.put(instrument, receivers);
		}

		// if not yet on list
		if (!receivers.contains(pair))
		{
			// add it
			receivers.add(pair);

			if (log.isDebugEnabled())
			{
				log.debug("Registered on [" + getName() + "] to [" + instrument + "] receiver list the data receiver: [" + dataReceiver.getName()
						+ "/" + input + "]");
			}
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
		// FIXME: remove the pair!!!
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
			List<Pair<DataReceiver<O>, String>> receivers = dataReceivers.get(instrument);
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

		log.debug("New data from [" + instrument + "] on [" + getName() + "]:" + data);

		notifyReceivers(fullDataReceivers, data);
		notifyReceivers(dataReceivers.get(instrument), data);
	}

	/**
	 * @param receivers
	 * @param instrument
	 * @param data
	 */
	private void notifyReceivers(List<Pair<DataReceiver<O>, String>> receivers, O data)
	{
		if (receivers != null)
		{
			log.debug("Source [" + getName() + "] start to broadcust data: [" + data + "]");
			for (Pair<DataReceiver<O>, String> pair : receivers)
			{
				try
				{
					pair.getFirst().newDataArrivedNotification(pair.getSecond(), data);
					log.debug("Notified processor: [" + pair.getFirst() + "] on input: [" + pair.getSecond() + "]");
				}
				catch (RuntimeException e)
				{
					log.warn("error notifying processor [" + pair.getFirst() + "] on input: [" + pair.getSecond() + "] with data [" + data + "]", e);
				}
			}

			log.debug("Receivers notifyed on source [" + getName() + "]");
		}
	}
}
