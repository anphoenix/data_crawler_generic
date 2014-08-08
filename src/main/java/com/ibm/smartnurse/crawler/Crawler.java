package com.ibm.smartnurse.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.smartnurse.crawler.conf.ConfConstants;
import com.ibm.smartnurse.crawler.extractor.ExtractInfoWithHtmlCleaner;
import com.ibm.smartnurse.crawler.extractor.ExtractInfoWithHtmlUnit;

public class Crawler {

	
	public static Crawler INSTANCE = new Crawler();
	private Crawler()
	{
		
	}
	
	public void  crawlerInfo(JsonObject interfaceConf) throws Exception
	{
		String charSet = interfaceConf.get(ConfConstants.SOURCE_CHARSET).getAsString();
		JsonObject crawl_conf = interfaceConf.get("crawl_conf").getAsJsonObject();
		String seed = crawl_conf.get(ConfConstants.SOURCE_SEED).getAsString();
		JsonArray category = crawl_conf.get(ConfConstants.SOURCE_INFO_CATEGORY).getAsJsonArray();
        String list_pattern = crawl_conf.get(ConfConstants.SOURCE_INFO_LIST_PATTERN).getAsString();
        String next_pattern = crawl_conf.get(ConfConstants.SOURCE_INFO_NEXT_PATTERN).getAsString();
        String next_type = crawl_conf.get(ConfConstants.SOURCE_INFO_NEXT_TYPE)!=null ?crawl_conf.get(ConfConstants.SOURCE_INFO_NEXT_TYPE).getAsString() :null;
        String next_url_pre = crawl_conf.has("next_url_pre") ?crawl_conf.get("next_url_pre").getAsString():"";
        
       
        for(JsonElement je : category)
        {
        	StringBuilder seed_url = new StringBuilder();
        	seed_url.append(seed).append(je.getAsString());
        	TagNode node = ExtractInfoWithHtmlCleaner.INSTANCE.getCleaner().clean(new URL(seed_url.toString()),charSet);
        	if(next_type.equals("all"))
        	{
	        	ArrayList<String>next_url = ExtractInfoWithHtmlCleaner.INSTANCE.getUrlListByPath(node, next_pattern);
	        	if(crawl_conf.has("first_dif")) next_url.add(0, "");
	        	for (String next : next_url)
	        	{
	        		next = seed_url + "/" +next ;
	        		TagNode next_node = ExtractInfoWithHtmlCleaner.INSTANCE.getCleaner().clean(new URL(next),charSet);
	        		ArrayList<String>list_url = ExtractInfoWithHtmlCleaner.INSTANCE.getUrlListByPath(next_node, list_pattern);
	        		extractInfoFromPage(list_url, seed);
	        	}
        	}
        	else if (next_type.equals("next"))
        	{
        		String newUrl = null;
        		do
        		{
        			ArrayList<String>list_url = ExtractInfoWithHtmlCleaner.INSTANCE.getUrlListByPath(node, list_pattern);
    	        	
        			extractInfoFromPage( list_url,"");
	        		newUrl = ExtractInfoWithHtmlCleaner.INSTANCE.parsePageInfoByPathandNameAndindex(node, next_pattern, "href", 0);
        			node = ExtractInfoWithHtmlCleaner.INSTANCE.getCleaner().clean(new URL(next_url_pre+newUrl),charSet);
        			
        		}while(!newUrl.equals(""));
        		
        	}
        }
		
	}
	public void extractInfoFromPage(ArrayList<String> list_url, String seed) throws MalformedURLException, IOException, XPatherException
	{
		Map<String, Object> parameters = new HashMap<String ,Object>();
		for(String url : list_url)
		{
			parameters.put("url", seed+url);
			//if (LinkManager.isExistUser(parameters)) continue;
			JsonObject conf = getConfByURL(seed+url);
			if(conf!=null && conf.has("htmlunit"))
				ExtractInfoWithHtmlUnit.INSTANCE.getPageInfoUseHtmlUnit(seed+url, conf);
			else 
				ExtractInfoWithHtmlCleaner.INSTANCE.getPageInfoUseHtmlCleaner(seed+url, conf);
			JsonObject link = new JsonObject();
			link.addProperty("url", url);
			//LinkManager.insertLink(link);
		}
	}
	public  JsonObject getConfByURL(String url)
	{
		JsonArray confArray = CrawlerManage.INSTANCE.getSourceArray().getAsJsonArray();
		for(JsonElement jo : confArray)
		{
			String site = jo.getAsJsonObject().get(ConfConstants.SOURCE_SITE).getAsString();
			if(url.contains(site))
			{
				return jo.getAsJsonObject();
			}
		}
		return null;
	}
}
