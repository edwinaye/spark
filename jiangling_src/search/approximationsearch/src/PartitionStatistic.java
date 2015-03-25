package approximationsearch.src;

import approximationsearch.bloomfilter.*;

import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import java.io.IOException;

public class PartitionStatistic implements Comparable<PartitionStatistic>{
	
	public String fileName;
	int partitionID;
	public int quantity;
	double cutoff;  
	HashMap<String, Integer> statisticMap;
	HashMap<String, Integer> fullMap;
	BloomFilter<String> bloomFilter;
	
	public PartitionStatistic(String file, double cut){
		fileName = file;
		cutoff = cut;
	}
	
	public void buildStatistic() {
	
	HashMap<String, Integer> map = new HashMap<String, Integer>();   
	int[] interval = {1,4,7,12,20,33};
	int[] count = new int[interval.length];

	try{
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;

		while((line = br.readLine()) != null) {
			String x = line.split(" ")[1];
			if(!map.containsKey(x)){
				map.put(x,1); count[0]++;}
			else{ 
				int val = map.get(x);
				map.put(x, val+1);
				if(val+1 > interval[count.length-1])continue;
				for(int i=count.length-1; i > 0; i--)
					if(val+1 == interval[i]) {count[i]++; break;}
			}
		}
		br.close();		
		fr.close();		
	     } catch (IOException e) {
			e.printStackTrace();
	     }

		fullMap = map;	

		int cutVal = interval[0], index =0, total = map.size(), dominateSize;
		dominateSize = (int)cutoff * total > 1024? (int)cutoff*total : 1536;
			
		while(index < count.length && dominateSize < count[index]) index++;
	
		if(index >= count.length) {cutVal = interval[count.length-1];dominateSize =count[count.length-1];}
		else {cutVal = interval[index];dominateSize = count[index];}

		double falsePositiveProbability = 0.01;
		int expectedNumberOfElements = total - dominateSize > 0? total-dominateSize:1;
	
		statisticMap = new HashMap<String, Integer>(dominateSize);
		bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedNumberOfElements);
		
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			String key = entry.getKey();
			int val = entry.getValue();
			if(val >= cutVal)
				statisticMap.put(key, val);
			else
				bloomFilter.add(key);
		}
	}
				 
	public void printStatistic(){
		for(Map.Entry<String, Integer>  entry : statisticMap.entrySet())
			System.out.println(entry.getKey() +" " +entry.getValue());
	}

	public int lookup(String setName){
		if(statisticMap.containsKey(setName))
			quantity = statisticMap.get(setName);
		else if(bloomFilter.contains(setName))
			quantity = 1;
		else
			quantity = 0;

		return this.quantity;
	}

	public int fullLookup(String setName){
		if(fullMap.containsKey(setName))
			quantity = fullMap.get(setName);
		else
			quantity = 0;
		return this.quantity;
	}	

	public int compareTo(PartitionStatistic that){
		return that.quantity - this.quantity;
	}
}
