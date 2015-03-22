package org.github.kasparnagu.json;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class JSONParser {
	public static class JSONParseException extends Exception {
		private static final long serialVersionUID = 7078320816065460L;

		public JSONParseException(String cause) {
			super(cause);
		}
	}
	public static Object parseJSONFile(String file) throws JSONParseException, IOException{
		return parseJSON(Paths.get(file));
	}
	public static Object parseJSON(String text) throws JSONParseException{
		return parseJSON(new Scanner(text));
	}
	public static Object parseJSON(Path file) throws JSONParseException, IOException{
		return parseJSON(new Scanner(file));
	}
	public static Object parseJSON(Scanner s) throws JSONParseException{
		Object ret = null;
		skipWhitespace(s);
		if(s.findWithinHorizon("\\{", 1) != null){
			HashMap<Object, Object> retMap = new HashMap<>();
			ret = retMap;
			skipWhitespace(s);
			if(s.findWithinHorizon("\\}", 1) == null){
				while(s.hasNext()){
					Object key = parseJSON(s);
					skipWhitespace(s);
					if(s.findWithinHorizon(":", 1) == null){
						throw new JSONParseException("Expected :");
					}
					Object value = parseJSON(s);
					retMap.put(key, value);
					skipWhitespace(s);
					if(s.findWithinHorizon(",", 1)== null){
						break;
					}
				}			
				if(s.findWithinHorizon("\\}", 1) == null){
					throw  new JSONParseException("Expected }");
				}
			}
		}else if(s.findWithinHorizon("\"", 1) != null){
			ret = s.findWithinHorizon("(\\\\\\\\|\\\\\"|[^\"])*",0);
			if(s.findWithinHorizon("\"", 1) == null){
				throw  new JSONParseException("Expected quote");				
			}
		}else if(s.findWithinHorizon("'", 1) != null){
			ret = s.findWithinHorizon("(\\\\\\\\|\\\\'|[^'])*",0);
			if(s.findWithinHorizon("'", 1) == null){
				throw  new JSONParseException("Expected quote");				
			}		
		}else if(s.findWithinHorizon("\\[", 1) != null){
			ArrayList<Object> retList = new ArrayList<>();
			ret = retList;
			skipWhitespace(s);
			if(s.findWithinHorizon("\\]", 1) == null){
				while(s.hasNext()){
					retList.add(parseJSON(s));
					skipWhitespace(s);
					if(s.findWithinHorizon(",", 1)== null){
						break;
					}
				}
				if(s.findWithinHorizon("\\]", 1) == null){
					throw  new JSONParseException("Expected , or ]");
				}
			}
		}else if(s.findWithinHorizon("true",4) != null){
			ret = true;
		}else if(s.findWithinHorizon("false",5) != null){
			ret = false;
		}else if(s.findWithinHorizon("null",4) != null){
			ret = null;
		}else{
			String numberStart = s.findWithinHorizon("[-0-9+eE]", 1);
			if(numberStart != null){
				String numStr = numberStart + s.findWithinHorizon("[-0-9+eE.]*", 0);
				if(numStr.contains(".") | numStr.contains("e")){
					ret = Double.valueOf(numStr);
				}else{
					ret = Long.valueOf(numStr);
				}
			}else{
				throw new JSONParseException("No JSON value found");
			}
		}
		return ret;
	}
	private static void skipWhitespace(Scanner s) {
		s.findWithinHorizon("\\s*", 0);
	}
}
