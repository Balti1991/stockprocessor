/**
 * 
 */
package stockprocessor.source;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author anti
 */
public abstract class ImportTimer extends Timer
{
	/**
	 * @param name timer name
	 * @param timerStep time in milliseconds between successive task executions
	 */
	public ImportTimer(final String name, final long timerStep)
	{
		super(name);

		// start timer
		schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				timeTick();
			}
		}, new Date(), timerStep);

	}

	protected abstract void timeTick();
}