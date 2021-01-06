package tags;

import java.util.List;
import java.util.Map;

import content.Content;
import content.PairedTag;
import entity.Variables;
import exception.Message;
import exception.TagException;
import main.Generator;
import utils.Marker;

/**
 * For class allows to loop on objects
 * 
 * @author Nicolas Amiot
 */
public class Foreach {
	
	/**
	 * State begin of the Foreach tag
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static String begin(PairedTag tag, Variables variables) throws TagException {
		Map<String, String> parameters = tag.getParameters();
		List<Content> contents = tag.getContents();
		int loop = 0;
		int maxLoop = Generator.getInstance().getLoop();
		String text = "";
		String item = parameters.get(Marker.PARAM_ITEM);
		if(variables.contains(item)) {
			throw new TagException(Message.existingVariable(item));
		}
		Object[] items = (Object[]) Marker.eval(parameters.get(Marker.PARAM_ITEMS), variables);
		for (Object variable : items) {
			loop++;
			if(loop > maxLoop)
			{
				throw new TagException(Message.infiniteLoop());
			}
			variables.openBlock();
			variables.set(item, variable);
			text += Marker.iterate(contents, variables);
			variables.closeBlock();
		}
		tag.setExecute(true);
		return text;
	}

}
