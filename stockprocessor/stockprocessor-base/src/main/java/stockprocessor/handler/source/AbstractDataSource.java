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

	/**
	 * the map of outputs to data receivers lists
	 */
	private final Map<String, List<Pair<DataReceiver<O>, String>>> dataReceivers = new HashMap<String, List<Pair<DataReceiver<O>, String>>>();

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
	public void registerDataReceiver(String output, DataReceiver<O> dataReceiver, String input)
	{
		Pair<DataReceiver<O>, String> pair = new Pair<DataReceiver<O>, String>(dataReceiver, input);

		List<Pair<DataReceiver<O>, String>> receivers = dataReceivers.get(output);

		if (receivers == null)
		{
			receivers = new ArrayList<Pair<DataReceiver<O>, String>>();
			dataReceivers.put(output, receivers);
		}

		// if not yet on list
		if (!receivers.contains(pair))
		{
			// add it
			receivers.add(pair);

			if (log.isDebugEnabled())
			{
				log.debug("Registered on [" + getName() + "] to [" + output + "] receiver list the data receiver: [" + dataReceiver.getName() + "/"
						+ input + "]");
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
	public void removeDataReceiver(String output, DataReceiver<O> dataReceiver, String input)
	{
		// single delete
		List<Pair<DataReceiver<O>, String>> receivers = dataReceivers.get(output);
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

	/**
	 * populate stock message to listeners
	 * 
	 * @param data
	 */
	protected void publishNewData(String output, O data)
	{
		log.debug("New [" + output + "] data from [" + getName() + "]: " + data);

		// skip empty data
		if (data == null)
			return;

		List<Pair<DataReceiver<O>, String>> receivers = dataReceivers.get(output);
		// skip if no receivers on output
		if (receivers == null)
			return;

		log.debug("Source [" + getName() + "] start to broadcust data: [" + data + "]");

		for (Pair<DataReceiver<O>, String> pair : receivers)
		{
			try
			{
				log.debug("Source [" + getName() + "]: Notifying processor: [" + pair.getFirst() + "] on input: [" + pair.getSecond() + "]");
				pair.getFirst().newDataArrivedNotification(pair.getSecond(), data);
				log.debug("Source [" + getName() + "]: Notified processor: [" + pair.getFirst() + "] on input: [" + pair.getSecond() + "]");
			}
			catch (RuntimeException e)
			{
				log.warn("Source [" + getName() + "]: error notifying processor [" + pair.getFirst() + "] on input: [" + pair.getSecond()
						+ "] with data [" + data + "]", e);
			}
		}

		log.debug("Receivers notifyed on source [" + getName() + "]");
	}

	/**
	 * @param receivers
	 * @param instrument
	 * @param data
	 */
	private void notifyReceivers(List<Pair<DataReceiver<O>, String>> receivers, O data)
	{
	}
}
