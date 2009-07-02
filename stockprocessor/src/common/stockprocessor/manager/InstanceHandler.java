/**
 * 
 */
package stockprocessor.manager;

/**
 * @author anti
 */
public interface InstanceHandler<T>
{
	public String getName();

	public T getInstance();
}
