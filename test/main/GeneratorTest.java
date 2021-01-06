package main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import content.Content;
import content.UnpairedTag;
import content.PairedTag;
import content.Text;
import exception.TagException;
import utils.Marker;

/**
 * JUnit Generator class
 * 
 * @author Nicolas Amiot
 */
class GeneratorTest {
	
	/**
	 * Generator class
	 */
	Generator generator;

	/**
	 * Initializes the instance of TagExec
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		generator = Generator.getInstance();
		generator.setSource("test/resources/source");
		generator.setDestination("test/resources/dest");
		generator.setProject("test");
	}

	/**
	 * Reading the JSON property file
	 * 
	 * @throws IOException
	 * @throws TagException
	 */
	@Test
	@SuppressWarnings("unchecked")
	void readVariablesTest() throws IOException, TagException {
		generator.setDatafile("test.json");
		generator.readVariables();
		Object object = generator.getVariables().get("Table");
		
		Map<String, Object> table = (Map<String, Object>) object;
		assertEquals("Table 1", (String) table.get("name"));
		assertEquals(56.60, (double) ((Object[]) table.get("price"))[0]);
		assertEquals(12.00, (double) ((Object[]) table.get("price"))[1]);
		
		Map<String, Object> property = (Map<String, Object>) table.get("property");
		assertEquals("I'm red", (String) property.get("color"));
		assertEquals(10, (int) property.get("size"));
		assertEquals(true, (boolean) property.get("sortable"));
		assertEquals(false, (boolean) property.get("others"));
		
		Object[] headers = (Object[]) table.get("headers");
		assertEquals("header1", (String) ((Map<String, Object>) headers[0]).get("name"));
		assertEquals("header2", (String) ((Map<String, Object>) headers[1]).get("name"));
		assertEquals("1", (String) ((Map<String, Object>) headers[0]).get("id"));
		assertEquals("2", (String) ((Map<String, Object>) headers[1]).get("id"));
	}
	
	/**
	 * Reading a valid file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TagException
	 */
	@Test
	void readContentOkTest() throws FileNotFoundException, IOException, TagException {
		generator.setFilename("test1.txt");
		List<Content> contents = generator.readContent();
		
		assertEquals(contents.size(), 5);
		
		UnpairedTag tag1 = (UnpairedTag) contents.get(0);
		assertEquals(Marker.TAG_INIT, tag1.getName());
		assertEquals(Marker.STATE_BEGIN, tag1.getState());
		assertEquals(3, tag1.getParameters().size());
		assertEquals("gen/java", tag1.getParameters().get(Marker.PARAM_FOLDER));
		assertEquals("Test.txt", tag1.getParameters().get(Marker.PARAM_FILENAME));
		assertEquals("Table.data != null", tag1.getParameters().get(Marker.PARAM_TEST));
		assertEquals(1, tag1.getLine());
		assertEquals(1, tag1.getColumn());
		assertTrue(contents.get(1) instanceof Text);
		
		UnpairedTag tag2 = (UnpairedTag) contents.get(2);
		assertEquals(Marker.TAG_SET, tag2.getName());
		assertEquals(Marker.STATE_BEGIN, tag2.getState());
		assertEquals(2, tag2.getParameters().size());
		assertEquals("array", tag2.getParameters().get(Marker.PARAM_ITEM));
		assertEquals("['HTML', 'JS', 'CSS'];", tag2.getParameters().get(Marker.PARAM_VALUE));
		assertEquals(4, tag2.getLine());
		assertEquals(5, tag2.getColumn());
		
		PairedTag tag3 = (PairedTag) contents.get(3);
		assertEquals(Marker.TAG_FOR, tag3.getName());
		assertEquals(Marker.STATE_BEGIN, tag3.getState());
		assertEquals(4, tag3.getParameters().size());
		assertEquals("i", tag3.getParameters().get(Marker.PARAM_ITEM));
		assertEquals("0", tag3.getParameters().get(Marker.PARAM_BEGIN));
		assertEquals("array.length - 1", tag3.getParameters().get(Marker.PARAM_END));
		assertEquals("1", tag3.getParameters().get(Marker.PARAM_STEP));
		assertEquals(5, tag3.getLine());
		assertEquals(5, tag3.getColumn());
		assertTrue(contents.get(4) instanceof Text);
		
		contents = tag3.getContents();
		assertEquals(contents.size(), 5);
		
		assertTrue(contents.get(0) instanceof Text);
		UnpairedTag tag4 = (UnpairedTag) contents.get(1);
		assertEquals(Marker.TAG_GET, tag4.getName());
		assertEquals(Marker.STATE_BEGIN, tag4.getState());
		assertEquals(1, tag4.getParameters().size());
		assertEquals("i", tag4.getParameters().get(Marker.PARAM_ITEM));
		assertEquals(6, tag4.getLine());
		assertEquals(17, tag4.getColumn());
		assertTrue(contents.get(2) instanceof Text);
		
		PairedTag tag5 = (PairedTag) contents.get(3);
		assertEquals(Marker.TAG_IF, tag5.getName());
		assertEquals(Marker.STATE_BEGIN, tag5.getState());
		assertEquals(1, tag5.getParameters().size());
		assertEquals("array[i] != null", tag5.getParameters().get(Marker.PARAM_TEST));
		assertEquals(7, tag5.getLine());
		assertEquals(9, tag5.getColumn());
		
		PairedTag tag6 = (PairedTag) contents.get(4);
		assertEquals(Marker.TAG_IF, tag6.getName());
		assertEquals(Marker.STATE_NEITHER, tag6.getState());
		assertEquals(0, tag6.getParameters().size());
		assertEquals(9, tag6.getLine());
		assertEquals(9, tag6.getColumn());
		
		UnpairedTag tag7 = (UnpairedTag) tag5.getContents().get(0);
		assertEquals(Marker.TAG_GET, tag7.getName());
		assertEquals(Marker.STATE_BEGIN, tag7.getState());
		assertEquals(1, tag7.getParameters().size());
		assertEquals("array[i]", tag7.getParameters().get(Marker.PARAM_ITEM));
		assertEquals(8, tag7.getLine());
		assertEquals(13, tag7.getColumn());
		assertTrue(tag6.getContents().get(0) instanceof Text);
	}
	
	/**
	 * Reading an invalid file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TagException
	 */
	@Test
	void readContentKOTest() throws FileNotFoundException, IOException, TagException {
		generator.setFilename("test2.txt");
		TagException ex1 = assertThrows(TagException.class, () -> generator.readContent());
		System.err.println(ex1.getMessage());
		
		generator.setFilename("test3.txt");
		TagException ex2 = assertThrows(TagException.class, () -> generator.readContent());
		System.err.println(ex2.getMessage());
		
		generator.setFilename("test4.txt");
		TagException ex3 = assertThrows(TagException.class, () -> generator.readContent());
		System.err.println(ex3.getMessage());
		
		generator.setFilename("test5.txt");
		TagException ex4 = assertThrows(TagException.class, () -> generator.readContent());
		System.err.println(ex4.getMessage());
	}
	
	/**
	 * Writing a file
	 * 
	 * @throws TagException
	 * @throws IOException
	 */
	@Test
	void writeFileTest() throws TagException, IOException {
		File dir = new File(generator.getDestination());
		if (dir.isDirectory()) {
		    for (File file : dir.listFiles())
		    {
		    	file.delete();
		    }
		    dir.delete();
		}
		
		generator.setFilename("test1.txt");
		List<Content> contents = new ArrayList<>();
		assertFalse(Files.exists(Paths.get(generator.getDestination() + "/test1.txt")));
		generator.writeFile(contents);
		assertTrue(Files.exists(Paths.get(generator.getDestination() + "/test1.txt")));
		UnpairedTag init = new UnpairedTag();
		init.setName(Marker.TAG_INIT);
		init.setState(Marker.STATE_BEGIN);
		init.addParameters(Marker.PARAM_FILENAME, "test2.txt");
		contents.add(init);
		assertFalse(Files.exists(Paths.get(generator.getDestination() + "/test2.txt")));
		generator.writeFile(contents);
		assertTrue(Files.exists(Paths.get(generator.getDestination() + "/test2.txt")));
	}

}
