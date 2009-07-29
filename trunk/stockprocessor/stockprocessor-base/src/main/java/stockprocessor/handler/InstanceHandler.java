/**
 * 
 */
package stockprocessor.handler;

/**
 * @author anti
 */
public interface InstanceHandler<T>
{
	public String getName();

	public T getInstance();
}
