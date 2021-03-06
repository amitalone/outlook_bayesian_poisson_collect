package com.alcance.stdNegativeCollector;

import java.io.FileWriter;
import java.util.List;

public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			System.out.println("Program requires username & Password");
			System.exit(0);
		} else {
			String userName = args[0];
			String password = args[1];
			List<String> words =  new MailReader().readMails(userName, password);
			writeFile(words);
			System.out.println("Negatives generated in poission.txt");
		}
	 
	}
	
	private static void  writeFile(List<String> words) throws Exception{
		FileWriter writer = new FileWriter("poission.txt");
		for(String word : words) {
			writer.write(word+"\n");
		}
		writer.close();
	}
	
}
