package org.king2.dynamicdatasource.utils;


import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  功能：默认应用工具类信息
 *  作者：刘梓江
 *  时间：2020/9/7 11:43
 */
public class ApplicationUtil {

	/**
	 * 读取json文件
	 * @param fileUrl json文件名地址
	 * @return 返回json字符串
	 */
	public static String readJsonFile(String fileUrl) {
		BufferedReader bufferedReader=null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			//读取文件内容
			bufferedReader = new BufferedReader(new FileReader(fileUrl));
			String readerContent="";
			while (!StringUtils.isEmpty(readerContent=bufferedReader.readLine())){
				//拼接文件每行内容
				stringBuilder.append(readerContent.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(bufferedReader!=null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return stringBuilder.toString();
		}
	}


	/**
	 * 获取本机外网ip
	 * @return
	 */
	public static String getHostInternetIp() {
		String ip = "";
		StringBuilder inputLine = new StringBuilder();
		String read = "";
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedReader in = null;

		try {
			url = new URL("http://ip.chinaz.com");
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			while ((read = in.readLine()) != null) {
				inputLine.append(read + "\r\n");
			}
			Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
			Matcher m = p.matcher(inputLine.toString());
			if (m.find()) {
				String ipstr = m.group(1);
				ip = ipstr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//二次尝试获取公网IP
		if (StringUtils.isEmpty(ip)) {
			try {
				URL getInternetIpUrl = new URL("http://checkip.amazonaws.com");
				in = new BufferedReader(new InputStreamReader(getInternetIpUrl.openStream()));
				ip = in.readLine(); //you get the IP as a String
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ip;
	}




	/**
	 * 将包含字符串ip替换新的ip
	 * @param replaceStrIP	包含ip的字符串
	 * @param newIP			新ip
	 * @return
	 */
	public static String replaceStrIPOnNewIp(String replaceStrIP,String newIP){
		String reg = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		String newIpStr= replaceStrIP.replaceAll(reg, newIP);
		return newIpStr;
	}


	/**
	 * 打印获取日志信息
	 * @param t
	 * @return
	 */
	public static String getExceptionMessage(Throwable t) {
		StringWriter stringWriter= new StringWriter();
		PrintWriter writer= new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer= stringWriter.getBuffer();
		return buffer.toString();
	}

}
