/**
 * 
 */
package stockprocessor.data;

/**
 * @author anti
 */
public class Candle
{
	private final int open;

	private final int close;

	private final int min;

	private final int max;

	/**
	 * @param open
	 * @param close
	 * @param min
	 * @param max
	 */
	public Candle(final int open, final int close, final int min, final int max)
	{
		this.open = open;
		this.close = close;
		this.min = min;
		this.max = max;
	}

	public int getOpen()
	{
		return open;
	}

	public int getClose()
	{
		return close;
	}

	public int getMin()
	{
		return min;
	}

	public int getMax()
	{
		return max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Candle[ open=" + open + ", close=" + close + ", min=" + min + ", max= " + max + "]";
	}
}
