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
 * For class allows to loop according to a counter
 * 
 * @author Nicolas Amiot
 */
public class For {
	
	/**
	 * State begin of the For tag
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
		String var = parameters.get(Marker.PARAM_ITEM);
		String step = parameters.get(Marker.PARAM_STEP);
		if(variables.contains(var)) {
			throw new TagException(Message.existingVariable(var));
		}
		int begin = (int) Marker.eval(parameters.get(Marker.PARAM_BEGIN), variables);
		int end = (int) Marker.eval(parameters.get(Marker.PARAM_END), variables);
		int increment = 1;
		if(step != null) {
			increment = (int) Marker.eval(parameters.get(Marker.PARAM_STEP), variables);
		}
		for (int i = begin; i - begin <= end - begin; i += increment) {
			loop++;
			if(loop > maxLoop)
			{
				throw new TagException(Message.infiniteLoop());
			}
			variables.openBlock();
			variables.set(var, i);
			text += Marker.iterate(contents, variables);
			variables.closeBlock();
		}
		tag.setExecute(true);
		return text;
	}

}
