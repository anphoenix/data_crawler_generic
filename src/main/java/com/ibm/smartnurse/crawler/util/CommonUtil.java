package com.ibm.smartnurse.crawler.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CommonUtil {
	
	public static void saveContent(String context, String  filename){
		File file =new File(filename);
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

	public static void main(String [] args)
	{
		System.out.println(cleanString("\r\\n\nsdfa"));
	}
}
