package com.ibm.crawler.db;

import com.mongodb.DBCursor;

public interface ResultHandler {
	public void handle(DBCursor cursor);
	//while (cursor.hasNext()) {
//		System.out.println(cursor.next());
//	}
}
