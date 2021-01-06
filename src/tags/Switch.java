package tags;

import java.util.List;
import java.util.Map;

import content.Content;
import content.PairedTag;
import entity.Variables;
import exception.TagException;
import utils.Marker;

/**
 * If class allows to condition certain parts without repeat the condition
 * 
 * @author Nicolas Amiot
 */
public class Switch {
	
	/**
	 * State begin of the Switch tag
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
		boolean execute = false;
		Object object = Marker.eval(parameters.get(Marker.PARAM_TEST), variables);
		variables.addStore(object);
		Object result = Marker.eval(parameters.get(Marker.PARAM_CASE), variables);
		if(variables.getStore().toString().equals(result.toString())) {
			variables.openBlock();
			text = Marker.iterate(contents, variables);
			variables.closeBlock();
			execute = true;
		}
		if(execute || tag.isLast())
		{
			variables.delStore();
		}
		tag.setExecute(execute);
		return text;
	}
	
	/**
	 * State other of the Switch tag
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
		boolean execute = false;
		Object result = Marker.eval(parameters.get(Marker.PARAM_CASE), variables);
		if(variables.getStore().toString().equals(result.toString())) {
			variables.openBlock();
			text = Marker.iterate(contents, variables);
			variables.closeBlock();
			execute = true;
		}
		if(execute || tag.isLast())
		{
			variables.delStore();
		}
		tag.setExecute(execute);
		return text;
	}
	
	/**
	 * State neither of the Switch tag
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
		variables.delStore();
		tag.setExecute(true);
		return text;
	}

}
