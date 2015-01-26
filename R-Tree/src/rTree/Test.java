package rTree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
	static List<Integer> yValue;
	static List<Integer> xValue;
	
	public static void valueExtractor(String filePath){
		  try{
				File inputFile = new File (filePath);
			   Scanner scan = new Scanner(inputFile);

			   xValue = new ArrayList<Integer>();
			   yValue = new ArrayList<Integer>();
			    while (scan.hasNextLine())
			  {
			  String line = scan.nextLine(); 

			  String[] words = line.split(","); 

			  for (int index = 0; index < words.length; index++)
			   //System.out.println(words[index]);
				  if(index==0)
					  xValue.add(Integer.parseInt(words[index]));
				  else
					  yValue.add(Integer.parseInt(words[index]));
			  }
			    
			    int[] xValueArray = new int[xValue.size()];
			    int[] yValueArray = new int[yValue.size()];
			    long[] hilbertValueArray = new long[xValue.size()];
			    
			    HilbertValue HO = new HilbertValue();
			    
			    for(int i=0; i<xValueArray.length; i++){
			    	xValueArray[i] = xValue.get(i);
			    	yValueArray[i] = yValue.get(i);
			    	hilbertValueArray[i] = HO.getHilbertValue(xValueArray[i], yValueArray[i]);
			    	
			    }
			    
			    //Sorting hilbertValues
			    
			    for(int i=0; i<hilbertValueArray.length; i++){
			    	for(int j=0; j<hilbertValueArray.length; j++) {
			    		if(hilbertValueArray[i]<hilbertValueArray[j]){
			    			long temp = hilbertValueArray[j];
			    			hilbertValueArray[j] = hilbertValueArray[i];
			    			hilbertValueArray[i] = temp;
			    			
			    			int xtemp = xValueArray[j];
			    			xValueArray[j] = xValueArray[i];
			    			xValueArray[i] = xtemp;
			    			
			    			int ytemp = yValueArray[j];
			    			yValueArray[j] = yValueArray[i];
			    			yValueArray[i] = ytemp;
			    		}
			    	}
			    }
			    
			    

			    xValue = new ArrayList<Integer>();
			    yValue = new ArrayList<Integer>();
			    
			    
			   for(int i=0; i<hilbertValueArray.length; i++){
				   xValue.add(i, xValueArray[i]);
				   yValue.add(i, yValueArray[i]);
				   
			   }
			   
			   System.out.println("xValue Size  " + xValue.size() + " yValue Size " + yValue.size());
			   
			   
			   // sorted lists of xValues and yValues 
			   // the xValue and yValue lists here contain the sorted lists.
			   
			  
			   
			  }
			  catch(IOException e){
				  e.printStackTrace();
			  }
			  
	}

	public static void main(String args[]){
		
		/*RBox a = new RBox(2000,3000,500,700);
		RBox b = new RBox(2096,4094,4,4093);
		
		RBoxHelper RH = new RBoxHelper();
		System.out.println(RH.doTheseOverlap(a, b));*/
		
		valueExtractor("project3dataset30K-1-2.txt");
		
		 int[] xValueArray = new int[xValue.size()];
		 int[] yValueArray = new int[yValue.size()];
		 
		 
		 
		 for(int i=0; i<xValue.size(); i++){
			 xValueArray[i]=xValue.get(i);
			 yValueArray[i]=yValue.get(i);
		 }
		/* 
		List<Integer> dupX = new ArrayList<Integer>();
		List<Integer> dupY = new ArrayList<Integer>();
		int x=0;
		int y=0;
		int counter=0;
		for(int i=0; i<xValue.size(); i++){
			x=xValue.get(i);
			y=yValue.get(i);
			
			for(int j=0; j<xValue.size(); j++){
				if(i!=j){
					if(x==xValue.get(j) && y==yValue.get(j)){
						System.out.println("x = " + xValue.get(j) + "y = " + yValue.get(j));
						System.out.println("Duplicate found = " + counter++);}
						//dupX.add(x);
					    //dupY.add(y);
				}
			}
		}
		
		
		System.out.println("Number of duplicate pairs = " + dupX.size());
		
		System.out.println("Printing the duplicate pairs " + counter);
		
		for(int i=0; i<dupX.size(); i++){
			System.out.println("x = " + dupX.get(i) + " y = " + dupY.get(i));
		}
		
		System.out.println("Complete");*/
		
		 float max = -1.0F;
		 float a = 0.1F;
		 float b = 0.9F;
		 int x=0;
		 int y=0;
		 
		 for(int i=0; i<xValue.size(); i++){
			 if((a*xValueArray[i]+b*yValueArray[i])>max)
			 {
				 x=xValueArray[i];
				 y=yValueArray[i];
				 max=a*xValueArray[i]+b*yValueArray[i];
			 }
				 
		 }
		
	System.out.println("x = "+ x + " y = "+ y + " max = "+ max);

	

	RBox a1 = new RBox(2411,2411,9702,9702);
	RBox b1 = new RBox(2049,4073,8192,9771);
	
	RBoxHelper RH = new RBoxHelper();
	System.out.println(RH.contains(b1, a1));
		 
	}

}
