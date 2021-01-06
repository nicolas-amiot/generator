package content;

import java.util.Map;

/**
 * UnpairedTag is a auto closeable tag
 * 
 * @author Nicolas Amiot
 */
public class UnpairedTag extends Tag {

	/**
	 * Default constructor
	 */
	public UnpairedTag() {
		super();
	}
	
	/**
	 * Overloading constructor
	 * 
	 * @param name
	 * @param state
	 * @param parameters
	 */
	public UnpairedTag(String name, String state, Map<String, String> parameters) {
		super(name, state, parameters);
	}
	
}
