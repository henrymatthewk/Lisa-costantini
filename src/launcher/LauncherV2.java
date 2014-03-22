package launcher;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import btree.*;
import preprocess.MyRecord;
import preprocess.RecordPreprocessor;

import utility.*;
public class LauncherV2 {
	
	
	public static void main(String[] args) throws IOException{
		RecordPreprocessor.splitFile();
		BufferedReader br=null;
		String record=null;
		while(true){
			br=new BufferedReader(new InputStreamReader(System.in));
			String name=br.readLine();
			record=RecordPreprocessor.recordFinder(name);
			
			
			if(record==null){
				System.out.println("No such user with name \""+name+"\"");
			}
			else{
				System.out.println("------------Record found!!!------------");
				System.out.println("Name: "+name+", "+"Mobile: "+record);
			}
		}
		
	}
}
