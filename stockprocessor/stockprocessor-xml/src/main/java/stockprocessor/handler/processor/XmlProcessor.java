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
import stockprocessor.handler.xml.WireType;
import stockprocessor.handler.xml.ProcessorDocument.Processor;
import stockprocessor.handler.xml.ProcessorDocument.Processor.InnerWire;
import stockprocessor.handler.xml.ProcessorDocument.Processor.Input;
import stockprocessor.handler.xml.ProcessorDocument.Processor.Output;
import stockprocessor.handler.xml.ProcessorDocument.Processor.Output.Type.Enum;
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
			dataProcessor.setName(name);
			processors.put(name, dataProcessor);
		}
		for (Processor usedProcessor : processor.getUsedProcessors().getProcessorArray())
		{
			DataProcessor<?, ?> dataProcessor = new XmlProcessor(usedProcessor);

			String name = usedProcessor.getName();
			dataProcessor.setName(name);
			processors.put(name, dataProcessor);
		}

		// get input side
		inputParameters = new ArrayList<ParameterInformation>();
		inputProcessors = new HashMap<String, DataProcessor<?, ?>>();

		for (Input connector : processor.getInputArray())
		{
			// create input information
			String name = connector.getName();
			ParameterInformation parameterInformation = new DefaultParameterInformation(name, ParameterType.STOCK_DATA);
			inputParameters.add(parameterInformation);

			// create input block
			DataProcessor<ShareData<?>, ShareData<?>> inputProcessor = new NopProcessor<ShareData<?>>();
			inputProcessor.setName(name);
			inputProcessors.put(name, inputProcessor);

			if (log.isInfoEnabled())
			{
				log.info("Input created on " + this.name + ", name=" + name); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		// get output side
		outputParameters = new ArrayList<ParameterInformation>();
		outputProcessors = new HashMap<String, DataProcessor<?, ?>>();

		for (Output connector : processor.getOutputArray())
		{
			String name = connector.getName();
			// create output block
			Enum type = connector.getType();
			DataProcessor<?, ?> outputProcessor = new NopProcessor<ShareData<?>>();

			// create output information
			ParameterInformation parameterInformation; // =
			// outputProcessor.getOutputParameters().get(0);
			parameterInformation = new DefaultParameterInformation(name, decodeParameterType(type));

			// store
			outputProcessor.setName(name);
			outputProcessors.put(name, outputProcessor); // inner
			outputParameters.add(parameterInformation);

			if (log.isInfoEnabled())
			{
				log.info("Output created on " + this.name + ", name=" + name); //$NON-NLS-1$ //$NON-NLS-2$
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
			{
				// xml processors output is the receiver
				receiverProcessor = outputProcessors.get(receiverTaget);
				receiverTaget = "Input";
			}

			// source
			WireType source = innerWire.getSource();
			String sourceTaget = source.getTaget();
			if (source.isSetProcessorName())
				sourceProcessor = processors.get(source.getProcessorName());
			else
			{
				sourceProcessor = inputProcessors.get(sourceTaget);
				sourceTaget = "Output";
			}

			sourceProcessor.registerDataReceiver(sourceTaget, (DataReceiver) receiverProcessor, receiverTaget);

			if (log.isInfoEnabled())
			{
				log.info("Wire created from " + sourceProcessor.getName() + "[" + sourceTaget + "] to " + receiverProcessor.getName() + "["
						+ receiverTaget + "]");
			}
		}

		if (log.isInfoEnabled())
		{
			log.info("Created new XML processor - this.name=" + this.name + ", description=" + description); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private ParameterType decodeParameterType(Enum type)
	{
		if (Output.Type.STOCK_ACTION.equals(type))
			return ParameterType.STOCK_ACTION;
		if (Output.Type.STOCK_CANDLE.equals(type))
			return ParameterType.STOCK_DATA_CANDLE;
		if (Output.Type.STOCK_NUMBER.equals(type))
			return ParameterType.STOCK_DATA_NUMBER;

		throw new RuntimeException("Unkonwn parameter type: [" + type + "]");
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
	public void registerDataReceiver(String output, DataReceiver<O> dataReceiver, String input)
	{
		for (ParameterInformation parameterInformation : outputParameters)
		{
			DataProcessor dataProcessor = outputProcessors.get(parameterInformation.getDisplayName());
			dataProcessor.registerDataReceiver(NopProcessor.OUTPUT, dataReceiver, input);

			if (log.isInfoEnabled())
			{
				log
						.info("XmlProcessor [" + getName() + "] register DataReceiver [" + dataReceiver.getName() + "/" + input + "] on [" + dataProcessor.getName() + "]"); //$NON-NLS-1$
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
	public void removeDataReceiver(String output, DataReceiver<O> dataReceiver, String input)
	{
		for (DataProcessor dataProcessor : outputProcessors.values())
		{
			dataProcessor.removeDataReceiver(output, dataReceiver, input);
		}
	}
}
