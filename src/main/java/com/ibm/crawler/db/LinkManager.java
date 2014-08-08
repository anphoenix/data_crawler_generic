package com.ibm.crawler.db;

import java.util.Map;

import com.google.gson.JsonObject;
import com.mongodb.DBCollection;

public class LinkManager {

	public static void insertLink(JsonObject link)
	{
		MongoDB mb = new MongoDB();
		
		DBCollection dc = mb.getCollection("link");
		mb.insertDocument(dc, link);
		mb.close();
	}
	
	public static boolean isExistUser( Map<String, Object> parameters)
	{
		MongoDB mb = new MongoDB();
		DBCollection dc = mb.getCollection("link");
		boolean result = mb.isExsit(dc, parameters);
		mb.close();
		return result;
	}
	
	public static void UpdateLink( Map<String, Object> updateValue, Map<String, Object> conditionValue)
	{
		MongoDB mb = new MongoDB();
		DBCollection dc = mb.getCollection("link");
		mb.executeUpdate(dc, updateValue, conditionValue);
		mb.close();
	}
	
}
