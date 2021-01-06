package main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import content.Content;
import content.PairedTag;
import content.Text;
import entity.Variables;
import exception.TagException;
import utils.Marker;

/**
 * JUnit Marker class
 * 
 * @author Nicolas Amiot
 */
class MarkerTest {

	/**
	 * Test the iterate method
	 * 
	 * @throws TagException
	 */
	@Test
	void iterateTest() throws TagException {
		List<Content> contents = new ArrayList<>();
		Variables variables = new Variables(new HashMap<>());
		Map<String, String> parameters;
		
		parameters = new HashMap<>();
		parameters.put(Marker.PARAM_TEST, "true");
		PairedTag tag1 = new PairedTag(Marker.TAG_IF, Marker.STATE_BEGIN, parameters);
		tag1.addContents(new Text("1"));
		contents.add(tag1);
		parameters = new HashMap<>();
		parameters.put(Marker.PARAM_TEST, "true");
		PairedTag tag2 = new PairedTag(Marker.TAG_IF, Marker.STATE_OTHER, parameters);
		tag2.addContents(new Text("2"));
		contents.add(tag2);
		parameters = new HashMap<>();
		parameters.put(Marker.PARAM_TEST, "true");
		PairedTag tag3 = new PairedTag(Marker.TAG_IF, Marker.STATE_BEGIN, parameters);
		tag3.addContents(new Text("3"));
		contents.add(tag3);
		assertEquals("13", Marker.iterate(contents, variables));
		
		tag1.getParameters().put(Marker.PARAM_TEST, "false");
		tag2.getParameters().put(Marker.PARAM_TEST, "true");
		tag3.getParameters().put(Marker.PARAM_TEST, "false");
		assertEquals("2", Marker.iterate(contents, variables));
	}
	
	/**
	 * Test the call method
	 * 
	 * @throws TagException
	 */
	@Test
	void callTest() throws TagException {
		Variables variables = new Variables(new HashMap<>());
		Map<String, String> parameters = new HashMap<>();
		parameters.put(Marker.PARAM_TEST, "true");
		PairedTag tag1 = new PairedTag(Marker.TAG_IF, Marker.STATE_BEGIN, parameters);
		Marker.call(tag1, variables);
		
		TagException ex1 = assertThrows(TagException.class, () -> Marker.call(tag1, null));
		System.err.println(ex1.getMessage());
		
		tag1.setName("error");
		TagException ex2 = assertThrows(TagException.class, () -> Marker.call(tag1, variables));
		System.err.println(ex2.getMessage());
		
		tag1.setName(Marker.TAG_IF);
		tag1.setState("error");
		TagException ex3 = assertThrows(TagException.class, () -> Marker.call(tag1, variables));
		System.err.println(ex3.getMessage());
	}
	
	/**
	 * Test the eval method
	 * 
	 * @throws TagException
	 */
	@Test
	void evalTest() throws TagException {
		Map<String, Object> var = new HashMap<>();
		Object index = 0;
		var.put("index", index);
		Object value = 5;
		var.put("length", value);
		Object array = null;
		array = Variables.putArray(array, value);
		var.put("array", array);
		Variables variable = new Variables(var);
		assertTrue((boolean) Marker.eval("length==5", variable));
		assertTrue((boolean) Marker.eval("5==length", variable));
		assertFalse((boolean) Marker.eval("length==6", variable));
		assertEquals(4, (int) Marker.eval("'aaaa'.length", variable));
		assertEquals(5, (int) Marker.eval("array[index]", variable));
		TagException ex = assertThrows(TagException.class, () -> Marker.eval("test", variable));
		System.err.println(ex.getMessage());
	}
	
	/**
	 * Test the evalObject method through the eval method
	 * 
	 * @throws TagException
	 */
	@Test
	void evalObjectTest() throws TagException {
		Variables variable = new Variables(new HashMap<>());
		assertTrue(Marker.eval("i = null", variable) instanceof Boolean);
		assertTrue(Marker.eval("i = true", variable) instanceof Boolean);
		assertTrue(Marker.eval("i = 1", variable) instanceof Integer);
		assertTrue(Marker.eval("i = 1.00", variable) instanceof Integer);
		assertTrue(Marker.eval("i = 10000000000", variable) instanceof Double);
		assertTrue(Marker.eval("i = 1.01", variable) instanceof Double);
		assertTrue(Marker.eval("i = 'test'", variable) instanceof String);
		assertTrue(Marker.eval("i = \"test\"", variable) instanceof String);
		assertTrue(Marker.eval("i = [2, 3, 5]", variable) instanceof Object[]);
		assertTrue(Marker.eval("i = []", variable) instanceof Object[]);
		assertTrue(Marker.eval("i = {id:1, text:'test'}", variable) instanceof Map);
		assertTrue(Marker.eval("i = {}", variable) instanceof Object[]);
	}
	
	/**
	 * Test the isPairedTag method
	 * 
	 * @throws TagException
	 */
	@Test
	void isPairedTagTest() {
		assertTrue(Marker.isPairedTag("if"));
		assertFalse(Marker.isPairedTag("get"));
	}
	
	/**
	 * Test the validate method
	 * 
	 * @throws TagException
	 */
	@Test
	void validateTest() throws TagException {
		TagException ex;
		Map<String, String> parameters = new HashMap<>();
		
		ex = assertThrows(TagException.class, () -> Marker.validdate("error", Marker.STATE_BEGIN, null, null));
		System.err.println(ex.getMessage());
		
		ex = assertThrows(TagException.class, () -> Marker.validdate(Marker.TAG_IF, "error", null, null));
		System.err.println(ex.getMessage());
		
		ex = assertThrows(TagException.class, () -> Marker.validdate(Marker.TAG_SET, Marker.STATE_BEGIN, parameters, null));
		System.err.println(ex.getMessage());
		
		parameters.put(Marker.PARAM_TEST, "true");
		Marker.validdate(Marker.TAG_IF, Marker.STATE_BEGIN, parameters, null);
		
		ex = assertThrows(TagException.class, () -> Marker.validdate(Marker.TAG_IF, Marker.STATE_NEITHER, parameters, "error"));
		System.err.println(ex.getMessage());
	}

}
