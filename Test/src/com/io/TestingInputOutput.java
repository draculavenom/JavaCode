package com.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import com.utilities.PrintingUtility;

public class TestingInputOutput implements PrintingUtility{
	public void test1() {
		println("Reading files using Scanner");
		try {
			File file = new File("TestingInputOutput.java");
			file = new File(file.getAbsolutePath());
			File[] matchingFiles = null;
			
			int i = 0;
			while((matchingFiles == null || matchingFiles.length == 0) && i<5) {
				matchingFiles = file.listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        //return name.startsWith("temp") && name.endsWith("txt");
				    	return name.endsWith("txt");
				    }
				});
				File[] documentFiles = file.listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.startsWith("Google");
				    }
				});
				i++;

				if((documentFiles != null && documentFiles.length > 0)) {
					file = documentFiles[0];
					System.out.println(file);
				}else if((matchingFiles == null || matchingFiles.length == 0) && file.getParent() != null && file.getParent() != "null") {
					file = new File(file.getParent());
					System.out.println(file);
				}
			}
			if(matchingFiles != null && matchingFiles.length > 0) {
				File txtFile = matchingFiles[0];
				Scanner input = new Scanner(txtFile);
				while(input.hasNextLine()) {
					println(input.nextLine());
				}
				input.close();
				println(Arrays.toString(matchingFiles));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test2(){
		println("Reading files using java 6 features");
		try {
			File file = new File("C:\\Users\\paqui\\Google Drive\\lugares para visitar.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line = bufferedReader.readLine();
			while(line != null) {
				println(line);
				line = bufferedReader.readLine();
			}
			
			bufferedReader.close();
			fileReader.close();
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test3() {
		println("Reading files using java 7 features, try with resources");
		File file = new File("C:\\Users\\paqui\\Google Drive\\lugares para visitar.txt");
		
		try(FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader);){
			String line = bufferedReader.readLine();
			while(line != null) {
				println(line);
				line = bufferedReader.readLine();
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
