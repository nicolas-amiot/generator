package exception;

/**
 * TagException class used during the generation
 * 
 * @author Nicolas Amiot
 */
public class TagException extends Exception {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with error message
	 * 
	 * @param message
	 */
	public TagException(String message) {
		super(message);
	}

	/**
	 * Constructor with error message and cause
	 * 
	 * @param message
	 * @param cause
	 */
	public TagException(String message, Throwable cause) {
		super(message, cause);
	}

}
