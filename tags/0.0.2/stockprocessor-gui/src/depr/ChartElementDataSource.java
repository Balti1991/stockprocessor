/**
 * 
 */
package stockprocessor.stock.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stockprocessor.data.StockData;
import stockprocessor.data.handler.DataReceiver;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.gui.processor.BaseElement;
import stockprocessor.gui.processor.ChartElement;
import stockprocessor.handler.AbstractDataProcessor;
import stockprocessor.source.AbstractDataSource;

/**
 * @author anti
 */
public class ChartElementDataSource extends AbstractDataSource<StockData<?>>
{
	public static final String CHART_ELEMENTS = "Chart elements";

	private final Map<String, ChartElement> elementMap = new HashMap<String, ChartElement>();

	private final List<String> elementNameList = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getAvailableInstruments()
	 */
	@Override
	public String[] getAvailableInstruments()
	{
		return elementNameList.toArray(new String[elementNameList.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getName()
	 */
	@Override
	public String getName()
	{
		return "Chart elements";
	}

	public void registerElement(ChartElement element)
	{
		String name = element.getName();
		elementMap.put(name, element);
		elementNameList.add(name);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractStockDataSource#registerDataReceiver
	 * (java.lang.String, hu.bogar.anti.stock.processor.StockDataReceiver)
	 */
	@Override
	public void registerDataReceiver(String instrument, DataReceiver<StockData<?>> dataReceiver)
	{
		BaseElement element = elementMap.get(instrument);
		element.registerDataReceiver(instrument, dataReceiver);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractStockDataSource#removeDataReceiver
	 * (java.lang.String, hu.bogar.anti.stock.processor.StockDataReceiver)
	 */
	@Override
	public void removeDataReceiver(String instrument, DataReceiver<StockData<?>> dataReceiver)
	{
		BaseElement element = elementMap.get(instrument);
		element.removeDataReceiver(instrument, dataReceiver);
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.source.AbstractDataSource#createOutputParameters()
	 */
	@Override
	protected List<ParameterInformation> createOutputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = AbstractDataProcessor.createParameterInformation("Output", ParameterType.STOCK_DATA);
		list.add(parameterInformation);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.data.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return "Reuse existing elemnts from the charts";
	}
}
