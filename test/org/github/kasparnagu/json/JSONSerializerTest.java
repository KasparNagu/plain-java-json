package org.github.kasparnagu.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.github.kasparnagu.json.JSONSerializer.JSONSerializerException;
import org.junit.Test;

public class JSONSerializerTest {

	@Test
	public void testSerializeJSONObject() throws JSONSerializerException {
		assertEquals("[]", JSONSerializer.serializeJSON(new ArrayList<>()));
		assertEquals("{}", JSONSerializer.serializeJSON(new HashMap<>()));
		assertEquals("1", JSONSerializer.serializeJSON(1));
		assertEquals("1100001020301", JSONSerializer.serializeJSON(1100001020301l));
		assertEquals("1.1", JSONSerializer.serializeJSON(1.1));
		assertEquals("null", JSONSerializer.serializeJSON(null));
		assertEquals("true", JSONSerializer.serializeJSON(true));
		assertEquals("false", JSONSerializer.serializeJSON(false));
		assertEquals("\"a\"", JSONSerializer.serializeJSON("a"));
		assertEquals("\"\\\\\"", JSONSerializer.serializeJSON("\\"));
		assertEquals("[1,2.2,[],{\"a\":1,\"b\":\"b\"}]", JSONSerializer.serializeJSON(Arrays.asList(1,2.2,
				new ArrayList<>(),
				new HashMap<Object,Object>(){{put("a",1);put("b","b");}})));
	}
	
	@Test(expected=JSONSerializerException.class)
	public void testSerializeJSONException1() throws JSONSerializerException {
		JSONSerializer.serializeJSON(new Throwable());
	}
	

	@Test
	public void testSerializeJSONObjectWriter() throws JSONSerializerException, IOException {
		JSONSerializer.serializeJSON(13, new Writer() {
			
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
				assertEquals(0, off);
				assertEquals(2, len);
				assertArrayEquals(new char[]{'1','3'}, Arrays.copyOf(cbuf, len));				
			}
			
			@Override
			public void flush() throws IOException {
				fail("flush");				
			}
			
			@Override
			public void close() throws IOException {
				fail("close");				
			}
		});
	}

}
