package content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PairedTag is opened with state and closed with other state.
 * 
 * @author Nicolas Amiot
 */
public class PairedTag extends Tag {
	
	/**
	 * Contents that contains this tag
	 */
	private List<Content> contents;
	
	/**
	 * Specifies if this tag is a last before the end state
	 */
	private boolean last;
	
	/**
	 * Default constructor
	 */
	public PairedTag() {
		super();
		contents = new ArrayList<>();
	}
	
	/**
	 * Overloading constructor
	 * 
	 * @param name
	 * @param state
	 * @param parameters
	 */
	public PairedTag(String name, String state, Map<String, String> parameters) {
		super(name, state, parameters);
		contents = new ArrayList<>();
		setLast(false);
	}

	/**
	 * Gets the {@link #contents}
	 * 
	 * @return
	 */
	public List<Content> getContents() {
		return contents;
	}

	/**
	 * Sets the {@link #contents}
	 * 
	 * @param contents
	 */
	public void setContents(List<Content> contents) {
		this.contents = contents;
	}
	
	/**
	 * Add a {@link #contents}
	 * 
	 * @param content
	 */
	public void addContents(Content content) {
		contents.add(content);
	}

	/**
	 * Gets the {@link #last}
	 * 
	 * @return
	 */
	public boolean isLast() {
		return last;
	}

	/**
	 * Sets the {@link #last}
	 * 
	 * @param last
	 */
	public void setLast(boolean last) {
		this.last = last;
	}

}
