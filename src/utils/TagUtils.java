package utils;

public class TagUtils {

	/**
	 * Private constructor
	 */
	private TagUtils() {}
	
	/**
	 * Indent the generated text
	 * 
	 * @param text
	 * @param number
	 * @return
	 */
	public static String indent(String text, int number) {
		StringBuilder sb = new StringBuilder();
		String[] lignes = text.split("\r\n|\n");
		for(int i = 0; i < lignes.length; i++) {
			String ligne = lignes[i];
			if(i == lignes.length - 1) {
				ligne = TagUtils.rtrim(ligne);
			} else if(i != 0) {
				ligne = ligne + "\r\n";
			}
			for(int j = 0; j < number; j++) {
				if(ligne.indexOf("\t") == 0) {
					ligne = ligne.substring(1);
				} else if(ligne.indexOf("    ") == 0) {
					ligne = ligne.substring(4);
				}
			}
			sb.append(ligne);
		}
		return sb.toString();
	}
	
	/**
	 * Locate a tag in a file
	 * 
	 * @param text
	 * @param previousLine
	 * @param previousColumn
	 * @return
	 */
	public static int[] locate(String text, int previousLine, int previousColumn) {
		if(previousLine == 0) {
			previousLine = 1;
		}
		if(previousColumn == 0) {
			previousColumn = 1;
		}
		String[] lines = text.split("\n");
		int line = lines.length;
		int column = lines[lines.length - 1].replaceAll("\t", "    ").replaceAll("\\P{Print}", "").length() + 1;
		if(line == 1) {
			line = previousLine;
			column += previousColumn - 1;
		} else {
			line += previousLine - 1;
		}
		return new int[]{line, column};
	}
	
	/**
	 * Capitalize a string
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		if(str != null){
			str = str.trim();
			if(str.length() > 1){
				return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
			} else {
				return str.toUpperCase();
			}
		}
		return "";
	}
	
	/**
	 * Join a list of string with specific delimiter
	 * 
	 * @param delimiter
	 * @param elements
	 * @return
	 */
	public static String join(char delimiter, String... elements) {
		StringBuilder sb = new StringBuilder();
		for(String element : elements) {
			if(element != null && !element.isEmpty()) {
				sb.append(element);
				if(element.charAt(element.length() - 1) != delimiter) {
					sb.append(delimiter);
				}
			}
		}
		if(sb.length() != 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}
	
	/**
	 * Verify if the string is a number
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNumber(CharSequence cs) {
		if (cs == null || cs.length() == 0) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
	}
	
	/**
	 * trim the right whitespaces
	 * 
	 * @param s
	 * @return
	 */
	public static String rtrim(String s) {
        int i = s.length() - 1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }
	
	/**
	 * trim the left whitespaces
	 * 
	 * @param s
	 * @return
	 */
	public static String ltrim(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }

}
