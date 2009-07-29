/**
 * 
 */
package stockprocessor.handler.source;

import java.util.List;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.DataHandler;
import stockprocessor.handler.receiver.DataReceiver;

/**
 * @author anti
 */
public interface DataSource<O> extends DataHandler
{
	/**
	 * the list of output parameters descriptions
	 * 
	 * @return
	 */
	public List<ParameterInformation> getOutputParameters();

	/**
	 * register a stock data processor to the source for future notifications
	 * 
	 * @param dataReceiver the data input
	 * @param input the data receivers input
	 */
	public void registerDataReceiver(String output, DataReceiver<O> dataReceiver, String input);

	/**
	 * unregister a data processor from future notification
	 * 
	 * @param dataReceiver the data input
	 * @param input the data receivers input
	 */
	public void removeDataReceiver(String output, DataReceiver<O> dataReceiver, String input);
}
