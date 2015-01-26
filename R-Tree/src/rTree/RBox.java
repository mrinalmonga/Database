package rTree;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class RBox {

	int xmax;
	int xmin;
	int ymax;
	int ymin;

	
	public char[] description; // This is used only by a Data Entry. Not used by any other type of RBox.
	
	List<RBox> Entry = null; // for data entry its value is null
	
	List<RBox> listOfPotentialRBoxes = null;
	
	RBox(int xmin, int xmax, int ymin, int ymax){
		
		
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		//id++;
	}
	
	public void dimensionCalculation(){
		// will assign xmax and xmin, ymax and ymin from the values of the entries
		int XMax = -100000;
		int YMax = -100000;
		int XMin = 100000;
		int YMin = 100000;
		
		for(int i=0; i<Entry.size(); i++){
			
			if(XMax < Entry.get(i).xmax)
				XMax = Entry.get(i).xmax;
			
			if(XMin > Entry.get(i).xmin)
				XMin = Entry.get(i).xmin;
			
			if(YMax < Entry.get(i).ymax)
				YMax = Entry.get(i).ymax;
			
			if(YMin > Entry.get(i).ymin)
				YMin = Entry.get(i).ymin;
		}
		try{
		if(XMax<XMin||YMax<YMin)
			 throw new Exception("ERROR MAX < MIN");
		     
		}
		catch(Exception e){
			e.printStackTrace();
		}
		this.xmax=XMax;
		this.ymax=YMax;
		this.xmin=XMin;
		this.ymin=YMin;
		
	}
	
	
	public String toString(){
		String value=null;
		
		value="X-min = " + xmin + " X-max = " + xmax + " Y-min = " + ymin + " Y-max = "+ ymax ;
		return value;
	}
	
	
	
	public void pSearch(RBox b, QueryCostCalculator QueryCost){
		
		boolean isThisALeafEntry=false ;
		
		if(this.Entry==null)
			isThisALeafEntry=true;
		
		RBoxHelper Temp = new RBoxHelper();
		
		
		if(!isThisALeafEntry){
		QueryCost.increment();
		
		System.out.println(" Now Searching  inside RBox"+this.toString());	
		
		listOfPotentialRBoxes = new ArrayList<RBox>();
		
		    
			// prepare a list of boxes to be searched
			for(int i=0; i<Entry.size(); i++){
				if(Temp.doTheseOverlap(Entry.get(i), b))
				//if(Temp.doTheseOverlap(b, Entry.get(i)))
					listOfPotentialRBoxes.add(Entry.get(i));
			}
			
			for(int i=0; i < listOfPotentialRBoxes.size(); i++){
				listOfPotentialRBoxes.get(i).pSearch(b, QueryCost);
			}
			
		}
		
		if(isThisALeafEntry){
			
			if(Temp.contains (this,b)){
				System.out.println("__________________________________________________________________________");
				System.out.println("FOUND the Data Entry : " + this.toString());
				System.out.println("DESCRIPTION of the Data Entry = " + this.getDescription());
				System.out.println("__________________________________________________________________________");
				
			}
				
		}
		
		
	}
	
	
	public void rSearch(RBox b, QueryCostCalculator QueryCost){

		
		
		
		boolean isThisALeaf=false ;
		
		
		if(this.Entry!=null)
			if(this.Entry.get(0).Entry==null)
			   isThisALeaf=true;
		
		RBoxHelper Temp = new RBoxHelper();
		
		// if this is index node
		if(!isThisALeaf){
		QueryCost.increment();
		//System.out.println(" Now Searching  inside RBox "+this.toString());	
		
		listOfPotentialRBoxes = new ArrayList<RBox>();
		
		    
			// prepare a list of boxes to be searched
			for(int i=0; i<Entry.size(); i++){
				//System.out.println("Now checking the overlap condition for "+Entry.get(i).toString());
				if(Temp.doTheseOverlap(b, Entry.get(i))){
					listOfPotentialRBoxes.add(Entry.get(i));
					//System.out.println("added");
					}
				
					
			}
			
			
			//System.out.println("Starting Search in the prepared list-");
			
			for(int i=0; i < listOfPotentialRBoxes.size(); i++){
				
				listOfPotentialRBoxes.get(i).rSearch(b, QueryCost);
			}
			
		}
		
		if(isThisALeaf){
			QueryCost.increment();
			//System.out.println("Now Searching in a LEAF RBox " + this.toString());
			
			
			for(int i=0; i<this.Entry.size(); i++){
				
				if(Temp.contains(b, Entry.get(i)))
				{   
				    System.out.println("_________________________________________________________________________________");
					System.out.println("FOUND the Data Entry " + Entry.get(i).toString());
					System.out.println("DESCRIPTION of the Data Entry " + Entry.get(i).getDescription());
					System.out.println("_________________________________________________________________________________");
				}
				
			}
			
				
		}
		
		
	}

    public String getDescription(){
    	if(description==null)
    		return null;
    	else{
    		String temp="";
    		
    		for(int i=0;i<description.length;i++)
    			temp = temp + description[i];
    		
    		return temp;
    	}	
    	
    }

    public void setDescription(char[] description){
    	this.description=description;
    }

    public float centroidX(){
    	float centroidX = ((float)(xmin+xmax))/(float)2.0;
    	return centroidX;
    }
    
    public float centroidY(){
    	float centroidY = ((float)(ymin+ymax))/(float)2.0;
    	return centroidY;
    }

    public Detective maximizingEntryDetector(Detective detective){
    	
    	boolean isThisALeafNode = false ;
    	
    	if(this.Entry!=null)
			if(this.Entry.get(0).Entry==null)
    		isThisALeafNode=true;
    	
    	if(!isThisALeafNode){
    		
    		float a = detective.getA();
    		float b = detective.getB();
    		float maximalValue= detective.getMaximalValue();
    		
    		List<RBox> potentialRBoxes = new ArrayList<RBox>();
    		
    		//System.out.println("INSIDE NODE " + this.toString());
    		
    		for(int i=0; i<Entry.size(); i++)
    		{
    			float currentValue = (a*Entry.get(i).centroidX()) + (b*Entry.get(i).centroidY());
    			
    			//System.out.println("Currenlty at " + Entry.get(i).toString());
    			//System.out.println("Currently ax+by = " + currentValue);
    			
    			if((a*Entry.get(i).centroidX())+(b*Entry.get(i).centroidY())>maximalValue)
    			{
    				
    				maximalValue = a*Entry.get(i).centroidX() + b*Entry.get(i).centroidY();
    		    }
    		}	
    	    
    		//System.out.println("The maximal value in this NODE being analyzed  = " + maximalValue);
    		
    		for(int i=0; i<Entry.size(); i++){
    			float currentValue = (a*Entry.get(i).centroidX()) + (b*Entry.get(i).centroidY());
    			
    			if(currentValue==maximalValue){
    				potentialRBoxes.add(Entry.get(i));
    			//	if(i>0)
    				//	potentialRBoxes.add(Entry.get(i-1));
    				//if(i<(Entry.size()-1))
    					//potentialRBoxes.add(Entry.get(i+1));
    				//System.out.println("Adding " + Entry.get(i).toString());
    			}
    		}
    		
    		//System.out.println("The number of RBoxes with maximal value = " + potentialRBoxes.size());
    		//System.out.println("These are -");
    		
    		
    		
    		for(int i=0; i<potentialRBoxes.size(); i++){
    			//System.out.println(potentialRBoxes.get(i).toString());
    		}
    		
    		
    		
    		
    		
    		RBoxHelper RBH = new RBoxHelper();
    		List<RBox> overlappingRBoxes = new ArrayList<RBox>();
    		
    		for(int i=0; i<potentialRBoxes.size(); i++){
    			for(int j=0; j<Entry.size(); j++){
    				
    				 if(RBH.doTheseOverlap(potentialRBoxes.get(i), Entry.get(j))||RBH.doTheseOverlap(Entry.get(j), potentialRBoxes.get(i)))
    					 overlappingRBoxes.add(Entry.get(j));
    			
    			}
    		}
    	
    		
    		//System.out.println("The list of overlapping RBoxes is " + overlappingRBoxes.size());
    		
    		for(int i=0; i<overlappingRBoxes.size(); i++){
    			//System.out.println(overlappingRBoxes.get(i).toString());
    		}
    		
    		for(int i=0; i<overlappingRBoxes.size(); i++){
    			potentialRBoxes.add(overlappingRBoxes.get(i));
    		}
    		
    		//System.out.println("After adding the overlapping boxes to the list of potential RBoxes the list is -");
    		
    		for(int i=0; i<potentialRBoxes.size(); i++){
    			//System.out.println(potentialRBoxes.get(i));
    		}
    		
    		//System.out.println("Now removing any possible duplication in the potential RBoxes list");
    		
    		HashSet hs = new HashSet();
    		hs.addAll(potentialRBoxes);
    		potentialRBoxes.clear();
    		potentialRBoxes.addAll(hs);
    		
    		//System.out.println("Completed duplication removal. Now printing the potential RBoxes list ");
    		
    		for(int i=0; i<potentialRBoxes.size(); i++){
    			//System.out.println(potentialRBoxes.get(i));
    		}
    		
    		//System.out.println("Now sending DETECTIVE to the list of potential RBoxes");
    		
    		for(int i=0; i<potentialRBoxes.size(); i++){
    			potentialRBoxes.get(i).maximizingEntryDetector(detective);
    		}
    		
    		return detective;
    	}
    	
    	if(isThisALeafNode){
    		for(int i=0; i<Entry.size(); i++){
    			detective.detection(Entry.get(i));
    		}
    		
    		return detective;
    	}
    	
    	return detective;
    }
    
}
