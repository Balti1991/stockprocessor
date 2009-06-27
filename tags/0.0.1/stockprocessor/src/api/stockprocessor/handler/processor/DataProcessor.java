/**
 * 
 */
package stockprocessor.handler.processor;

import stockprocessor.handler.receiver.DataReceiver;
import stockprocessor.handler.source.DataSource;

/**
 * @author anti
 */
public interface DataProcessor<I, O> extends DataReceiver<I>, DataSource<O>
{
}
