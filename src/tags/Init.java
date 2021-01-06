package tags;

import java.util.Map;

import content.UnpairedTag;
import entity.Variables;
import content.TagFile;
import exception.TagException;
import utils.Marker;

/**
 * Init class is used for configure the generation of file
 * 
 * @author Nicolas Amiot
 */
public class Init {

	/**
	 * State begin of the Init tag
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static TagFile begin(UnpairedTag tag, Variables variables) throws TagException {
		Map<String, String> parameters = tag.getParameters();
		TagFile tagFile = new TagFile();
		if(parameters.containsKey(Marker.PARAM_TEST))
		{
			boolean result = (boolean) Marker.eval(parameters.get(Marker.PARAM_TEST), variables);
			if(!result) {
				tag.setExecute(false);
				return null;
			}
		}
		if(parameters.containsKey(Marker.PARAM_FOLDER))
		{
			tagFile.setLocation(parameters.get(Marker.PARAM_FOLDER));
		}
		if(parameters.containsKey(Marker.PARAM_FILENAME))
		{
			tagFile.setFilename(parameters.get(Marker.PARAM_FILENAME));
		}
		tag.setExecute(true);
		return tagFile;
	}

}
