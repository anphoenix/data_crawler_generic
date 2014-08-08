package com.ibm.smartnurse.crawler.extractor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.smartnurse.crawler.conf.ConfConstants;
import com.ibm.smartnurse.crawler.conf.SysConf;
import com.ibm.smartnurse.crawler.util.CommonUtil;



public class ExtractInfoWithHtmlCleaner {

	private static final HtmlCleaner cleaner = new HtmlCleaner();
	public static ExtractInfoWithHtmlCleaner INSTANCE = new ExtractInfoWithHtmlCleaner();
	
	
	private ExtractInfoWithHtmlCleaner()
	{
		CleanerProperties props = cleaner.getProperties();     
        props.setUseCdataForScriptAndStyle(true);     
        props.setRecognizeUnicodeChars(true);     
        props.setUseEmptyElementTags(true);     
        props.setAdvancedXmlEscape(true);     
        props.setTranslateSpecialEntities(true);     
        props.setBooleanAttributeValues("empty");
	}
	public  ArrayList<String> getUrlListByPath(TagNode node, String xpath) throws IOException, XPatherException
	{
		 Object[] ns = node.evaluateXPath(xpath);
		 ArrayList<String> nodeList = new ArrayList<String> ();

		 for (Object object : ns) 
		 {
		    TagNode dd = (TagNode) object;
		    nodeList.add(dd.getAttributeByName("href"));
		 }
		 return nodeList;
	}
	public  String parsePageInfoByPath(TagNode node, String xpath) throws IOException, XPatherException
	{
		 Object[] ns = node.evaluateXPath(xpath);
		 String result ="";
		 for (Object object : ns) 
		 {
		    TagNode dd = (TagNode) object;
		    result = result +dd.getText();
		 }
		 return result;
	}
	public  String parsePageInfoByPathandName(TagNode node, String xpath,String name) throws IOException, XPatherException
	{
		 Object[] ns = node.evaluateXPath(xpath);
		 String result = "";
		 for (Object object : ns) 
		 {
		    TagNode dd = (TagNode) object;
		    result = result +dd.getAttributeByName(name);
		 }
		 return result;
	}
	public  String parsePageInfoByPathandIndex(TagNode node, String xpath,int index) throws IOException, XPatherException
	{
		 Object[] ns = node.evaluateXPath(xpath);
		 String result = "" ;
		 if(ns.length>0)
		 {
		    TagNode dd = (TagNode) ns[index];
		    result = result +dd.getText();
		 }
		 return result;
	}
	public  String parsePageInfoByPathandNameAndindex(TagNode node, String xpath,String name,int index) throws IOException, XPatherException
	{
		 Object[] ns = node.evaluateXPath(xpath);
		 String result = "";
		 if(ns.length>0)
		 {
		    TagNode dd = (TagNode) ns[index];
		    result = result +dd.getAttributeByName(name);
		 }
		 return result;
	}
	
	public  void getPageInfoUseHtmlCleaner(String url, JsonObject sourceConf) throws MalformedURLException, IOException, XPatherException
	{
		System.out.println("HtmlCleaner: "+url);
		if (sourceConf == null ) return ;
		String charSet = sourceConf.get(ConfConstants.SOURCE_CHARSET).getAsString();
		TagNode node = null;
		try{
			node = cleaner.clean(new URL(url),charSet);
		}catch(Exception e)
		{
			return ;
		}
		JsonObject info = sourceConf.get("info_extractor").getAsJsonObject();
		JsonObject qa = new JsonObject();
		String result = "";
		for(Entry<String, JsonElement> entry: info.entrySet())
		{
			result = ExtractInfoWithHtmlCleaner.INSTANCE.parsePageInfoByPathandIndex(node, entry.getValue().getAsString(), 0);
			if(result.length()>0)
				qa.addProperty(entry.getKey(), CommonUtil.cleanString(result));
		}
		CommonUtil.saveContent(qa.toString()+"\n", SysConf.SAVE_PATH);
	}
	public static void main(String[] args) throws IOException, XPatherException {
		CleanerProperties props = cleaner.getProperties();     
        props.setUseCdataForScriptAndStyle(true);     
        props.setRecognizeUnicodeChars(true);     
        props.setUseEmptyElementTags(true);     
        props.setAdvancedXmlEscape(true);     
        props.setTranslateSpecialEntities(true);     
        props.setBooleanAttributeValues("empty");     
        String result ="";
        File file = new File("E:/test4java/tangniaobing.htm");
        
        
        URL url = new URL("http://www.tangjk.com/dictionary/qianqi/");
		 TagNode node = cleaner.clean(url,"GBK");
		 //Object[] ns = node.getElementsByName("", true);
		 Object[] ns = node.evaluateXPath("//*[@id=\"newsREP_1_pageNumLab\"]/a[position()>1]");
		 //Object[] ns = node.("//*[@id=\"shequREP_pageNumLab\"]/a");
		 for (Object object : ns) 
		 {
		    TagNode dd = (TagNode) object;
		    result = result +dd.getAttributeByName("href")+"\n";
		 }
		 result = result.replace("&nbsp", "").replace("\r", "").replace(";", "");
		 /*result = "?uthorid=4917458&page=6&tid=16785968";
		 String rex = "\\?(?!authorid=).*";
		 Pattern p = Pattern.compile(rex);
		 Matcher m = p.matcher(result);
		 boolean s = m.matches();
		 for(int i=1;i<=m.groupCount();i++)
		 {
			 System.out.println(m.group(i));
		 }*/
		 
		 System.out.print(result);
	}
	
	public HtmlCleaner getCleaner()
	{
		return cleaner;
	}
	
	

}