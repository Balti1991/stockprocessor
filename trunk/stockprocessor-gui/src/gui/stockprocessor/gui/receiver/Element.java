/**
 * 
 */
package stockprocessor.gui.receiver;

import org.jfree.chart.plot.XYPlot;

/**
 * @author anti
 */
public interface Element
{
	/**
	 * @param plot
	 */
	public void setPlot(XYPlot plot);

	// /**
	// * @return the datasetTime
	// */
	// public abstract AbstractXYDataset getDataset();
	//
	// /**
	// * @return the datasetTime
	// */
	// public abstract XYItemRenderer getRenderer();

	// /**
	// * @return the datasetOHLC
	// */
	// public abstract OHLCSeriesCollection getDatasetOHLC();
	//
	// /**
	// * @return the datasetTime
	// */
	// public abstract TimeSeriesCollection getDatasetTime();
}