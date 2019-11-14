package at.dalex.grape.script;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONReader {
	
	private JSONObject root;

	public JSONReader(InputStream inputStream) {

		JSONParser parser = new JSONParser();
		try {
			try {
				root = (JSONObject) parser.parse(new InputStreamReader(inputStream));
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getRootElement() {
		return this.root;
	}
	
}
