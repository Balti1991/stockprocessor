/**
 * 
 */
package stockprocessor.data.information;

/**
 * @author anti
 */
public class DefaultParameterInformation implements ParameterInformation
{
	private final String name;

	private final ParameterType type;

	/**
	 * @param name
	 * @param type
	 */
	public DefaultParameterInformation(String name, ParameterType type)
	{
		this.name = name;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.information.ParameterInformation#getDisplayName()
	 */
	@Override
	public String getDisplayName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.data.information.ParameterInformation#getType()
	 */
	@Override
	public ParameterType getType()
	{
		return type;
	}
}
