/**
 * 
 */
package stockprocessor.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anti
 */
public abstract class AbstractManager<T> implements Manager<T>
{
	private final Map<String, InstanceHandler<T>> instanceHandlers = new HashMap<String, InstanceHandler<T>>();

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.manager.Manager#getAvailableInstances()
	 */
	@Override
	public List<String> getAvailableInstances()
	{
		return new ArrayList<String>(instanceHandlers.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.manager.Manager#getInstance(java.lang.String)
	 */
	@Override
	public T getInstance(String name)
	{
		InstanceHandler<T> instanceHandler = instanceHandlers.get(name);
		return instanceHandler == null ? null : instanceHandler.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.manager.Manager#registerInstanceHandler(stockprocessor
	 * .manager.InstanceHandler)
	 */
	@Override
	public void registerInstanceHandler(InstanceHandler<T> instanceHandler)
	{
		instanceHandlers.put(instanceHandler.getName(), instanceHandler);
	}

}
