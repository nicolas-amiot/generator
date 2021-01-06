package entity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import exception.Message;
import exception.TagException;

/**
 * This class contains all variables stored during the generation
 * 
 * @author Nicolas Amiot
 */
public class Variables {
	
	/**
	 * A valid variable must start with letter and can contains letters, numbers and underscores
	 */
	public static final Pattern VARIABLE = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
	
	/**
	 * Root variables are the varaiables reading in the datafile configuration
	 */
	private Map<String, Object> root;
	
	/**
	 * Data variables contains all variables
	 */
	private Map<String, Object> data;
	
	/**
	 * List of the temporary variable by tag
	 */
	private List<List<String>> temp;
	
	/**
	 * Hidden variables used by the different tag
	 */
	private List<Object> store;
	
	/**
	 * Constructor
	 * 
	 * @param data
	 */
	public Variables(Map<String, Object> data) {
		this.root = data;
		this.data = new LinkedHashMap<>(data);
		this.temp = new LinkedList<>();
		this.store = new LinkedList<>();
	}
	
	/**
	 * Create or use the passing map of object and add the new object if the key name is not null
	 * 
	 * @param object
	 * @param key
	 * @param value
	 * @return
	 * @throws TagException if the key don't match with {@link #VARIABLE}
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> putObject(Object object, String key, Object value) throws TagException {
		if(!(object instanceof Map)) {
			object = new LinkedHashMap<String, Object>();
		}
		if(key != null) {
			if(VARIABLE.matcher(key).matches()) {
				((Map<String, Object>) object).put(key, value);
			} else {
				throw new TagException(Message.wrongFormat());
			}
		}
		return (Map<String, Object>) object;
	}
	
	/**
	 * Create or use the passing array of object and add the news objects
	 * 
	 * @param object
	 * @param values
	 * @return
	 */
	public static Object[] putArray(Object object, Object... values) {
		if(!(object instanceof Object[])) {
			object = new Object[0];
		}
		if(values.length != 0) {
			object = Stream.of((Object[]) object, values).flatMap(Stream::of).toArray(Object[]::new);
		}
		return (Object[]) object;
	}
	
	/**
	 * Gets the last storage parameter
	 * 
	 * @return
	 */
	public Object getStore() {
		if(store.size() > 0)
		{
			return store.get(store.size() - 1);
		} else {
			return null;
		}
	}

	/**
	 * Add a new storage parameter
	 * 
	 * @param object
	 */
	public void addStore(Object object) {
		store.add(object);
	}
	
	/**
	 * Remove the last storage parameter
	 */
	public void delStore() {
		store.remove(store.size() - 1);
	}
	
	/**
	 * Open a new block
	 */
	public void openBlock() {
		temp.add(new LinkedList<>());
	}
	
	/**
	 * Close the last last block and remove the created variables in this last
	 */
	public void closeBlock() {
		for(String key : temp.get(temp.size() - 1)) {
			data.remove(key);
		}
		temp.remove(temp.size() - 1);
	}
	
	/**
	 * Verify if this variable already exist
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		return data.containsKey(key);
	}
	
	/**
	 * Gets the variable
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return data.get(key);
	}
	
	/**
	 * Sets a temporary variable
	 * <br>See also {@link #set(String, Object, boolean)}}
	 * 
	 * @param key
	 * @param object
	 * @throws TagException
	 */
	public void set(String key, Object object) throws TagException {
		set(key, object, true);
	}
	
	/**
	 * Sets a variable that will be deleted when the block will be closed if it's temporary and was not created before
	 * 
	 * @param key
	 * @param object
	 * @param temporary
	 * @throws TagException if the variable existing in the json variable or if the key don't match with {@link #VARIABLE}
	 */
	public void set(String key, Object object, boolean temporary) throws TagException {
		if(root.containsKey(key)) {
			throw new TagException(Message.unreplacedVariable(key));
		}
		if(key == null || !VARIABLE.matcher(key).matches()) {
			throw new TagException(Message.wrongFormat());
		}
		if(temporary && !temp.isEmpty() && !data.containsKey(key)) {
			temp.get(temp.size() - 1).add(key);
		}
		data.put(key, object);
	}

}
