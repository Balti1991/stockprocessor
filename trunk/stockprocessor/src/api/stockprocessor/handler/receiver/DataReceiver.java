/**
 * 
 */
package stockprocessor.handler.receiver;

import java.util.List;
import java.util.Map;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.DataHandler;

/**
 * @author anti
 */
public interface DataReceiver<I> extends DataHandler
{
	/**
	 * the list of input parameters descriptions
	 * 
	 * @return
	 */
	public List<ParameterInformation> getInputParameters();

	/**
	 * the list of optional parameters descriptions
	 * 
	 * @return
	 */
	public List<ParameterInformation> getOptionalParameters();

	/**
	 * point to calibrate optional input parameters
	 * 
	 * @param optInputParameters the map of changeable parameters
	 */
	public void setOptionalParameters(Map<String, Object> optionalParameters);

	/**
	 * method where the receiver get the data
	 * 
	 * @param input the instruments name
	 * @param inputData the data
	 */
	public void newDataArrivedNotification(String input, I inputData);
}
