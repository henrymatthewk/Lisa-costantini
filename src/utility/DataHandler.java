package utility;

import java.io.*;
import java.util.ArrayList;
import preprocess.*;
import btree.*;
import preprocess.*;
import java.lang.Object;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DataHandler {
	public static String originFolder="./resources/origin/";
	public static String preprocessFolder="./resources/preprocess/";
	public static int threshold=500;
	public static int btreeOrder=100;
	public static int bufferedTreeNumber=20;

	//sort ascendly the MyRecord ArrayList by record names
	public static void sortMyRecord(ArrayList<MyRecord> myrecord){
		for(int i=1;i<myrecord.size();i++){
		  for(int j=i-1;j>=0;j--){
			  if(myrecord.get(j).getName().compareTo(myrecord.get(i).getName())>0){
				  MyRecord tmp=null;
				  tmp=myrecord.get(j);
				  myrecord.set(j, myrecord.get(i));
				  myrecord.set(i,tmp);
			  }
		  }
		}
	}

	//get file numbers in a folder
	public static int getFileNumber(String folderPath){
		File folder=new File(folderPath);
		return folder.list().length;
	}
	//get fileList of a folder
	public static ArrayList<String> getFileList(String folderPath){
		File folder=new File(folderPath);
		ArrayList<String> fileList=new ArrayList<String>(DataHandler.getFileNumber(folderPath));
		File [] files=folder.listFiles();
		for(int i=0;i<files.length;i++){
			fileList.add(files[i].getPath());
		}
		//System.out.print(fileList);
		return fileList;
	}
	
	//clean a directory
	public static void cleanDir(String folder){
		File dir=new File(folder);
		for(File file: dir.listFiles()) file.delete();
	}
	
	
	//split big gb level files in a folder into several small files every threshold lines 
	public static  ArrayList<String> splitFile() throws IOException{
		DataHandler.cleanDir(preprocessFolder);
		ArrayList<String> fileList=DataHandler.getFileList(DataHandler.originFolder);
		int fileTag=0;
		int lineCnt=0;
		BufferedReader br = null;
		BufferedWriter bw = null;
		for(int i=0;i<fileList.size();i++){
			File file=new File(fileList.get(i));
			try {
				br = new BufferedReader(new FileReader(file));
				br.readLine();//discard the 1st line of csv file
			} catch (IOException e) {
				e.printStackTrace();
			}
			String line;
			try {
				File splitFile=new File(DataHandler.preprocessFolder+Integer.toString(fileTag)+"_"+Integer.toString(i)+".txt");
				bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(splitFile)));
				while ((line = br.readLine()) != null ){
					bw.write(line);
					bw.write("\r\n");
					lineCnt++;
					if(lineCnt>=threshold){
						bw.flush();
						bw.close();
						fileTag++;
						splitFile=new File(DataHandler.preprocessFolder+Integer.toString(fileTag)+"_"+Integer.toString(i)+".txt");
						bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(splitFile)));
						lineCnt=0;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		bw.flush();
		bw.close();
		ArrayList<String> splitFileList=DataHandler.getFileList(DataHandler.preprocessFolder);
		return splitFileList;	
	}
	
	//split files and build a tree for one file
	public static Btree buildBtree(String fileName) throws IOException{
		BufferedReader br=null;
		Btree btree=new Btree(DataHandler.btreeOrder);
			File file=new File(fileName);
			br=new BufferedReader(new FileReader(file));
			String line;
			while((line=br.readLine())!=null){
				String[] s=line.split(",");
				MyRecord record=new MyRecord(s[0],s[1]);
				btree.insert(record);
			}	
		br.close();
		return btree;
	}
	
	//write and read object with files, abandonned
	public static void writeToDisc(Btree btree,File file) throws JsonGenerationException, JsonMappingException, IOException{
		new ObjectMapper().writeValue(file, btree);
	}
	
	public static Btree readFromDisc(File file) throws JsonParseException, JsonMappingException, IOException{
		return new ObjectMapper().readValue(file, Btree.class);
	}
	
	
	public static MyRecord recordFinder(ArrayList<String> btreeFiles, String name) throws JsonParseException, JsonMappingException, IOException{
		MyRecord result=null;
		for(int i=0;i<btreeFiles.size()-1;i++){
			System.out.print("Preprocessing the【"+ (i+1)+"/"+(btreeFiles.size()-1)+"】File...");
			Btree btree=buildBtree(btreeFiles.get(i));
			System.out.println("Done");
			System.out.println("Searching 【"+(i+1)+"/"+(btreeFiles.size()-1)+"】Files...");
			result=btree.search(name);
			if(result!=null){
				break;
			}
		}
		return result;
	}

	public static ArrayList<Btree> bufferTrees(){
		ArrayList<Btree> treeList=new ArrayList<Btree>(bufferedTreeNumber);
		for(int i=0;i<bufferedTreeNumber;i++){
			
		}
		return treeList; 
	}

}
