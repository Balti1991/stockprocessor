/**
 * 
 */
package stockprocessor.gui;

import stockprocessor.processor.TAProcessorManager;
import stockprocessor.source.EbrokerHtmlStockDataSource;
import stockprocessor.source.PortfolioStockDataSource;

/**
 * @author anti
 */
public class ExtrasMainWindow extends MainWindow
{
	public ExtrasMainWindow()
	{
		super();

		// customization
		processorManager.registerProcessorManager(new TAProcessorManager());

		sourceManager.registerStockDataSource(new EbrokerHtmlStockDataSource()); // ebroker
		sourceManager.registerStockDataSource(new PortfolioStockDataSource()); // portfolio
	}

	public static void main(String[] args)
	{
		new ExtrasMainWindow().setVisible(true);
	}
}
