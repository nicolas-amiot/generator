package content;

import java.util.HashMap;
import java.util.Map;

/**
 * Tag is superclass of PairedTag and UnpairedTag
 * 
 * @author Nicolas Amiot
 */
public abstract class Tag extends Content {

	/**
	 * Name tag
	 */
	private String name;
	
	/**
	 * State tag
	 */
	private String state;
	
	/**
	 * Parameters tag
	 */
	private Map<String, String> parameters;
	
	/**
	 * Specifies if this tag was executed with brio
	 */
	private boolean execute;
	
	/**
	 * The line number where the tag is located
	 */
	private int line;
	
	/**
	 * The column number where the tag is located
	 */
	private int column;
	
	/**
	 * Default constructor
	 */
	public Tag() {
		parameters = new HashMap<>();
	}
	
	/**
	 * Overloading constructor
	 * 
	 * @param name
	 * @param etat
	 * @param parameters
	 */
	public Tag(String name, String state, Map<String, String> parameters) {
		this.name = name;
		this.state = state;
		this.parameters = parameters;
	}

	/**
	 * Gets the {@link #name}
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the {@link #name}
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the {@link #state}
	 * 
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the {@link #state}
	 * 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the {@link #parameters}
	 * 
	 * @return
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Sets the {@link #parameters}
	 * 
	 * @param parameters
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Add a {@link #parameters}
	 * 
	 * @param key
	 * @param value
	 */
	public void addParameters(String key, String value) {
		parameters.put(key, value);
	}
	
	/**
	 * Gets the {@link #execute}
	 * 
	 * @return
	 */
	public boolean isExecute() {
		return execute;
	}

	/**
	 * Sets the {@link #execute}
	 * 
	 * @param execute
	 */
	public void setExecute(boolean execute) {
		this.execute = execute;
	}
	
	/**
	 * Gets the {@link #line}
	 * 
	 * @return
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Sets the {@link #line}
	 * 
	 * @param line
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * Gets the {@link #column}
	 * 
	 * @return
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Sets the {@link #column}
	 * 
	 * @param column
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	
}
