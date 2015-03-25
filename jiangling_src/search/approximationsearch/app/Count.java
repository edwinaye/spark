package approximationsearch.app;

import approximationsearch.src.*;
import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import java.io.IOException;

public class Count implements Runnable {
	private Thread t;
	private String threadName;
	private int id, total;
	private int flag;	
	private String movie;
	private MetaServer meta;

	public Count(String name, int ID, int t) {
		threadName = name;
		id = ID;
		total = t;
		System.out.println("Creating "+ threadName);
	}
	public void setTask(String movieID, MetaServer server, int f){
		movie = movieID;
		meta = server;
		flag = f;
	}
	public void run() {
		//try {i
			int len = (int) (meta.files.size()+2)/4;
			int start = id *len;
			long startTime = System.currentTimeMillis();
			while(start < meta.files.size()&& start < len*(id+1)){
				if(flag ==0 && meta.globalMap[start].quantity == 0){
					start +=1; continue;
				}
				try {	
					long t = System.currentTimeMillis();
					String file = flag == 0? meta.globalMap[start].fileName:meta.files.get(start);
		        		FileReader fr = new FileReader(new File(file));	
					BufferedReader br = new BufferedReader(fr);
					String line;
			
					while((line = br.readLine()) != null) {
						String x = line.split(" ")[1];
						if(x.equals(movie)) {
							int k = 0;
							while(k++ < 2);
						}
					}
					start += 1;
					//br.close(); fr.close();
					long e = System.currentTimeMillis() - t;
					System.out.println("id= " + id + " index= " + start + " time= " + e);
				} catch(IOException e){
					e.printStackTrace();
				}	
			}
				
			long use = System.currentTimeMillis()-startTime;
			System.out.println( "flag= "+flag+" Time of thread " + id + " : " + use);
		//}//catch (InterruptedException e) {
		//	System.out.println(threadName + "interrupted.");
		//}		
       }
	
	public void start (){
		System.out.println("Starting " + threadName);
		if(t == null){
			t = new Thread (this, threadName);
			t.start();
		} 
	}
	
}
