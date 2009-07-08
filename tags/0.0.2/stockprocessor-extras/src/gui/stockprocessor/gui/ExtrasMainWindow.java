/**
 * 
 */
package stockprocessor.gui;

import stockprocessor.handler.processor.TAProcessorManager;
import stockprocessor.handler.source.EbrokerHtmlStockDataSource;
import stockprocessor.handler.source.PortfolioStockDataSource;
import stockprocessor.manager.DefaultProcessorManager;
import stockprocessor.manager.DefaultSourceManager;

/**
 * @author anti
 */
public class ExtrasMainWindow extends MainWindow
{
	private static final long serialVersionUID = -4442683607418806440L;

	public ExtrasMainWindow()
	{
		super();

		DefaultProcessorManager.INSTANCE.registerProcessorManager(new TAProcessorManager()); // TA-lib

		DefaultSourceManager.INSTANCE.registerInstanceHandler(EbrokerHtmlStockDataSource.instanceHandler); // ebroker
		DefaultSourceManager.INSTANCE.registerInstanceHandler(PortfolioStockDataSource.instanceHandler); // portfolio
	}

	public static void main(String[] args)
	{
		new ExtrasMainWindow().setVisible(true);
	}
}
