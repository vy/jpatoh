package tr.edu.bilkent.cs.patoh;

/**
 * Exception raised in case of a shared library related execution error. (E.g.
 * When function return status is not equal to 0.)
 */
public class HyperGraphException extends Exception
{
    HyperGraphException(String message)
    {
	super(message);
    }
}
