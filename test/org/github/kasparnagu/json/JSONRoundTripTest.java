package org.github.kasparnagu.json;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.github.kasparnagu.json.JSONParser.JSONParseException;
import org.github.kasparnagu.json.JSONSerializer.JSONSerializerException;
import org.junit.Test;

public class JSONRoundTripTest {

	Object generateRandomObject(Random rnd, int maxDepth){
		return generateRandomObject(rnd.nextInt(maxDepth>0?7:5),rnd,maxDepth);
	}
	Object generateRandomObject(int choice, Random rnd, int maxDepth){
		switch(choice){
			case 0:
				return null;
			case 1:
				return rnd.nextBoolean();
			case 2:
				return rnd.nextLong();
			case 3:
				return rnd.nextDouble();				
			case 4:
			return randomString(rnd);
			case 5:
				return Stream.generate(()->generateRandomObject(rnd,maxDepth-1))
						.limit(rnd.nextInt(30))
						.collect(Collectors.toList());
			case 6:				
				Iterator<String> distinctKeys = Stream.generate(
						()->randomString(rnd)).distinct().iterator();
				return Stream.generate(()->generateRandomObject(rnd, maxDepth-1))
						.filter(a->a!=null)
						.limit(rnd.nextInt(30))
						.collect(Collectors.toMap(
								a->distinctKeys.next(), 
								a->a));
		}

		return null;
	}
	private String randomString(Random rnd) {
		int len = rnd.nextInt(100);
		return Stream.generate(()->rnd.nextInt(64)+'0')
				.limit(rnd.nextInt(100)).map(v->String.valueOf((char)(int)v))
				.collect(Collectors.joining());
	}
	private void assertEqualObjects(Object o1, Object o2){
		if(o1 == null){
			assertNull(o2);
			return;
		}
		assertEquals(o1.getClass(),o2.getClass());
		if(o1 instanceof List){
			for(int i=0;i<((List)o1).size();i++){
				assertEqualObjects(((List)o1).get(i), ((List)o2).get(i));
			}
		}else if(o1 instanceof Map){
			for(Object k:((Map)o1).keySet()){
				assertTrue(k + " not in map",((Map)o2).containsKey(k));
				assertEqualObjects(((Map)o1).get(k), ((Map)o2).get(k));				
			}
		}else{
			assertEquals(o1, o2);
		}
	}
	
	@Test
	public void test1() throws JSONSerializerException, JSONParseException {
		for(int i=0;i<100;i++){
			Object o1 = generateRandomObject((i&1)==0?5:6,new Random(i),2);
			String json = JSONSerializer.serializeJSON(o1);
			Object o2 = JSONParser.parseJSON(json);
			assertEqualObjects(o1, o2);
		}
	}

}
