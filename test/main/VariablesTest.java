package main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Variables;
import exception.TagException;

/**
 * JUnit Variables class
 * 
 * @author Nicolas Amiot
 */
class VariablesTest {
	
	/**
	 * Variables class
	 */
	private Variables variables;

	/**
	 * Initializes the variables
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		Map<String, Object> map = new HashMap<>();
		Object object1 = "aaa";
		Object object2 = "bbb";
		map.put("test1", object1);
		map.put("test2", object2);
		variables = new Variables(map);
	}
	
	/**
	 * Test the putObject method
	 * 
	 * @throws TagException
	 */
	@Test
	void putObjectTest() throws TagException {
		Map<String, Object> object = Variables.putObject(null, null, null);
		assertEquals(0, object.size());
		object = Variables.putObject(object, "aaa", "aaa");
		assertEquals(1, object.size());
		object = Variables.putObject(object, "bbb", "bbb");
		assertEquals(2, object.size());
	}
	
	/**
	 * Test the putArray method
	 * 
	 * @throws TagException
	 */
	@Test
	void putArrayTest() throws TagException {
		Object[] array = Variables.putArray(null);
		assertEquals(0, array.length);
		array = Variables.putArray(array, "aaa");
		assertEquals(1, array.length);
		array = Variables.putArray(array, "bbb");
		assertEquals(2, array.length);
	}

	/**
	 * Test the set method with the temporary parameter equals to false
	 * 
	 * @throws TagException
	 */
	@Test
	void setNotTemporaryValueTest() throws TagException {
		TagException ex1 = assertThrows(TagException.class, () -> variables.set("test1", null, false));
		System.err.println(ex1.getMessage());
		
		TagException ex2 = assertThrows(TagException.class, () -> variables.set("123soleil", null, false));
		System.err.println(ex2.getMessage());
		
		assertFalse(variables.contains("test3"));
		variables.set("test3", null, false);
		assertTrue(variables.contains("test3"));
	}
	
	/**
	 * Test the set method with the temporary parameter equals to true
	 * 
	 * @throws TagException
	 */
	@Test
	void setTemporaryValueTest() throws TagException {
		variables.set("test3", null, false);
		assertTrue(variables.contains("test3"));
		
		variables.openBlock();
		
		Object object1 = "aaa";
		variables.set("test3", object1, true);
		assertTrue(variables.contains("test3"));
		assertEquals("aaa", variables.get("test3"));
		
		variables.set("test4", null, true);
		assertTrue(variables.contains("test4"));
		
		Object object2 = "bbb";
		variables.set("test4", object2, true);
		assertTrue(variables.contains("test4"));
		assertEquals("bbb", variables.get("test4"));
		
		variables.closeBlock();
		
		assertFalse(variables.contains("test4"));
		assertTrue(variables.contains("test3"));
	}

}
