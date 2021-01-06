package entity;

import content.Tag;

/**
 * Index is the position of starting and ending tag
 * 
 * @author Nicolas Amiot
 */
public class Index {
	
	/**
	 * Tag recovered
	 */
	private Tag tag;
	
	/**
	 * Position of the first character tag
	 */
	private int start;
	
	/**Position of the last character tag
	 * 
	 */
	private int end;
	
	/**
	 * Constructor
	 * 
	 * @param tag
	 * @param start
	 * @param end
	 */
	public Index(Tag tag, int start, int end)
	{
		this.tag = tag;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Gets the {@link #tag}
	 * 
	 * @return
	 */
	public Tag getTag() {
		return tag;
	}
	
	/**
	 * Sets the {@link #tag}
	 * 
	 * @param tag
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}

	/**
	 * Gets the {@link #start}
	 * 
	 * @return
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * Sets the {@link #start}
	 * 
	 * @param start
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * Gets the {@link #end}
	 * 
	 * @return
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Sets the {@link #end}
	 * 
	 * @param end
	 */
	public void setEnd(int end) {
		this.end = end;
	}

}
