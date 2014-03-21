package launcher;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import btree.*;
import preprocess.MyRecord;

import utility.*;
public class Launcher {
	
	
	public static void main(String[] args) throws IOException{
		//split files;
		ArrayList<String> fileList=DataHandler.splitFile();
		BufferedReader br=null;
		while(true){
			br=new BufferedReader(new InputStreamReader(System.in));
			String name=br.readLine();
			MyRecord record=DataHandler.recordFinder(fileList, name);
			if(record==null){
				System.out.println("No such user with name \""+name+"\"");
			}
			else{
				System.out.println("------------Record found!!!------------");
				System.out.println("Name: "+record.getName()+", "+"Mobile: "+record.getNumber());
			}
		}
		
	}
}
