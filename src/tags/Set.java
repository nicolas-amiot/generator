package tags;

import java.util.Map;

import content.UnpairedTag;
import entity.Variables;
import exception.Message;
import exception.TagException;
import utils.Marker;

/**
 * Set class allows to store a variable
 * 
 * @author Nicolas Amiot
 */
public class Set {
	
	/**
	 * State begin of the Set tag
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static void begin(UnpairedTag tag, Variables variables) throws TagException {
		Map<String, String> parameters = tag.getParameters();
		String scope = parameters.get(Marker.PARAM_SCOPE);
		Object object;
		try {
			object = Marker.eval(parameters.get(Marker.PARAM_VALUE), variables);
		} catch(TagException e) {
			object = parameters.get(Marker.PARAM_VALUE);
		}
		if(scope == null || scope.equals("default")) {
			variables.set(parameters.get(Marker.PARAM_ITEM), object);
		} else if(scope.equals("global")) {
			variables.set(parameters.get(Marker.PARAM_ITEM), object, false);
		} else {
			throw new TagException(Message.invalidSetScope());
		}
		tag.setExecute(true);
	}

}
