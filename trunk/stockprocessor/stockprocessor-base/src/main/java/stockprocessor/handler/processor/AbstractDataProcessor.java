/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.List;
import java.util.Map;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.receiver.AbstractDataReceiver;
import stockprocessor.handler.receiver.DataReceiver;
import stockprocessor.handler.source.AbstractDataSource;

/**
 * @author anti
 */
public abstract class AbstractDataProcessor<I, O> implements DataProcessor<I, O>
{
	protected abstract class ProcessorDataSource extends AbstractDataSource<O>
	{
		/*
		 * (non-Javadoc)
		 * @see
		 * stockprocessor.source.AbstractDataSource#publishNewData(java.lang
		 * .String, java.lang.Object)
		 */
		@Override
		public void publishNewData(String output, O data)
		{
			super.publishNewData(output, data);
		}
	}

	private String name;

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.DataProcessor#setName(java.lang.String)
	 */
	@Override
	public void setName(String name)
	{
		this.name = name;
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

	protected ProcessorDataSource dataSource;

	protected DataReceiver<I> dataReceiver;

	/**
	 * 
	 */
	public AbstractDataProcessor()
	{
		dataSource = getProcessorDataSource();
		dataReceiver = getDataReceiver();
	}

	protected DataReceiver<I> getDataReceiver()
	{
		return new AbstractDataReceiver<I>()
		{
			@Override
			protected List<ParameterInformation> createInputParameters()
			{
				return AbstractDataProcessor.this.createInputParameters();
			}

			@Override
			protected List<ParameterInformation> createOptionalParameters()
			{
				return AbstractDataProcessor.this.createOptionalParameters();
			}

			@Override
			public void newDataArrivedNotification(String output, I inputData)
			{
				AbstractDataProcessor.this.newDataArrivedNotification(output, inputData);
			}

			@Override
			public void setOptionalParameters(Map<String, Object> optionalParameters)
			{
				// NOP
			}

			@Override
			public String getDescription()
			{
				return AbstractDataProcessor.this.getDescription();
			}

			@Override
			public String getName()
			{
				return AbstractDataProcessor.this.getName();
			}
		};
	}

	/**
	 * @return
	 */
	protected abstract List<ParameterInformation> createInputParameters();

	/**
	 * @return
	 */
	protected abstract List<ParameterInformation> createOptionalParameters();

	// -------------------------------------------------
	// Data Receiver methods
	// -------------------------------------------------

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataReceiver#getInputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getInputParameters()
	{
		return dataReceiver.getInputParameters();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataReceiver#getOptionalParameterInformations
	 * ()
	 */
	@Override
	public List<ParameterInformation> getOptionalParameters()
	{
		return dataReceiver.getOptionalParameters();
	}

	// -------------------------------------------------
	// Data Source methods
	// -------------------------------------------------

	protected ProcessorDataSource getProcessorDataSource()
	{
		return new ProcessorDataSource()
		{
			@Override
			protected List<ParameterInformation> createOutputParameters()
			{
				return AbstractDataProcessor.this.createOutputParameters();
			}

			@Override
			public String getDescription()
			{
				return AbstractDataProcessor.this.getDescription();
			}

			@Override
			public String getName()
			{
				return AbstractDataProcessor.this.getName();
			}
		};
	}

	/**
	 * @return
	 */
	protected abstract List<ParameterInformation> createOutputParameters();

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataSource#getOutputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOutputParameters()
	{
		return dataSource.getOutputParameters();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataSource#registerDataReceiver(java.lang
	 * .String, stockprocessor.data.handler.DataReceiver)
	 */
	@Override
	public void registerDataReceiver(String output, DataReceiver<O> dataReceiver, String input)
	{
		dataSource.registerDataReceiver(output, dataReceiver, input);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataSource#removeDataReceiver(java.lang.String
	 * , stockprocessor.data.handler.DataReceiver)
	 */
	@Override
	public void removeDataReceiver(String output, DataReceiver<O> dataReceiver, String input)
	{
		dataSource.removeDataReceiver(output, dataReceiver, input);
	}

	protected void publishNewData(String output, O data)
	{
		dataSource.publishNewData(output, data);
	}
}
