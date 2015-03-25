package approximationsearch.src;

import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import java.io.IOException;

public class MetaServer{
	//StatisticMap 
	 
	public PartitionStatistic[] globalMap; 

	public LinkedList<String> files;
	
	public MetaServer(){
		files = new LinkedList<String>();
	}

	public void buildFileList(String input){
		try{
			File file = new File(input);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
				files.add(line);
			}
			br.close();
			fr.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void printFileList(){
		for(String s : files)
			System.out.println(s);
	}
	public void buildMeta(double cutoff){
		globalMap = new PartitionStatistic[files.size()];
		int index =0;
		for(String file: files){
			long startTime = System.currentTimeMillis();
			globalMap[index] = new PartitionStatistic(file, cutoff);
			globalMap[index].fileName = file;
			globalMap[index].buildStatistic();
			globalMap[index].partitionID = index;
			index++;
			long time = System.currentTimeMillis()-startTime;
System.out.println("ID= "+index+" useTime= "+time + " mapSize= "+globalMap[index-1].statisticMap.size()+
" BF= "+globalMap[index-1].bloomFilter.size()+" fullSize= " + globalMap[index-1].fullMap.size());
			//if(time > 2000) break;
		}
	}
	public String checkDistribution(String movie){
		String res = "Map+Blo,movie="+movie+" ";
		for(int i =0; i < globalMap.length; i++)
			res = res + globalMap[i].lookup(movie)+" ";
		//res += "\n";
		return res;
	}
	
	public String fullDistribution(String movie){
		String res = "HashMap,movie="+movie+" ";
		for(int i =0; i < globalMap.length; i++)
			res = res + globalMap[i].fullLookup(movie)+" ";
		//res += "\n";
		return res;	
	}
	
	public void lookDistribution(String movie){
		for(int i =0; i < globalMap.length; i++) {
			globalMap[i].quantity = globalMap[i].lookup(movie);
		}
		Arrays.sort(globalMap);
		
		// for(int i =0; i < globalMap.length;i++)	
		//	System.out.println(globalMap[i].fileName+"---"+globalMap[i].partitionID+"---"+ globalMap[i].quantity);
	}
	
	public int distanceLookup(String movie){
		int res = 0;
		for(int i=0; i < globalMap.length;i++)
			res = res + Math.abs(globalMap[i].fullLookup(movie)-globalMap[i].lookup(movie));
		return res;	
	}

	public int allCount(String movie){
		int res =0;
		for(int i=0; i < globalMap.length;i++)
			res +=globalMap[i].fullLookup(movie);
		return res;
	}
	
	public void printMeta(){
		for(int i =0; i < globalMap.length; i++){
			System.out.println(globalMap[i].fileName);
			
			for( Map.Entry<String,Integer> entry : globalMap[i].statisticMap.entrySet()){
	 	        	System.out.println(entry.getKey() + " : " + entry.getValue());
				break;
			}
		}
	}
	
}
