/**
 * 
 */
package stockprocessor.source;

import stockprocessor.data.StockData;
import stockprocessor.processor.StockDataReceiver;

/**
 * @author anti
 */
public interface StockDataSource<SD extends StockData<?>>
{
	/**
	 * register a stock data processor to the source for future notifications
	 * 
	 * @param instrument the instrument code
	 * @param dataReceiver the data processor
	 */
	public void registerDataReceiver(String instrument, StockDataReceiver<SD> dataReceiver);

	/**
	 * unregister a data processor from future notification
	 * 
	 * @param instrument the instrument code, if null deletes from all
	 *            instruments list
	 * @param dataReceiver
	 */
	public void removeDataReceiver(String instrument, StockDataReceiver<SD> dataReceiver);

	// //////////////////////////////////
	// manage historical data
	// /////////////////////////////////

	// /**
	// * get the number of available historical datas if any
	// *
	// * @return the number of available historical data
	// */
	// public long getAvailableHistoricalData();
	//
	// /**
	// * get the earliest date of available datas
	// *
	// * @return
	// */
	// public Date getFirstHistoricalDataTime();
	//
	// public SD[] getHistoricalData(Date startDate);
	//
	// public SD[] getHistoricalData(long previouseData);
	//
	// /**
	// * register a stock data processor to the source for future notifications
	// * and request historical data for preparing/preprocessing
	// *
	// * @param dataProcessor the dataprocessot
	// * @param previouseData number of requested historical data
	// */
	// @Deprecated
	// public void registerDataProcessor(StockDataProcessor<SD> dataProcessor,
	// long previouseData);
	//
	// /**
	// * register a stock data processor to the source for future notifications
	// * and request historical data before date for preparing/preprocessing
	// *
	// * @param dataProcessor the dataprocessot
	// * @param startTime starting date of requested historical data
	// */
	// @Deprecated
	// public void registerDataProcessor(StockDataProcessor<SD> dataProcessor,
	// Date startDate);

	// //////////////////////////////////
	// common
	// //////////////////////////////////

	public String getName();

	public String[] getAvailableInstruments();
}
