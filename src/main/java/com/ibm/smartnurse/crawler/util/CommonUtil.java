package com.ibm.smartnurse.crawler.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtil {
	
	public static void saveContent(String context, String  filename){
		File file =new File(filename);
		if(!file.exists())
			{
				try {
					file.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			try {
				FileWriter writer = new FileWriter(file,true);
				writer.append(context);
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static String cleanString(String str) {


		return str.replace("&nbsp", "").replaceAll("\r", "").replaceAll("\n", "")
				.replaceAll("\\r", "").replaceAll("\\n", "")
		.replace(";", "").replace("&mdash", "").replace("&ldquo", "\"")
		.replace("&rdquo", "\"").replace("&plusmn", "+-")
		.replace("&le", "").replace("&ge", ">=").replace("&quot", "\"")
		.replace("&bull", "?").replace("&middot", ".")
		.replace("&times", "*").replace("&ample", "")
		.replace("&ampmdash", "_").replace("&ampldquo", "")
		.replace("&amprdquo", "").replaceAll(" ", "").replace("\t", "");
		}
	public static String getDateString(String date, String rex)
	{
		date = date.replace("\r", "").replace("\n", "");
		//String rex = ".*?([0-9]+.[0-9]+.[0-9]+).*";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(date);
		boolean has = m.find();
		if(has)
		return m.group(m.groupCount());
		else return null;

	}
	public static Date stringToDateByconf(String str, String formate){
		DateFormat formater;
		formater = new SimpleDateFormat(formate);
		try {
		return (Date) formater.parse(str);
		} catch (ParseException e) {
		// TODO Auto-generated catch block
		formater = new SimpleDateFormat("yyyy-MM-dd");
		try {
		return (Date) formater.parse(str.substring(0,10));
		} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		}
		return new Date();
		}
	
	public static void main(String [] args)
	{
		System.out.println(cleanString("\r\\n\nsdfa"));
	}
}
