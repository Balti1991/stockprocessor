/**
 * 
 */
package stockprocessor.gui.handler.receiver;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public class TimeElement extends BaseElement
{
	private final TimeSeriesCollection datasetTime = new TimeSeriesCollection();

	/**
	 * @param instrument
	 */
	public TimeElement(String instrument)
	{
		super(instrument);

		datasetTime.addSeries(new TimeSeries(getName(), Second.class));
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.AbstractDataReceiver#createInputParameters()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = new DefaultParameterInformation("Input", ParameterType.STOCK_DATA_NUMBER);
		list.add(parameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#newDataArrivedNotification
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void newDataArrivedNotification(String input, ShareData<?> stockData)
	{
		Object value = stockData.getValue();

		if (value instanceof Number)
		{
			try
			{
				datasetTime.getSeries(0).add(getTimePeriod(stockData), ((Number) value));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("TimeElement: unknown stock value [" + value + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.gui.receiver.Element#setPlot(org.jfree.chart.plot.XYPlot)
	 */
	@Override
	public void setPlot(XYPlot plot)
	{
		int index = getFreeRendererPosition(plot);
		plot.setRenderer(index, new XYLineAndShapeRenderer(true, false));
		plot.setDataset(index, datasetTime);
	}
}
