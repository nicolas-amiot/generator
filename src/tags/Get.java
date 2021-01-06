package tags;

import java.util.Map;

import content.UnpairedTag;
import entity.Variables;
import exception.TagException;
import utils.Marker;

/**
 * Get class allows to retrieve the value of a variable
 * 
 * @author Nicolas Amiot
 */
public class Get {
	
	/**
	 * State begin of the Get tag
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static String begin(UnpairedTag tag, Variables variables) throws TagException {
		Map<String, String> parameters = tag.getParameters();
		String texte = Marker.eval(parameters.get(Marker.PARAM_ITEM), variables).toString();
		tag.setExecute(true);
		return texte;
	}

}
