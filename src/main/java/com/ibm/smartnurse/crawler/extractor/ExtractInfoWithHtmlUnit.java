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
				System.out.println("ERRORXXXXHtmlUnit:  "+url);
			pg = webClient.getPage(url);
		}
		WebWindow win = webClient.getCurrentWindow();
		win.setInnerHeight(60000);
		
		JsonObject info = sourceConf.get("info_extractor").getAsJsonObject();
		JsonObject qa = new JsonObject();
		List<?> result = null;
		for(Entry<String, JsonElement> entry: info.entrySet())
		{
			result = pg.getByXPath(entry.getValue().getAsString());
			if(result.size() > 0)
			{
				qa.addProperty(entry.getKey(), CommonUtil.cleanString(((HtmlElement) result.get(0)).getTextContent().trim()));
			}
		}
		CommonUtil.saveContent(CommonUtil.cleanString(qa.toString())+"\n", SysConf.SAVE_PATH);
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
		String url = "http://zhidao.baidu.com/question/117927477.html";
		String Xpath = "//*[@class=\"evaluate-tip\" and @id=\"52-tip\"]";
		getValueByXpath(url, Xpath);
	}
}
