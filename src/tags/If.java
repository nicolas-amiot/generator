package tags;

import java.util.List;
import java.util.Map;

import content.Content;
import content.PairedTag;
import entity.Variables;
import exception.TagException;
import utils.Marker;

/**
 * If class allows to condition certain parts
 * 
 * @author Nicolas Amiot
 */
public class If {
	
	/**
	 * State begin of the If tag
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static String begin(PairedTag tag, Variables variables) throws TagException {
		Map<String, String> parameters = tag.getParameters();
		List<Content> contents = tag.getContents();
		String text = null;
		boolean result = (boolean) Marker.eval(parameters.get(Marker.PARAM_TEST), variables);
		if(result) {
			variables.openBlock();
			text = Marker.iterate(contents, variables);
			variables.closeBlock();
		}
		tag.setExecute(result);
		return text;
	}
	
	/**
	 * State other of the If tag
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static String other(PairedTag tag, Variables variables) throws TagException {
		Map<String, String> parameters = tag.getParameters();
		List<Content> contents = tag.getContents();
		String text = null;
		boolean result = (boolean) Marker.eval(parameters.get(Marker.PARAM_TEST), variables);
		if(result) {
			variables.openBlock();
			text = Marker.iterate(contents, variables);
			variables.closeBlock();
		}
		tag.setExecute(result);
		return text;
	}
	
	/**
	 * State neither of the If tag
	 * 
	 * @param tag
	 * @param variables
	 * @return
	 * @throws TagException
	 */
	public static String neither(PairedTag tag, Variables variables) throws TagException {
		List<Content> contents = tag.getContents();
		variables.openBlock();
		String text = Marker.iterate(contents, variables);
		variables.closeBlock();
		tag.setExecute(true);
		return text;
	}

}
