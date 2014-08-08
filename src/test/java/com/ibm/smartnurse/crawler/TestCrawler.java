package com.ibm.smartnurse.crawler;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonObject;

public class TestCrawler {

	@Test
	public void testGetInfoFromTJK() throws Exception {
		CrawlerManage.INSTANCE.crawler();
	}

	@Test
	public void testGetPageInfo() {
		
	}

}
