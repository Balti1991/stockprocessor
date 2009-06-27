/**
 * 
 */
package stockprocessor.gui.handler.receiver;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import stockprocessor.data.Candle;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.processor.AbstractDataProcessor;

/**
 * @author anti
 */
public class CandleElement extends BaseElement
{
	private final OHLCSeriesCollection datasetOHLC = new OHLCSeriesCollection();

	/**
	 * @param instrument
	 */
	public CandleElement(String instrument)
	{
		super(instrument);

		datasetOHLC.addSeries(new OHLCSeries(getName()));
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.AbstractDataReceiver#createInputParameters()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = AbstractDataProcessor.createParameterInformation("Input", ParameterType.STOCK_DATA_CANDLE);
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

		if (value instanceof Candle)
		{
			Candle candle = (Candle) value;

			try
			{
				datasetOHLC.getSeries(0).add(getTimePeriod(stockData), candle.getOpen(), candle.getMax(), candle.getMin(), candle.getClose());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("CandleElement: unknown stock value [" + value + "]");
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
		plot.setRenderer(new CandlestickRenderer());
		plot.setDataset(datasetOHLC);
	}
}
