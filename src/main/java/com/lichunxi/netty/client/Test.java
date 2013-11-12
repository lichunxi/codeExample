package com.lichunxi.netty.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static List<String> readFileByLines(String fileName) {
		System.out.println(fileName);
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			List<String> urls = new ArrayList<String>();
			// int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {

				urls.add(tempString);
			}

			reader.close();

			return urls;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		List<String> urls = readFileByLines("F:\\alibaba\\cms\\rule-core\\src\\main\\java\\com\\aliyun\\cms\\linkList.txt");
		
		if (null == urls){
			System.out.println("can't get links");
			return;
		}
		
		HttpClient client = new HttpClient();

		System.out.println("start:" + System.currentTimeMillis());
		for (int i = 0; i < 10000; i++) {
			double indexRandom = Math.random() * urls.size();

			int index = (int) indexRandom;
			try {
				URI uri = new URI(urls.get(index));
				client.dispatch(uri);
			} catch (URISyntaxException e) {
				//e.printStackTrace();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		System.out.println("Done.");
	}

}
