package content;

import java.util.ArrayList;
import java.util.List;

/**
 * TagFile is a specific unpaired tag for configure the file generation
 * 
 * @author Nicolas Amiot
 */
public class TagFile {
	
	/**
	 * Name of the file
	 */
	private String filename;
	
	/**
	 * Location of the file
	 */
	private String location;
	
	/**
	 * Contents of the file
	 */
	private List<Content> contents;
	
	/**
	 * Default constructor
	 */
	public TagFile() {
		this.contents = new ArrayList<>();
	}
	
	/**
	 * Overloading constructor
	 * 
	 * @param filename
	 * @param location
	 * @param contents
	 */
	public TagFile(String filename, String location, List<Content> contents) {
		this.filename = filename;
		this.location = location;
		this.contents = contents;
	}

	/**
	 * Gets the {@link #filename}
	 * 
	 * @return
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Sets the {@link #filename}
	 * 
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Gets the {@link #location}
	 * 
	 * @return
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the {@link #location}
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
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

}
