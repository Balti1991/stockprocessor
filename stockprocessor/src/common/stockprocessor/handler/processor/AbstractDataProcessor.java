/**
 * 
 */
package stockprocessor.handler.processor;

import java.util.List;
import java.util.Map;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.receiver.AbstractDataReceiver;
import stockprocessor.handler.receiver.DataReceiver;
import stockprocessor.handler.source.AbstractDataSource;

/**
 * @author anti
 */
public abstract class AbstractDataProcessor<I, O> implements DataProcessor<I, O>
{
	private abstract class ProcessorDataSource extends AbstractDataSource<O>
	{
		/*
		 * (non-Javadoc)
		 * @see
		 * stockprocessor.source.AbstractDataSource#publishNewData(java.lang
		 * .String, java.lang.Object)
		 */
		@Override
		public void publishNewData(String instrument, O data)
		{
			super.publishNewData(instrument, data);
		}
	}

	protected ProcessorDataSource dataSource = new ProcessorDataSource()
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

	protected AbstractDataReceiver<I> dataReceiver = new AbstractDataReceiver<I>()
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
		public void newDataArrivedNotification(String instrument, I inputData)
		{
			AbstractDataProcessor.this.newDataArrivedNotification(instrument, inputData);
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

	protected abstract List<ParameterInformation> createInputParameters();

	protected abstract List<ParameterInformation> createOptionalParameters();

	protected abstract List<ParameterInformation> createOutputParameters();

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
	public void registerDataReceiver(String instrument, DataReceiver<O> dataReceiver, String input)
	{
		dataSource.registerDataReceiver(instrument, dataReceiver, input);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataSource#removeDataReceiver(java.lang.String
	 * , stockprocessor.data.handler.DataReceiver)
	 */
	@Override
	public void removeDataReceiver(String instrument, DataReceiver<O> dataReceiver)
	{
		dataSource.removeDataReceiver(instrument, dataReceiver);
	}

	protected void publishNewData(String instrument, O data)
	{
		dataSource.publishNewData(instrument, data);
	}

	public static ParameterInformation createParameterInformation(final String name, final ParameterType parameterType)
	{
		return new ParameterInformation()
		{
			@Override
			public String getDisplayName()
			{
				return name;
			}

			@Override
			public ParameterType getType()
			{
				return parameterType;
			}
		};
	}
}
