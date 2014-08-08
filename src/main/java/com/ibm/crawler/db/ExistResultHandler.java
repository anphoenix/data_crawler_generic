package com.ibm.crawler.db;

import com.mongodb.DBCursor;

public class ExistResultHandler implements ResultHandler{
	public boolean isExist = false;
	
	public void handle(DBCursor cursor) {
		if (cursor.hasNext()) {
			isExist = true;
		}
	}
	
}
