package exception;

import entity.Parameter;
import main.Generator;
import utils.Marker;

/**
 * Error message
 * 
 * @author Nicolas Amiot
 */
public class Message {
	
	/**
	 * Constructor
	 */
	private Message() {}
	
	/**
	 * Build the message for a invalid tag
	 * 
	 * @param name
	 * @param state
	 * @param parameters
	 * @return
	 */
	public static String invalidTag(String name, String state, Parameter parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append("The tag <");
		sb.append(Generator.getInstance().getIdentifier());
		if (!Marker.TAG_INIT.equals(name)) {
			sb.append(name);
		}
		if (Marker.STATE_BEGIN.equals(state)) {
			sb.append("> or <");
			sb.append(Generator.getInstance().getIdentifier());
			sb.append(name);
		}
		sb.append(":");
		sb.append(state);
		sb.append("> ");
		if(parameters.getOrders() != null) {
			sb.append("can only be placed after the ");
			for(String stateOrdered : parameters.getOrders()) {
				sb.append(stateOrdered);
				sb.append(", ");
			}
			sb.delete(sb.length() - 2, sb.length());
			sb.append(" state and ");
		}
		if (parameters.getRequires().length == 0 && parameters.getOptionals().length == 0) {
			sb.append("not have parameter");
		} else {
			if (parameters.getRequires().length != 0) {
				sb.append("must have ");
				for (String required : parameters.getRequires()) {
					sb.append(required);
					sb.append(", ");
				}
				sb.delete(sb.length() - 2, sb.length());
			}
			if (parameters.getRequires().length != 0 && parameters.getOptionals().length != 0) {
				sb.append(" and ");
			}
			if (parameters.getOptionals().length != 0) {
				sb.append("can have ");
				for (String optional : parameters.getOptionals()) {
					sb.append(optional);
					sb.append(", ");
				}
				sb.delete(sb.length() - 2, sb.length());
			}
		}
		return sb.toString();
	}
	
	/**
	 * Message for a not found tag
	 * 
	 * @param nom
	 * @return
	 */
	public static String tagNotFound(String name) {
		return "The tag " + name + " not exist";
	}
	
	/**
	 * Message for a not found state
	 * 
	 * @param name
	 * @param state
	 * @return
	 */
	public static String stateNotFound(String name, String state) {
		return "The state " + state + " for the tag " + name + " not exist";
	}
	
	/**
	 * Message for the tag init not placed at the beginning
	 * 
	 * @return
	 */
	public static String misplacedInit() {
		return "The initializer tag can only be placed at the beginning of the file";
	}
	
	/**
	 * Message for unclosed tag
	 * 
	 * @param name
	 * @return
	 */
	public static String unclosedTag(String name) {
		return "The tag '" + name + "' to close does not match with the last open tag";
	}
	
	/**
	 * Message for unmatched tag
	 * 
	 * @param name
	 * @return
	 */
	public static String unmatchedTag(String name) {
		return "This tag '" + name + "' does not match with the last open tag";
	}
	
	/**
	 * Message for unclosed tags
	 * 
	 * @return
	 */
	public static String unclosedTags() {
		return "There are no closed tags";
	}
	
	/**
	 * Message for a variable name with wrong format
	 * 
	 * @return
	 */
	public static String wrongFormat() {
		return "A variable must start with letter and can only contains letter, number, underscore";
	}
	
	/**
	 * Message for unreplaced variable
	 * 
	 * @param variable
	 * @return
	 */
	public static String unreplacedVariable(String variable) {
		return "Initialization variable " + variable + " can't be replaced";
	}
	
	/**
	 * Message for unexisting tag
	 * 
	 * @param classname
	 * @param line
	 * @return
	 */
	public static String tagNotExist(String classname, int line) {
		return "The tag " + classname + " at the line " + line + "not exist";
	}
	
	/**
	 * Message for unexisting state
	 * 
	 * @param classname
	 * @param methodname
	 * @param line
	 * @return
	 */
	public static String stateNotExist(String classname, String methodname, int line) {
		return "Invalid state " + methodname + " for the tag " + classname + " at the line " + line;
	}
	
	/**
	 * Message for unvokable tag
	 * 
	 * @param classname
	 * @param methodname
	 * @param line
	 * @return
	 */
	public static String unvokableTag(String classname, String methodname, int line) {
		return "Can't invoke the method " + methodname + " of the class " + classname + " at the line " + line;
	}
	
	/**
	 * Message for invalid script
	 * 
	 * @return
	 */
	public static String invalidScript() {
		return "Invalid script";
	}
	
	/**
	 * Message for existing variable
	 * 
	 * @param variable
	 * @return
	 */
	public static String existingVariable(String variable) {
		return "Variable " + variable + " already exist";
	}
	
	/**
	 * Message for invalid scope
	 * 
	 * @return
	 */
	public static String invalidSetScope() {
		return "The scope can only be default or global";
	}
	
	/**
	 * Message for invalid do parameter
	 * 
	 * @return
	 */
	public static String invalidDoWhileValue() {
		return "The parameter do can only be yes or no";
	}
	
	/**
	 * Message for infinite loop
	 * 
	 * @return
	 */
	public static String infiniteLoop() {
		return "The loop seems infinite. Verify her condition or increase the loop max value in the properties (loop=value with value: min=1, max=1000000, default=1000).";
	}

}
