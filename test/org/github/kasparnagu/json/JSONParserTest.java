package org.github.kasparnagu.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.github.kasparnagu.json.JSONParser.JSONParseException;
import org.junit.Test;

public class JSONParserTest {

	@Test
	public void testParseJSONFile() throws JSONParseException, IOException {
		Object o = JSONParser.parseJSONFile(JSONParserTest.class.getResource("test.json").getFile());
		assertEquals(6,((List)o).size());
		assertEquals("abae9624-c753-4773-ae48-4f165cd6500b",((Map)((List)o).get(5)).get("guid"));
	}

	@Test
	public void testParseJSONString() throws JSONParseException {
		Object  o = JSONParser.parseJSON("{\"k\":[1,2.2,{\"a\":2e+1},\"\\\"be\\\"\",false,true,null],\"j\":[],\"l\":{}}");
		assertTrue(o instanceof Map<?, ?>);
		Map<?,?> m = (Map<?,?>)o;
		assertTrue(m.containsKey("k"));
		assertTrue(m.get("k") instanceof List<?>);
		List<?> l = (List<?>) m.get("k");
		assertEquals(1, (long)(Long)l.get(0));
		assertEquals(2.2, (Double)l.get(1),1e-10);
		assertTrue(l.get(2) instanceof Map);
		Map<?,?> m2 = (Map<?,?>)l.get(2);
		assertTrue(m2.containsKey("a"));
		assertEquals(20.0, (Double)m2.get("a"),1e-10);
		assertEquals("\\\"be\\\"", l.get(3));
		assertFalse((Boolean)l.get(4));
		assertTrue((Boolean)l.get(5));
		assertNull(l.get(6));
		assertEquals(7, l.size());
		assertTrue(m.containsKey("j"));
		assertTrue(m.get("j") instanceof List);
		assertEquals(0,((List)m.get("j")).size());
		assertTrue(m.containsKey("l"));
		assertTrue(m.get("l") instanceof Map);
		assertEquals(0,((Map)m.get("l")).size());
		
		
	}
	
	@Test(expected=JSONParseException.class)
	public void testParseJSONStringException1() throws JSONParseException {
		Object  o = JSONParser.parseJSON("{");
	}

	@Test(expected=JSONParseException.class)
	public void testParseJSONStringException2() throws JSONParseException {
		Object  o = JSONParser.parseJSON("[}");
	}

	@Test(expected=JSONParseException.class)
	public void testParseJSONStringException3() throws JSONParseException {
		Object  o = JSONParser.parseJSON("[1}");
	}

	@Test(expected=JSONParseException.class)
	public void testParseJSONStringException4() throws JSONParseException {
		Object  o = JSONParser.parseJSON("{2}");
	}

	@Test(expected=JSONParseException.class)
	public void testParseJSONStringException5() throws JSONParseException {
		Object  o = JSONParser.parseJSON("\"ja");
	}

	
	@Test
	public void testParseJSONPath() throws JSONParseException, IOException, URISyntaxException {
		Object o = JSONParser.parseJSON(Paths.get(JSONParserTest.class.getResource("test.json").toURI()));
		assertEquals(6,((List)o).size());
		assertEquals(527,((String)((Map)((List)o).get(5)).get("about")).length());
	}

	@Test
	public void testParseJSONScanner() throws JSONParseException {
		Scanner s = new Scanner("[\"a\",1,1.1,{\"a\":2}]");		
		Object o = JSONParser.parseJSON(s);
		assertTrue(o instanceof List);
		List<?> l = (List)o;
		assertEquals(4, l.size());
		assertEquals("a", l.get(0));
		assertEquals(1, (long)(Long)l.get(1));
		assertEquals(1.1, (Double)l.get(2),1e-10);
		assertTrue(l.get(3) instanceof Map);
		Map<?,?> m = (Map<?,?>)l.get(3);
		assertEquals(1,m.size());
		assertEquals(2,(long)(Long)m.get("a"));
		
	}

}
