package com.ibm.crawler.db;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class EntryResultHandler implements ResultHandler{
	private JsonArray array;
	public EntryResultHandler(JsonArray array){
		this.array = array;
	}
	public void handle(DBCursor cursor) {
		while (cursor.hasNext()) {
			JsonObject obj = null;
			try {
				JsonParser parser = new JsonParser();
				obj = (JsonObject) parser.parse(JSON.serialize(cursor.next()));
			} catch (MongoException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(obj != null){
				array.add(obj);
			}
			
		}
	}
	
}
