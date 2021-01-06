package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import content.Content;
import content.UnpairedTag;
import entity.Parameter;
import entity.Variables;
import content.PairedTag;
import content.Tag;
import content.Text;
import exception.Message;
import exception.TagException;

/**
 * Marker class is used for verify and execute the tags
 * 
 * @author Nicolas Amiot
 */
public class Marker {
	
	// Package of the tags
	private static final String PACKAGE = "tags.";
	
	// Tags name
	public static final String TAG_INIT = "init";
	public static final String TAG_FOR = "for";
	public static final String TAG_FOREACH = "foreach";
	public static final String TAG_WHILE = "while";
	public static final String TAG_IF = "if";
	public static final String TAG_SWITCH = "switch";
	public static final String TAG_GET = "get";
	public static final String TAG_SET = "set";

	// Methods name
	public static final String STATE_BEGIN = "begin";
	public static final String STATE_END = "end";
	public static final String STATE_OTHER = "other";
	public static final String STATE_NEITHER = "neither";

	// Parameters name
	public static final String PARAM_FOLDER = "folder";
	public static final String PARAM_FILENAME = "filename";
	public static final String PARAM_TEST = "test";
	public static final String PARAM_ITEMS = "items";
	public static final String PARAM_ITEM = "item";
	public static final String PARAM_BEGIN = "begin";
	public static final String PARAM_END = "end";
	public static final String PARAM_VALUE = "value";
	public static final String PARAM_STEP = "step";
	public static final String PARAM_CASE = "case";
	public static final String PARAM_DO = "do";
	public static final String PARAM_SCOPE = "scope";
	
	/**
	 * Iterate the different tags found
	 * 
	 * @param contents
	 * @param variable
	 * @return
	 * @throws TagException
	 */
	public static String iterate(List<Content> contents, Variables variables) throws TagException {
		boolean execute = false;
		StringBuilder sb = new StringBuilder();
		for(Content content : contents) {
			if(content instanceof Text) {
				Text text = (Text) content;
				sb.append(text.getValue());
			} else if(content instanceof Tag) {
				Tag tag = (Tag) content;
				if(!execute || STATE_BEGIN.equals(tag.getState())) {
					Object text = call(tag, variables);
					if(text != null)
					{
						sb.append(text);
					}
					execute = tag.isExecute();
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * Call the method corresponding to the state of tags
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static Object call(Tag tag, Variables variables) throws TagException {
		String classname = tag.getName();
		String methodname = tag.getState();
		try {
			Class<?> clazz = Class.forName(PACKAGE + TagUtils.capitalize(classname));
			// invoke : Pas besoin d'instancier l'objet car il s'agit d'un appel à une méthode statique
			if(tag instanceof UnpairedTag) {
				Method method = clazz.getMethod(methodname, UnpairedTag.class, Variables.class);
				return method.invoke(null, (UnpairedTag) tag, variables);
			} else {
				Method method = clazz.getMethod(methodname, PairedTag.class, Variables.class);
				return method.invoke(null, (PairedTag) tag, variables);
			}
		} catch (ClassNotFoundException e) {
			throw new TagException(Message.tagNotExist(classname, tag.getLine()), e.getCause());
		} catch (NoSuchMethodException | SecurityException e) {
			throw new TagException(Message.stateNotExist(classname, methodname, tag.getLine()), e.getCause());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new TagException(Message.unvokableTag(classname, methodname, tag.getLine()), e.getCause());
		}
	}
	
	/**
	 * Evaluate a text with Javascript engine
	 * 
	 * @param text
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static Object eval(String text, Variables variables) throws TagException {
		StringBuilder sb = new StringBuilder(text);
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Matcher m = Variables.VARIABLE.matcher(text);
		int i = 0;
		int taille = 0;
		while(m.find()) {
			String var = m.group(0);
			int start = m.start(0);
			int end = m.end(0);
			if((start == 0 || text.charAt(start - 1) != '.') && variables.contains(var))
			{
				i++;
				Object object = variables.get(var);
				String name = "$" + i;
				sb.replace(start - taille, end - taille, name);
				engine.put("$" + i, object);
				taille += var.length() - name.length();
			}
		}
		try {
			return evalObject(engine.eval(sb.toString()));
		} catch (ScriptException e) {
			throw new TagException(Message.invalidScript(), e.getCause());
		}
	}
	
	/**
	 * Return a specific engine object to standard java object
	 * 
	 * @param object
	 * @return
	 */
	private static Object evalObject(Object object) {
		// Pour correspondre à la transformation JSON du Reader
		if(object == null) {
			return Boolean.parseBoolean("null");
		// La fontion eval retourne pour un nombre soit un integer soit un double si trop long ou avec decimal
		} else if(object instanceof Integer) {
			return (int) object;
		// Lors d'operation sur un entier la fonction eval renvoi automatiquement un double
		} else if (object instanceof Double) {
			double number = (double) object;
			if(number == Math.floor(number) && (int) number == number) {
				return (int) number;
			} else {
				return number;
			}
		// Les tableaux et objets créés en javascrpt sont de type Bindings lors de l'eval
		} else if(object instanceof Bindings) {
			Bindings map = (Bindings) object;
			if(map.containsKey("0") || map.isEmpty()) {
				Object[] array = map.values().toArray();
				Object[] objects = new Object[array.length];
				for (int j = 0; j < array.length; j++) {
					objects[j] = evalObject(array[j]);
				}
				return objects;
			} else {
				Map<String, Object> objects = new HashMap<>();
				for (Entry<String, Object> entry : map.entrySet()) {
					objects.put(entry.getKey(), evalObject(entry.getValue()));
				}
				return objects;
			}
		} else {
			return object;
		}
	}
	
	/**
	 * Verify if the tags is a paired tag
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isPairedTag(String name) {
		try {
			searchTag(name, STATE_END);
			return true;
		} catch (TagException e) {
			return false;
		}
	}

	/**
	 * Validate the state and parameter name for a tag
	 * 
	 * @param name
	 * @param state
	 * @param parameters
	 * @param lastState
	 * @throws TagException
	 */
	public static void validdate(String name, String state, Map<String, String> parameters, String lastState) throws TagException {
		Parameter param = searchTag(name, state);
		List<String> paramList = new ArrayList<>(parameters.keySet());
		if (param != null) {
			if(!validateOrders(param.getOrders(), lastState) ||
					!validateParameters(param.getRequires(), param.getOptionals(), paramList)) {
				throw new TagException(Message.invalidTag(name, state, param));
			}
		}
	}
	
	/**
	 * Validate the state order for a tag
	 * 
	 * @param states
	 * @param lastState
	 * @return
	 */
	private static boolean validateOrders(String[] states, String lastState) {
		if(states != null && lastState != null) {
			for(String state : states) {
				if(lastState.equals(state)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Validate the required and optinal paramater for a state of a tag
	 * 
	 * @param requires
	 * @param optionals
	 * @param parameters
	 * @return
	 */
	private static boolean validateParameters(String[] requires, String[] optionals, List<String> parameters) {
		boolean find;
		// Vérifie que chaque paramètre obligatoire est présent et on les supprime de la liste
		for (String req : requires) {
			if (parameters.contains(req)) {
				parameters.remove(req);
			} else {
				return false;
			}
		}
		// Parmi les paramètres non obligatoires, on verifie qu'ils sont présents dans les optionnels
		for (String parameter : parameters) {
			find = false;
			for (String opt : optionals) {
				if (parameter.equals(opt)) {
					find = true;
					break;
				}
			}
			if (!find) {
				return false;
			}
		}
		return true;
	}

	/**
	 * List of different valid tags
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchTag(String name, String state) throws TagException {
		switch (name) {
		case TAG_INIT:
			return searchInitState(name, state);
		case TAG_GET:
			return searchGetState(name, state);
		case TAG_SET:
			return searchSetState(name, state);
		case TAG_FOR:
			return searchForState(name, state);
		case TAG_FOREACH:
			return searchForeachState(name, state);
		case TAG_WHILE:
			return searchWhileState(name, state);
		case TAG_IF:
			return searchIfState(name, state);
		case TAG_SWITCH:
			return searchSwitchState(name, state);
		default:
			throw new TagException(Message.tagNotFound(name));
		}
	}

	/**
	 * List of valid states and parameters for init tag
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchInitState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires().addOptionals(PARAM_FOLDER, PARAM_FILENAME, PARAM_TEST);
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}

	/**
	 * List of valid states and parameters for get tag
	 * 
	 * @param name
	 * @param etat
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchGetState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires(PARAM_ITEM);
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}

	/**
	 * List of valid states and parameters for set tag
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchSetState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires(PARAM_ITEM, PARAM_VALUE).addOptionals(PARAM_SCOPE);
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}

	/**
	 * List of valid states and parameters for for tag
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchForState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires(PARAM_ITEM, PARAM_BEGIN, PARAM_END).addOptionals(PARAM_STEP);
		case STATE_END:
			return null;
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}

	/**
	 * List of valid states and parameters for foreach tag
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchForeachState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires(PARAM_ITEM, PARAM_ITEMS).addOptionals();
		case STATE_END:
			return null;
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}

	/**
	 * List of valid states and parameters for while tag
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchWhileState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires(PARAM_TEST).addOptionals(PARAM_DO);
		case STATE_END:
			return null;
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}
	
	/**
	 * List of valid states and parameters for if tag
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchIfState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires(PARAM_TEST).addOptionals();
		case STATE_OTHER:
			return new Parameter().addOrders(STATE_BEGIN).addRequires(PARAM_TEST).addOptionals();
		case STATE_NEITHER:
			return new Parameter().addOrders(STATE_BEGIN, STATE_OTHER).addRequires().addOptionals();
		case STATE_END:
			return null;
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}
	
	/**
	 * List of valid states and parameters for switch tag
	 * 
	 * @param name
	 * @param state
	 * @return
	 * @throws TagException
	 */
	private static Parameter searchSwitchState(String name, String state) throws TagException {
		switch (state) {
		case STATE_BEGIN:
			return new Parameter().addRequires(PARAM_VALUE, PARAM_CASE).addOptionals();
		case STATE_OTHER:
			return new Parameter().addOrders(STATE_BEGIN).addRequires(PARAM_CASE).addOptionals();
		case STATE_NEITHER:
			return new Parameter().addOrders(STATE_BEGIN, STATE_OTHER).addRequires().addOptionals();
		case STATE_END:
			return null;
		default:
			throw new TagException(Message.stateNotFound(name, state));
		}
	}

}
