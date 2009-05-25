/**
 * 
 */
package stockprocessor.processor;


import java.util.List;

import stockprocessor.data.StockData;
import stockprocessor.data.information.ParameterInformation;

/**
 * @author anti
 */
public interface StockDataProcessor<SD extends StockData<?>> extends StockDataReceiver<SD>
{
	/************************
	 * informations
	 ************************/
	public String getDescription();

	public List<ParameterInformation> getInputParameterInformations();

	public List<ParameterInformation> getOptionalInputParameterInformations();

	public List<ParameterInformation> getOutputParameterInformations();
}
