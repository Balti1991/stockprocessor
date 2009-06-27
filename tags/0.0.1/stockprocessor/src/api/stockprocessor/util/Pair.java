/**
 * 
 */
package stockprocessor.util;

/**
 * @author anti
 */
public class Pair<A, B>
{
	private A first;

	private B second;

	/**
	 * @param first
	 * @param second
	 */
	public Pair(A first, B second)
	{
		super();
		this.setFirst(first);
		this.setSecond(second);
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(A first)
	{
		this.first = first;
	}

	/**
	 * @return the first
	 */
	public A getFirst()
	{
		return first;
	}

	/**
	 * @param second the second to set
	 */
	public void setSecond(B second)
	{
		this.second = second;
	}

	/**
	 * @return the second
	 */
	public B getSecond()
	{
		return second;
	}
}
