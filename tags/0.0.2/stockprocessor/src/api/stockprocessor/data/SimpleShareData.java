/**
 * 
 */
package stockprocessor.data;

import java.util.Date;

/**
 * @author anti
 */
public class SimpleShareData extends ShareData<Integer>
{

	/**
	 * @param name
	 * @param value
	 * @param volume
	 * @param timeStamp
	 */
	public SimpleShareData(String name, Integer value, long volume, Date timeStamp)
	{
		super(name, value, volume, timeStamp);
		// TODO Auto-generated constructor stub
	}
}
