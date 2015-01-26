package rTree;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PublicInteraction {
	
	static List<Integer> yValue;
	static List<Integer> xValue;
	static List<RBox> LeafDataEntry;
	
	//this method extracts the values from the files and stores them in the xValue and yValue list
	//it then sorts the values based on the HilbertValue(x,y) of the above taken a pair at a time;
	// thus the output of this method is that the xValue list and yValue list contain sequentially the values sorted on the basis
	// of their HilbertValue(x,y) in ascending order
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
			   
			  // System.out.println("xValue Size  " + xValue.size() + " yValue Size " + yValue.size());
			   
			   
			   // sorted lists of xValues and yValues 
			   // the xValue and yValue lists here contain the sorted lists.
			   
			  
			   
			  }
			  catch(IOException e){
				  e.printStackTrace();
			  }
			  
	}

	
	public static void populateLeafDataEntry(){
		LeafDataEntry = new ArrayList<RBox>();
		
		char[] desc = new char[500];
		
		for(int i=0; i<500; i++)
			desc[i]='a';
		
		for(int i=0; i<xValue.size(); i++){
			RBox temp = new RBox(xValue.get(i), xValue.get(i), yValue.get(i), yValue.get(i));
			LeafDataEntry.add(temp);
			
		}
		
		// Setting the description = '500 a's' for all the Data Entries
		for(int i=0;i<LeafDataEntry.size(); i++){
			LeafDataEntry.get(i).setDescription(desc);
		}
	}
	
	public static void main(String args[]){
		valueExtractor("project3dataset30K-1-2.txt");
		
		HilbertValue HV = new HilbertValue();
		
		//System.out.println("size of xValue list  = " + xValue.size() + "size of yValue list = "+ yValue.size());
	//	long MAX = 0;
		
		
		
		
		/*
		 * 					Start of BulkLoading
		 */
		
		
		/*
		for(int i=0; i<xValue.size(); i++){
			System.out.println("x = " + xValue.get(i) + "y = " + yValue.get(i) + " Hilbert Value = " + HV.getHilbertValue(xValue.get(i), yValue.get(i)));
		    if(MAX>HV.getHilbertValue(xValue.get(i), yValue.get(i))){
		    	System.out.println("-------------ERROR-------------");
		    	break;
		    }
		   
		    else
		    	MAX = HV.getHilbertValue(xValue.get(i), yValue.get(i));
		}*/
		
		populateLeafDataEntry();
		
	 /*   for(int i=0; i<LeafDataEntry.size(); i++){
	    	System.out.println(LeafDataEntry.get(i).toString());
	    }
	   */ 
	  //  System.out.println("Size of LeafDataEntry arrayList = "+LeafDataEntry.size());
	    
	    
	    List<RBox> LeafLayer = new ArrayList<RBox>();
	    
	    int counter=0;
	    
	    for(int i=0; i<7500; i++){
	    	
	    	RBox temp = new RBox(0,0,0,0);
	    	temp.Entry= new ArrayList<RBox>();
	    	
	    	for(int j=0; j<4; j++){
	    		temp.Entry.add(LeafDataEntry.get(counter));
	    		counter++;
	    	}
	       temp.dimensionCalculation();
	  	   LeafLayer.add(temp);
	    }
	    
	 //   System.out.println("Size of the LeafLayer = " + LeafLayer.size());
	   /* 
	    for(int i=0; i<LeafLayer.size(); i++){
	    	System.out.println("LEAF NUMBER " + (i+1));
	    	System.out.println(LeafLayer.get(i).toString());
	    }*/
	    
	    List<RBox> IndexLayer2= new ArrayList<RBox>();
	    
	    counter = 0;
	    
	    for(int i=0; i<37; i++){
	    	
	    	RBox temp = new RBox(0,0,0,0);
	    	temp.Entry = new ArrayList<RBox>();
	    	
	    	for(int j=0; j<204; j++){
	    		if(counter<LeafLayer.size())
	    		temp.Entry.add(LeafLayer.get(counter));
	    		counter++;
	    	}
	    	temp.dimensionCalculation();
	    	IndexLayer2.add(temp);
	    }
	    /*
	    for(int i=0;i<IndexLayer2.size(); i++){
	    	System.out.println("Node number " +  (i+1));
	    	System.out.println("Node details  " + IndexLayer2.get(i).toString());
	    }
	    System.out.println("Number of Index Nodes = " + IndexLayer2.size());
	    System.out.println("37th index node has " + IndexLayer2.get(36).Entry.size());
	    */
	    // root building
	    
	    RBox root = new RBox(0,0,0,0);
	    root.Entry = new ArrayList<RBox>();
	    
	    for(int i=0; i<37; i++){
	    	root.Entry.add(IndexLayer2.get(i));
	    }
	    
	    root.dimensionCalculation();
	    
	    System.out.println("Root " + root.toString());
	    
	    
	    
	    
	    /*
	     *                                END OF BULK LOADING 
	     */
	    
	    System.out.println("Bulkloading is complete");
	    Scanner in = new Scanner(System.in);
	    String usI="";
	    
	    RBox b = new RBox(-1,-1,-1,-1); 
	    QueryCostCalculator QueryCost;
	    int response=-1;
	    int x;
	    int y;
	    int xmin;
	    int ymin;
	    int xmax;
	    int ymax;
	    String temp;
	    
	    
	    
	    
	    
	    while(true){
	    
	    System.out.println();
	    System.out.println("Select from the below option - ");
	    System.out.println("1> Point Query");
	    System.out.println("2> Range Query");
	    System.out.println("3> f : ax + by maximzing point ");
	    System.out.println("4> Quit ");
	    System.out.println();
	    
	    System.out.println(" Please provide Input ");
	    usI = in.nextLine();
	    try{
	    	 response = Integer.parseInt(usI);
	    }
	    catch(Exception e){
	    	System.out.println("An Exception has Occurred - " + e.toString());
	    	System.out.println("INVALID INPUT PROVIDED");
	    	e.printStackTrace();
	    	
	    }
	   
	    
	    if(response!=1)
	    	if(response!=2)
	    		if(response!=3)
	    			if(response!=4)
	    			{
	    				System.out.println("Please select from options 1,2,3,4 only");
	    				System.out.println("Hit Enter to continue");
	    				temp=in.nextLine();
	    				continue;
	    			}
	    
	    if(response==1){
	    	QueryCost = new QueryCostCalculator();
	    
	    try
	    {
	    	
	    System.out.println("Enter X =");
	    x=Integer.parseInt(in.nextLine());
	    
	    System.out.println("Enter Y =");
	    y=Integer.parseInt(in.nextLine());
	    
	    
	    b = new RBox(x,x,y,y);
	     QueryCost = new QueryCostCalculator();
	    
	    System.out.println();
	    System.out.println(" Now Searching for - " + b.toString());
	    System.out.println("HIT ENTER TO SEARCH ... ");
	    System.out.println("___________________________________________________");
	    System.out.println("Now search will be conducted. Note that all the Rectangles being ");
	    System.out.println("traversed will be displayed. If the Entered Data Point is FOUND");
	    System.out.println("then the FOUND will be displayed with the details of the rectangle");
	    System.out.println("corresponding to the Data Point. The DESCRIPTION will also be displayed");
	    System.out.println("Number of pages is dipslayed.");
	    System.out.println("____________________________________________________");
	
	    temp = in.nextLine();
	    
	    root.pSearch(b, QueryCost);
	    System.out.println("_________________________________________________________________");
	    System.out.println("OUTPUT result of Point Query");
	    System.out.println("The found points will have FOUND key word at the beginning. If FOUND key word is NOT in the above messages than no point could be located" );
	    System.out.println("Search Complete ");
	    System.out.println("Number of Pages Accessed = " +  ((QueryCost.numberOfPagesAccessed())+1));
	    System.out.println("Hit Enter to continue");
	    System.out.println("_________________________________________________________________");
	    
	    temp=in.nextLine();
	    
	    }
	    
	    catch(Exception e)
	    {
	    	System.out.println("An Exception has Occurred - " + e.toString());
	    	System.out.println("INVALID INPUT PROVIDED");
	    	e.printStackTrace();
	    	
	    }
	    
	     
	    	
	    }
	    else if(response==2){
	    	try{
	   
	    
	    
	    System.out.println("Enter x-min. Please ensure x-min  <= x-max");
	    xmin=Integer.parseInt(in.nextLine());
	    
	    System.out.println("Enter x-max. Please ensure x-max  >= x-min");
	    xmax=Integer.parseInt(in.nextLine());
	    
	    if(xmin>xmax)
	    	{System.out.println("ERROR - Xmin is GREATER THAN Xmax");
	        continue;}
	        
	    System.out.println("Enter y-min. Please ensure y-min <= y-max");
	    ymin=Integer.parseInt(in.nextLine());
	    
	    System.out.println("Enter y-max. Please ensure y-max >= y-min");
	    ymax=Integer.parseInt(in.nextLine());
	    
	    if(ymin>ymax){
	    	System.out.println("ERROR - Ymin is GREATER THAN Ymax");
	    	continue;
	    }
	    
	    b=new RBox(xmin,xmax, ymin,ymax);
	    QueryCost = new QueryCostCalculator();
	    
	    System.out.println(" Now Searching for - " + b.toString());
	    System.out.println("HIT ENTER TO SEARCH ... ");
	    System.out.println("___________________________________________________");
	    System.out.println("Now search will be conducted. If the Data Points are FOUND");
	    System.out.println("then the FOUND data points will be displayed with the details");
	    System.out.println("The DESCRIPTION will also be displayed");
	    System.out.println("Number of pages is dipslayed.");
	    System.out.println("____________________________________________________");
	    
	   
	    temp = in.nextLine();
	    
	    
	   
	    
	    root.rSearch(b, QueryCost);
	    
	    System.out.println("_________________________________________________________________");
	    System.out.println("OUTPUT of Range Query");
	    System.out.println("The found points will have FOUND key word at the beginning. If FOUND key word is NOT in the above messages than no point could be located" );
	    System.out.println("Search Complete");
	    System.out.println("Number of Pages Accessed = " + QueryCost.numberOfPagesAccessed());
	    System.out.println("Hit Enter to continue");
	    System.out.println("_________________________________________________________________");
	    temp=in.nextLine();
	  
	   
	    }
	    catch (Exception e)
	    {
	    System.out.println("An Exception occurred" + e.toString());
	    System.out.println("INVALID INPUT PROVIDED");
	    e.printStackTrace();
	    }
	    }
	    else if(response==3){
	    	try{
	    System.out.println(" Enter a : ");
	    float A = Float.parseFloat(in.nextLine());
	    
	    System.out.println(" Enter b :");
	    float B = Float.parseFloat(in.nextLine());
	    
	    if((A+B)>1.0F){
	    	System.out.println("WARNING ! A+B > 1");
	        System.out.println("Hit Enter to Continue");
	        temp = in.nextLine();
	    }
	    
	    Detective detective = new Detective(A,B);
	    
	    root.maximizingEntryDetector(detective);
	    
	    System.out.println("_________________________________________________________________");
	    System.out.println("Detection Result   X = " + detective.x + "  Y = " + detective.y);
	    System.out.println("Maximal Value  = " + A + " * " +detective.x+" + "+B+" * "+ detective.y +  " ="+detective.maximalValue);
	    System.out.println("Hit Enter to continue");
	    System.out.println("__________________________________________________________________");
	    temp=in.nextLine();
	    }
	    catch(Exception e){
	    	System.out.println(e.toString());
	    	e.printStackTrace();
	    }
	  
	    	
	    }
	    else if (response==4){
	    	break;
	    }
	    
	    
	    
	    
	   
	   
	}

	    System.out.println("PROGRAM STOPPED");
}
}
