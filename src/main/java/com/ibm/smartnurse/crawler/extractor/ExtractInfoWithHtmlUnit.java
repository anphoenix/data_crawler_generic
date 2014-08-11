package com.ibm.smartnurse.crawler.extractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map.Entry;

import org.htmlcleaner.XPatherException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.smartnurse.crawler.conf.ConfConstants;
import com.ibm.smartnurse.crawler.conf.SysConf;
import com.ibm.smartnurse.crawler.util.CommonUtil;

public class ExtractInfoWithHtmlUnit {
	private static WebClient webClient = new WebClient();
	public static ExtractInfoWithHtmlUnit INSTANCE = new ExtractInfoWithHtmlUnit();
	private ExtractInfoWithHtmlUnit()
	{
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		//webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
	    //webClient.getOptions().setDoNotTrackEnabled(true);
	    webClient.getOptions().setTimeout(300000);
		webClient.waitForBackgroundJavaScript(5000);
	}
	public WebClient getWebClient()
	{
		return webClient;
	}
	public void getPageInfoUseHtmlUnit(String url, JsonObject sourceConf) throws MalformedURLException, IOException, XPatherException
	{
		System.out.println("HtmlUnit:  "+url);
		webClient.closeAllWindows();
		HtmlPage pg = null;
		try{
			pg = webClient.getPage(url);
		}catch(FailingHttpStatusCodeException exc){
			if(503 == exc.getStatusCode())
			{	System.out.println("ERRORXXXXHtmlUnit:  "+url);
				try{
				pg = webClient.getPage(url);
				} catch(Exception e)
				{
					return;
				}
			}
			return ;
		}
		WebWindow win = webClient.getCurrentWindow();
		win.setInnerHeight(60000);
		
		JsonObject info = sourceConf.get("info_extractor").getAsJsonObject();
		String save_name = sourceConf.get(ConfConstants.SOURCE_SAVE_NAME).getAsString();
		JsonObject qa = new JsonObject();
		List<?> temp = null;
		for(Entry<String, JsonElement> entry: info.entrySet())
		{
			temp = pg.getByXPath(entry.getValue().getAsString());
			String result = "";
			if(temp.size() > 0)
			{
				result = ((HtmlElement) temp.get(0)).getTextContent().trim();
				if(entry.getKey().equals(ConfConstants.SOURCE_INFO_DATE))
				{
					result = CommonUtil.getDateString(result,".*?([0-9]+.[0-9]+.[0-9]+).*");
//					String format = sourceConf.get(ConfConstants.SOURCE_DATE_FORMAT).getAsString();
//					result = (CommonUtil.stringToDateByconf(result, format)).toString();
				}
				qa.addProperty(entry.getKey(), CommonUtil.cleanString(result));
			}
		}
		qa.addProperty(ConfConstants.SOURCE_URL, url);
		if(qa.has(ConfConstants.SOURCE_INFO_REPLY_TITLE)||qa.has(ConfConstants.SOURCE_INFO_REPLY_CONTENT))
			CommonUtil.saveContent(CommonUtil.cleanString(qa.toString())+"\n", SysConf.SAVE_PATH+save_name+".txt");
	}
	
	public static  void getValueByXpath(String url, String Xpath) throws MalformedURLException, IOException, XPatherException
	{
		webClient.closeAllWindows();
		HtmlPage pg = null;
		try{
			pg = webClient.getPage(url);
		}catch(FailingHttpStatusCodeException exc){
			if(503 == exc.getStatusCode())
				System.out.println("ERRORXXXXHtmlUnit:  "+url);
			pg = webClient.getPage(url);
		}
		WebWindow win = webClient.getCurrentWindow();
		win.setInnerHeight(60000);
		List<?> result = null;
		result = pg.getByXPath(Xpath);
		for (int i=0; i<result.size();i++)
		System.out.println("qqq"+((HtmlElement) result.get(i)).getTextContent().trim());

	}
	
	public static void main(String [] args) throws MalformedURLException, IOException, XPatherException
	{
		String url = "http://zhidao.baidu.com/question/411854996.html";
		String Xpath = "//*[@class=\"browse-times\"]";
		getValueByXpath(url, Xpath);
	}
}
