/**
 * 
 */
package stockprocessor.cli;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.meta.CoreMetaData2;

/**
 * @author anti
 */
public class StocCLI
{
	/**
	 * Log object for this class.
	 */
	private static final Log log = LogFactory.getLog(StocCLI.class);

	/**
	 * Default encoding.
	 */
	private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Parse command line
		final CommandLineParser commandLineParser = new PosixParser();
		Options options = new Options();
		options.addOption("h", "help", false, "print this message");
		options.addOption("e", "encoding", true, "file encoding");
		options.addOption("c", "config", true, "configuration file");

		CommandLine commandLine = null;
		try
		{
			commandLine = commandLineParser.parse(options, args);
		}
		catch (ParseException e)
		{
			log.error("Parsing failed", e);
			System.exit(1);
		}

		if (commandLine.hasOption("h") || commandLine.getArgList().size() != 1)
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("[options] source_path", options);
			System.exit(commandLine.hasOption("h") ? 0 : 1);
		}

		// Extract values
		final boolean recursive = commandLine.hasOption('r');
		final String encoding = commandLine.getOptionValue('e', DEFAULT_CHARACTER_ENCODING);

		String cliConfigFile = (String) commandLine.getArgList().get(0);
		String pathStr = (String) commandLine.getArgList().get(1);
	}

	private double[] getFunction(String line, double[] inArray)
	{
		String[] strings = line.split(",");

		CoreMetaData2 instance;
		try
		{
			// get instance
			instance = CoreMetaData2.getInstance(strings[0]);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException("Imstance creation error", e);
		}

		int lookback;
		try
		{
			// get lookback
			lookback = instance.getLookback();
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException("Look back error", e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException("Look back error", e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException("Look back error", e);
		}

		// set input
		instance.setInputParamReal(0, inArray);

		// set output
		double[] outArray = new double[inArray.length - lookback];
		instance.setOutputParamReal(0, outArray);

		// prepare out parameters
		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;
		MInteger outNbElement = new MInteger();
		outNbElement.value = outArray.length;

		try
		{
			// call function
			instance.callFunc(lookback, inArray.length - 1, outBegIdx, outNbElement);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException("Call function error", e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException("Call function error", e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException("Call function error", e);
		}

		return outArray;
	}
}
