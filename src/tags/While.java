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
 * While class allows to loop until the condition is wrong
 * 
 * @author Nicolas Amiot
 */
public class While {
	
	/**
	 * State begin of the While tag
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
		String doWhile = parameters.get(Marker.PARAM_DO);
		if(doWhile == null || doWhile.equals("no")) {
			while((boolean) Marker.eval(parameters.get(Marker.PARAM_TEST), variables)) {
				loop++;
				if(loop > maxLoop)
				{
					throw new TagException(Message.infiniteLoop());
				}
				variables.openBlock();
				text += Marker.iterate(contents, variables);
				variables.closeBlock();
			}
		} else if(doWhile.equals("yes")) {
			do {
				variables.openBlock();
				text += Marker.iterate(contents, variables);
				variables.closeBlock();
			} while((boolean) Marker.eval(parameters.get(Marker.PARAM_TEST), variables));
		} else {
			throw new TagException(Message.invalidDoWhileValue());
		}
		tag.setExecute(true);
		return text;
	}

}
