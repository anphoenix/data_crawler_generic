package com.ibm.smartnurse.crawler.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class ConfManagerUtil {
	
	public static JsonArray loadFromFile(String Name, String filePath){
		JsonArray array = null;
		try {
			if(filePath == null){
				JsonReader reader = new JsonReader(new InputStreamReader(ConfManagerUtil.class.getResourceAsStream(Name + ".json"), "utf-8"));
				JsonParser parser = new JsonParser();
			    array = (JsonArray)parser.parse(reader);
			} else {
				JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(new File(filePath)), "utf-8"));
				JsonParser parser = new JsonParser();
			    array = (JsonArray)parser.parse(reader);
			}			
			return array;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
