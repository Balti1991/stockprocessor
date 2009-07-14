/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.receiver.DataReceiver;
import stockprocessor.handler.xml.ConnectorType;
import stockprocessor.handler.xml.WireType;
import stockprocessor.handler.xml.ProcessorDocument.Processor;
import stockprocessor.handler.xml.ProcessorDocument.Processor.InnerWire;
import stockprocessor.handler.xml.ProcessorDocument.Processor.Output;
import stockprocessor.handler.xml.ProcessorDocument.Processor.UsedProcessors.ExtrnalProcessor;
import stockprocessor.manager.DefaultProcessorManager;

/**
 * @author anti
 */
public class XmlProcessor<I, O> extends AbstractDataProcessor<I, O>
{
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(XmlProcessor.class);

	private final String name;

	private String description;

	private final List<ParameterInformation> inputParameters;

	private final Map<String, DataProcessor<?, ?>> inputProcessors;

	private final List<ParameterInformation> outputParameters;

	private final Map<String, DataProcessor<?, ?>> outputProcessors;

	public XmlProcessor(Processor processor)
	{
		this.name = processor.getName();
		if (processor.isSetDescription())
			description = processor.getDescription();
		else
			description = "XML processor";

		if (log.isInfoEnabled())
		{
			log.info("Creating new processor from XML - this.name=" + this.name + ", description=" + description); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Map<String, DataProcessor<?, ?>> processors = new HashMap<String, DataProcessor<?, ?>>();

		// get used processors
		for (ExtrnalProcessor usedProcessor : processor.getUsedProcessors().getExtrnalProcessorArray())
		{
			String type = usedProcessor.getType();

			DataProcessor<?, ?> dataProcessor;
			if (usedProcessor.isSetLibrary())
			{
				String library = usedProcessor.getLibrary();
				dataProcessor = DefaultProcessorManager.INSTANCE.getInstance(library, type);

				if (log.isInfoEnabled())
				{
					log.info("Useing processor:  - type=" + type + ", library=" + library); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			else
			{
				dataProcessor = DefaultProcessorManager.INSTANCE.getInstance(type);

				if (log.isInfoEnabled())
				{
					log.info("Useing processor:  - type=" + type); //$NON-NLS-1$ 
				}
			}

			String name = usedProcessor.getName();
			processors.put(name, dataProcessor);
		}
		for (Processor usedProcessor : processor.getUsedProcessors().getProcessorArray())
		{
			DataProcessor<?, ?> dataProcessor = new XmlProcessor(usedProcessor);

			String name = usedProcessor.getName();
			processors.put(name, dataProcessor);
		}

		// get input side
		inputParameters = new ArrayList<ParameterInformation>();
		inputProcessors = new HashMap<String, DataProcessor<?, ?>>();

		for (ConnectorType connector : processor.getInputArray())
		{
			// create input information
			String name = connector.getName();
			ParameterInformation parameterInformation = new DefaultParameterInformation(name, ParameterType.STOCK_DATA);
			inputParameters.add(parameterInformation);

			// create input block
			DataProcessor<ShareData<?>, ShareData<?>> inputProcessor = new NopProcessor<ShareData<?>>();
			inputProcessors.put(name, inputProcessor);

			if (log.isInfoEnabled())
			{
				log.info("Input created on " + this.name + ", name=" + name); //$NON-NLS-1$ //$NON-NLS-2$
			}

			// wires from input
			for (WireType wire : connector.getWireArray())
			{
				DataProcessor<?, ?> dataProcessor = processors.get(wire.getProcessorName());
				inputProcessor.registerDataReceiver(null, (DataReceiver<ShareData<?>>) dataProcessor, wire.getTaget());

				if (log.isInfoEnabled())
				{
					log.info("Wire created on " + this.name + "/" + name + " to " + wire.getProcessorName() + "[" + wire.getTaget() + "]");
				}
			}
		}

		// get output side
		outputParameters = new ArrayList<ParameterInformation>();
		outputProcessors = new HashMap<String, DataProcessor<?, ?>>();

		for (Output connector : processor.getOutputArray())
		{
			String name = connector.getName();
			// create output block
			String type = connector.getType();
			DataProcessor<?, ?> outputProcessor;
			if (connector.isSetLibrary())
			{
				String library = connector.getLibrary();
				outputProcessor = DefaultProcessorManager.INSTANCE.getInstance(library, type);
			}
			else
			{
				outputProcessor = DefaultProcessorManager.INSTANCE.getInstance(type);
			}

			// create output information
			ParameterInformation parameterInformation = outputProcessor.getOutputParameters().get(0);

			// store
			outputProcessors.put(name, outputProcessor); // inner
			outputProcessors.put(parameterInformation.getDisplayName(), outputProcessor); // external
			outputParameters.add(parameterInformation);

			if (log.isInfoEnabled())
			{
				log
						.info("Output created on " + this.name + ", name=" + name + ", (alias: " + parameterInformation.getDisplayName() + ") using processor " + outputProcessor.getName()); //$NON-NLS-1$ //$NON-NLS-2$
			}

			// wires to output
			for (WireType wire : connector.getWireArray())
			{
				DataProcessor<?, ?> dataProcessor = processors.get(wire.getProcessorName());
				dataProcessor.registerDataReceiver(null, (DataReceiver) outputProcessor, wire.getTaget());

				if (log.isInfoEnabled())
				{
					log.info("Wire created on " + this.name + "/" + name + " to " + wire.getProcessorName() + "[" + wire.getTaget() + "]");
				}
			}
		}

		// inner wire
		for (InnerWire innerWire : processor.getInnerWireArray())
		{
			DataReceiver<?> receiverProcessor;
			DataProcessor<?, ?> sourceProcessor;

			// receiver
			WireType receiver = innerWire.getReceiver();
			String receiverTaget = receiver.getTaget();
			if (receiver.isSetProcessorName())
				receiverProcessor = processors.get(receiver.getProcessorName());
			else
				// xml processors output is the receiver
				receiverProcessor = outputProcessors.get(receiverTaget);

			// source
			WireType source = innerWire.getSource();
			if (source.isSetProcessorName())
				sourceProcessor = processors.get(source.getProcessorName());
			else
				// xml processors input is the source
				sourceProcessor = inputProcessors.get(source.getTaget());

			String instrument = source.getInstrument();
			sourceProcessor.registerDataReceiver(source.isSetInstrument() ? instrument : null, (DataReceiver) receiverProcessor, receiverTaget);

			if (log.isInfoEnabled())
			{
				log.info("Wire created from " + sourceProcessor.getName() + "[" + source.getTaget() + "]"
						+ (source.isSetInstrument() ? "-" + instrument : "") + " to " + receiverProcessor.getName() + "[" + receiverTaget + "]");
			}
		}

		if (log.isInfoEnabled())
		{
			log.info("Created new XML processor - this.name=" + this.name + ", description=" + description); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.AbstractDataProcessor#createInputParameters
	 * ()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		return inputParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.AbstractDataProcessor#createOutputParameters
	 * ()
	 */
	@Override
	protected List<ParameterInformation> createOutputParameters()
	{
		return outputParameters;
	}

	/*
	 * (non-Javadoc)
	 * @seestockprocessor.handler.processor.AbstractDataProcessor#
	 * createOptionalParameters()
	 */
	@Override
	protected List<ParameterInformation> createOptionalParameters()
	{
		// FIXME the inner processors...
		return new ArrayList<ParameterInformation>();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#setOptionalParameters(java
	 * .util.Map)
	 */
	@Override
	public void setOptionalParameters(Map<String, Object> optionalParameters)
	{
		// TODO
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#newDataArrivedNotification
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void newDataArrivedNotification(String input, I inputData)
	{
		DataProcessor dataProcessor = inputProcessors.get(input);
		dataProcessor.newDataArrivedNotification(input, inputData);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.AbstractDataProcessor#registerDataReceiver
	 * (java.lang.String, stockprocessor.handler.receiver.DataReceiver,
	 * java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void registerDataReceiver(String instrument, DataReceiver<O> dataReceiver, String input)
	{
		if (instrument != null)
		{
			DataProcessor dataProcessor = outputProcessors.get(instrument);
			dataProcessor.registerDataReceiver(instrument, dataReceiver, input);

			if (log.isInfoEnabled())
			{
				log
						.info("XmlProcessor [" + getName() + "] register DataReceiver [" + dataReceiver.getName() + "/" + input + "] on [" + dataProcessor.getName() + "]"); //$NON-NLS-1$
			}
		}
		else
		{
			for (ParameterInformation parameterInformation : outputParameters)
			{
				DataProcessor dataProcessor = outputProcessors.get(parameterInformation.getDisplayName());
				dataProcessor.registerDataReceiver(instrument, dataReceiver, input);

				if (log.isInfoEnabled())
				{
					log
							.info("XmlProcessor [" + getName() + "] register DataReceiver [" + dataReceiver.getName() + "/" + input + "] on [" + dataProcessor.getName() + "]"); //$NON-NLS-1$
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.AbstractDataProcessor#removeDataReceiver
	 * (java.lang.String, stockprocessor.handler.receiver.DataReceiver)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void removeDataReceiver(String instrument, DataReceiver<O> dataReceiver)
	{
		if (instrument != null)
		{
			DataProcessor dataProcessor = outputProcessors.get(instrument);
			dataProcessor.removeDataReceiver(instrument, dataReceiver);
		}
		else
		{
			for (DataProcessor dataProcessor : outputProcessors.values())
			{
				dataProcessor.removeDataReceiver(instrument, dataReceiver);
			}
		}
	}
}
