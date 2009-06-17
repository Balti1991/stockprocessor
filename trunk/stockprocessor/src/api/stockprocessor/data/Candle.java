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

	private int close;

	private int min;

	private int max;

	/**
	 * @param open
	 * @param close
	 * @param min
	 * @param max
	 */
	public Candle(final int open)
	{
		this.open = open;
		this.close = open;
		this.min = open;
		this.max = open;
	}

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

	/**
	 * moves the closing value of candle
	 * 
	 * @param value
	 */
	public void addValue(Integer value)
	{
		// set max
		if (max < value)
			max = value;

		// set min
		if (min > value)
			min = value;

		// set close
		close = value;
	}

	public void addValue(Candle value)
	{
		addValue(value.getOpen());
		addValue(value.getMin());
		addValue(value.getMax());
		addValue(value.getClose());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Candle[ open=" + open + ", close=" + close + ", min=" + min + ", max= " + max + "]";
	}
}
