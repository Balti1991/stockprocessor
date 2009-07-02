/**
 * 
 */
package stockprocessor.handler.processor.relational;

/**
 * @author anti
 */
public class EqualProcessor extends AbstractRelationalProcessor<Number>
{

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.relational.AbstractRelationalProcessor
	 * #calculate(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Boolean calculate(Number inputDataA, Number inputDataB)
	{
		return inputDataA == inputDataB;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getName()
	 */
	@Override
	public String getName()
	{
		return "Equal";
	}

}
