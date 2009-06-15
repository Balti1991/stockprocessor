/**
 * 
 */
package stockprocessor.processor;

import java.util.List;
import java.util.Map;

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

	public void setOptionalInputParameterInformations(Map<String, Object> optInputParameters);

	public List<ParameterInformation> getOutputParameterInformations();
}
