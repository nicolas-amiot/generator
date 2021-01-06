package entity;

/**
 * Parameter specifies the state orders and require/optional parameters of a tag
 * 
 * @author Nicolas Amiot
 */
public class Parameter {
	
	/**
	 * State orders of a tag name
	 */
	private String[] orders;
	
	/**
	 * Requires parameters of a tag name
	 */
	private String[] requires;
	
	/**
	 * Optionals parameters of a tag name
	 */
	private String[] optionals;
	
	/**
	 * Constructor
	 */
	public Parameter() {
		
	}
	
	/**
	 * Gets the {@link #orders}
	 * 
	 * @return
	 */
	public String[] getOrders() {
		return orders;
	}

	/**
	 * Add {@link #orders}
	 * 
	 * @param orders
	 * @return
	 */
	public Parameter addOrders(String... orders) {
		this.orders = orders;
		return this;
	}

	/**
	 * Gets the {@link #requires}
	 * 
	 * @return
	 */
	public String[] getRequires() {
		return requires;
	}

	/**
	 * Add {@link #requires}
	 * 
	 * @param requires
	 * @return
	 */
	public Parameter addRequires(String... requires) {
		this.requires = requires;
		return this;
	}

	/**
	 * Gets the {@link #optionals}
	 * 
	 * @return
	 */
	public String[] getOptionals() {
		return optionals;
	}
	
	/**
	 * Add {@link #optionals}
	 * 
	 * @param optionals
	 * @return
	 */
	public Parameter addOptionals(String... optionals) {
		this.optionals = optionals;
		return this;
	}
	
}
