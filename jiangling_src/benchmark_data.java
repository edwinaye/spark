import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class benchmark_data{
  public static void main(String[] args) throws IOException {
 
  if(args.length != 2){
   System.out.println("usage: benchamrk numberOfItems fileName");
   System.exit(0);
  } 
  int end = Integer.parseInt(args[0]);
  File file = new File(args[1]);
  FileWriter out = new FileWriter(file);

  for(int i = 0; i < end; i++){
     out.write(i+"\t");
     for(int j = 0; j < 5; j++){
       out.write(Math.random()*100+"\t");
   }
   out.write("\r\n");
  }
  out.close(); 
 }
}

