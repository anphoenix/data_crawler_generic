package com.ibm.smartnurse.crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.ibm.smartnurse.crawler.conf.ConfManagerUtil;

public class CrawlerManage {

	private static JsonArray sourceArray = null;
	private static JsonArray InterfaceArray = null;
	public static CrawlerManage INSTANCE = new CrawlerManage();
	private CrawlerManage(){
		sourceArray =  ConfManagerUtil.loadFromFile("sources", null);
		InterfaceArray =  ConfManagerUtil.loadFromFile("interface", null);
	}
	public JsonArray getSourceArray()
	{
		return sourceArray;
	}
	public JsonArray getInterfaceArray()
	{
		return InterfaceArray;
	}
	public void crawler()
	{
		for (JsonElement je : InterfaceArray)
		{
			try {
				Crawler.INSTANCE.crawlerInfo(je.getAsJsonObject());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
