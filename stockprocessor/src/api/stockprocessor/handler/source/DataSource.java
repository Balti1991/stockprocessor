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
	public List<ParameterInformation> getOutputParameterInformations();

	/**
	 * register a stock data processor to the source for future notifications
	 * 
	 * @param instrument the instrument code
	 * @param dataReceiver the data processor
	 */
	public void registerDataReceiver(String instrument, DataReceiver<O> dataReceiver);

	/**
	 * unregister a data processor from future notification
	 * 
	 * @param instrument the instrument code, if null deletes from all
	 *            instruments list
	 * @param dataReceiver
	 */
	public void removeDataReceiver(String instrument, DataReceiver<O> dataReceiver);
}
