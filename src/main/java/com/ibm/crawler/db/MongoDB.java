package com.ibm.crawler.db;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.BSONObject;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class MongoDB {
	
	private static DB db;
	
	private static String databaseName = "links"; 
	private static String host = "9.119.148.179";
	private static String port = "27017";
	private static String username = "tangxiaohu";//用户名(api key);
	private static String password = "passw0rd";//密码(secret key)
	private static String serverName = host + ":" + port;
	private MongoClient mongoClient = null;
	/**
	 * init mongo client as the singleton
	 */
	public MongoDB(){
		
		try{
	        mongoClient = new MongoClient(new ServerAddress(serverName),
	        		Arrays.asList(MongoCredential.createMongoCRCredential(username, databaseName,password.toCharArray())),
	        		new MongoClientOptions.Builder().cursorFinalizerEnabled(false).build());
	        
	        db = mongoClient.getDB(databaseName);
	        
	        db.authenticate(username, password.toCharArray());
	        
        }
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * get collection in mongo db 
	 * @param db
	 * @param collName
	 * @return
	 */
	public void close()
	{
		mongoClient.close();
	}
	public DBCollection getCollection(DB db, String collName){
		return db.getCollection(collName);
	}

	/**
	 * get collection in mongo db 
	 * @param db
	 * @param collName
	 * @return
	 */
	public DBCollection getCollection(String collName){
		return db.getCollection(collName);
	}
	/**
	 * insert one record in document in specified collection
	 * @param col: collection instance
	 * @param JSONObject
	 */
	public int insertDocument(DBCollection col, JsonObject JsonObject){
		Object obj = JSON.parse(JsonObject.toString());
		DBObject dbObj = (DBObject) obj;
		//System.out.println("insert to DB:"+dbObj.toString());
		WriteResult result = col.insert(dbObj);
		return result.getN();
	}
	/**
	 * insert one record in document in specified collection
	 * @param col: collection instance
	 * @param JSONObject
	 */
	public int insertDocument(DBCollection col, BSONObject BSONObject){
		/*Object obj = JSON.parse(JSONObject.toString());
		DBObject dbObj = (DBObject) obj;*/
		BasicDBObject dbObj = new BasicDBObject();
		dbObj.putAll(BSONObject);
		WriteResult result = col.insert(dbObj);
		return result.getN();
	}
	
	/**
	 * execute certain query on a db collection
	 * @param col: collection instance
	 * @param parameters: a map object, key is the json key, value is object
	 * @param handler: pass the result db cursor as a handler
	 */
	public void executeQuery(DBCollection col, Map<String, Object> parameters, JsonObject result){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.putAll(parameters);
		
		DBCursor cursor = col.find(searchQuery);

		// loop over the cursor and display the result
		while (cursor.hasNext()) {
			try {
				@SuppressWarnings("unchecked")
				Map<Object,Object>temp = cursor.next().toMap();
				for(Entry<Object,Object> entry : temp.entrySet())
				{
					result.addProperty(entry.getKey().toString(),entry.getValue().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public boolean isExsit(DBCollection col, Map<String, Object> parameters){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.putAll(parameters);
		
		DBCursor cursor = col.find(searchQuery);
		return cursor.count()>0;
	}
	public void executeOrderByPubDateQuery(DBCollection col, Map<String, Object> parameters, ResultHandler handler){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.putAll(parameters);
		DBObject pub = new BasicDBObject();
		pub.put("date", -1);
		DBCursor cursor = col.find(searchQuery).sort(pub);

		// loop over the cursor and display the result
		handler.handle(cursor);
	}
	
	/**
	 * execute certain query on a db collection
	 * @param col: collection instance
	 * @param parameters: a map object, key is the json key, value is object
	 * @param handler: pass the result db cursor as a handler
	 */
	public void executeIndexQuery(DBCollection col, Map<String, Object> parameters, ResultHandler handler, int start, int limitNum ){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.putAll(parameters);
		DBObject pub = new BasicDBObject();
		pub.put("date", -1);
		DBCursor cursor = col.find(searchQuery).sort(pub).skip((start-1)*limitNum).limit(limitNum);

		// loop over the cursor and display the result
		handler.handle(cursor);
	}
	
	public long getTotalCount(DBCollection col, Map<String, Object> parameters ){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.putAll(parameters);
		
		return col.getCount(searchQuery);
	}
	
	public int executeUpdate(DBCollection col, Map<String, Object> updateValue, Map<String, Object> conditionValue){
		BasicDBObject updates = new BasicDBObject();
		updates.putAll(updateValue);
		BasicDBObject updateSetValue=new BasicDBObject("$set",updates);  
		BasicDBObject conditions = new BasicDBObject();
		conditions.putAll(conditionValue);
		WriteResult result = col.update(conditions, updateSetValue);  
		return result.getN();
	}
	
	public static void main(String [] args)
	{
		//DBCollection dc = MongoDB.INSTANCE.getCollection("user_profile");
		MongoDB mb = new MongoDB();
		
		DBCollection dc = mb.getCollection("link");
		JsonObject jo = new JsonObject();
		jo.addProperty("a", "A");
		mb.insertDocument(dc, jo);
		mb.close();
		//MongoDB.INSTANCE.insertDocument(dc, jo);
	}
}
