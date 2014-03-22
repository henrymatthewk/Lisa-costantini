package preprocess;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import btree.Btree;

import utility.*;
import preprocess.*;

public class RecordPreprocessor {
	public static String originFolder=DataHandler.originFolder;
	public static String preprocessFolder=DataHandler.preprocessFolder;
	//fileList after preprocess
	public static ArrayList<String> fileList;
	//depth threshold
	public static int depthThreshold=5;

	public RecordPreprocessor(){
	}
	
	public static void splitFile() throws IOException{
		System.out.print("Preprocessing...");
		//init
		DataHandler.cleanDir(preprocessFolder);
		ArrayList<String> originFileList=DataHandler.getFileList(DataHandler.originFolder);
		//br for original file list, indicated by fileTag. 
		BufferedReader obr=null;
		//pw for wrting into new file
		PrintWriter pw=null;
		
		String line;
		for(int i=0;i<originFileList.size();i++){
			obr=getBr(originFileList.get(i));
			obr.readLine();//discard 1st line
			while((line=obr.readLine())!=null){
				line=line.replaceAll(";", "");
				line=line.replaceAll(" ", "");
				String prefix=null;
				prefix=hashFile(line);
				String fileName=preprocessFolder+prefix+".txt";
				pw=getPw(fileName);
				pw.println(line);
				pw.close();
			}
		}
		obr.close();
		fileList=DataHandler.getFileList(preprocessFolder);
		System.out.println("Done");
	}
	
	public static String hashFile(String name){
		String prefix="";
		for(int j=0;j<depthThreshold && j<name.length();j++){
			prefix=prefix+"_"+hashNumber(name.charAt(j));
			//System.out.println(hashNumber(name.charAt(j)));
		}
		return prefix;
	}
	
	//return a br for a given fileName
	public static BufferedReader getBr(String fileName) throws FileNotFoundException{
		return new BufferedReader(new FileReader(fileName));
	}
	
	//return a pw for a given fileName
	public static PrintWriter getPw(String fileName) throws IOException{
		return new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
	}
	
	
	//based on a char value of an index of the user name to locate its preprocessed file number	
	public static String hashNumber(char x){
		if(x>='a'){
			return Integer.toString(x-'a');
		}
		else{
			return Integer.toString(x-'A');
		}
	}
	
	public static String recordFinder(String name) throws IOException{
		name=name.replaceAll(" ", "");
		String prefix=RecordPreprocessor.hashFile(name);
		
		String fileName=preprocessFolder+prefix+".txt";
		if(!fileList.contains(fileName)){
			return null;
		}
		System.out.println("Located in file "+fileName+"...");
		//Btree btree=DataHandler.buildBtree(fileName);
		BufferedReader br=getBr(fileName);
		String line;
		while((line=br.readLine())!=null){
			String[] s=line.split(",");
			if(s[0].equals(name)){
				return s[1];
			}
		}
		return null;
		//return btree.search(name);
	}
	
	/*
	public static void main(String[] args){
		System.out.println(hashFile("a"));
	}
	*/
	
}
