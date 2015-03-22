package org.github.kasparnagu.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class JSONSerializer {
	public static class JSONSerializerException extends Exception {
		private static final long serialVersionUID = -4942348678107203496L;

		public JSONSerializerException(String cause) {
			super(cause);
		}
	}
	public static String serializeJSON(Object o) throws JSONSerializerException{
		StringWriter wrt = new StringWriter();
		try {
			serializeJSON(o,wrt);
		} catch (IOException e) {
			throw new JSONSerializerException("IOException during serialization");
		}
		return wrt.toString();
	}
	public static void serializeJSON(Object o, Writer w) throws JSONSerializerException, IOException {
		if(o == null){
			w.write("null");
		}else if(o instanceof String){
			w.write("\"");
			w.write(((String)o).replace("\\","\\\\").replace("\"", "\\\""));
			w.write("\"");			
		}else if(o instanceof Integer){
			w.write(Integer.toString((Integer)o));
		}else if(o instanceof Long){
			w.write(Long.toString((Long)o));
		}else if(o instanceof Double){
			w.write(Double.toString((Double)o));
		}else if(o instanceof Boolean){
			w.write(Boolean.toString((Boolean)o));
		}else if(o instanceof List){
			boolean first = true;
			w.write("[");
			for(Object l:(List<?>)o){
				if(first){
					first = false;
				}else{
					w.write(",");
				}
				serializeJSON(l, w);
			}
			w.write("]");
		}else if(o instanceof Map){
			boolean first = true;
			w.write("{");
			for(Map.Entry<?,?> l:((Map<?,?>)o).entrySet()){
				if(first){
					first = false;
				}else{
					w.write(",");
				}
				serializeJSON(l.getKey(), w);
				w.write(":");
				serializeJSON(l.getValue(), w);
			}
			w.write("}");
		}else{
			throw new JSONSerializerException("Can not serialize type:"+o.getClass().getSimpleName());
		}
	}
	
}
