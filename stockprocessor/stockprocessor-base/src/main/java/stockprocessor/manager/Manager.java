/**
 * 
 */
package stockprocessor.manager;

import java.util.List;

import stockprocessor.handler.InstanceHandler;

/**
 * @author anti
 */
public interface Manager<T>
{
	public String getName();

	public T getInstance(String name);

	public void registerInstanceHandler(InstanceHandler<T> instanceHandler);

	public List<String> getAvailableInstances();
}