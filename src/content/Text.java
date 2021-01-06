package content;

/**
 * Text class contains a text fragment to generate
 * 
 * @author Nicolas Amiot
 */
public class Text extends Content {
	
	/**
	 * String value of the text
	 */
	private String value;
	
	/**
	 * Default constructor
	 */
	public Text() {
		
	}
	
	/**
	 * Overloading constructor
	 * 
	 * @param value
	 */
	public Text(String value) {
		this.value = value;
	}

	/**
	 * Gets the {@link #value}
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the {@link #value}
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
