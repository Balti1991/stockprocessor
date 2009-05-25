/**
 * 
 */
package stockprocessor.gui.processor;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

/**
 * @author anti
 */
public interface Element
{
	/**
	 * @return the datasetOHLC
	 */
	public abstract OHLCSeriesCollection getDatasetOHLC();

	/**
	 * @return the datasetTime
	 */
	public abstract TimeSeriesCollection getDatasetTime();

}