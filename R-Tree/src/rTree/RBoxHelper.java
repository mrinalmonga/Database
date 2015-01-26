package rTree;

public class RBoxHelper {

	static boolean doTheseOverlap(RBox b1, RBox b2){
	
		
		// If any of one of the Box contains the other then they overlap
		
		if(contains(b1,b2))
			return true;
		
		if(contains(b2,b1))
			return true;
		
		boolean xOverlap=false ;
		boolean yOverlap=false ;
		
		if(b1.xmax==b2.xmin && b2.xmax > b1.xmax)
			return false;
		
		if(b2.xmax==b1.xmin && b1.xmax > b2.xmax)
			return false;
		
		if((b1.xmin<=b2.xmin&&b1.xmax>=b2.xmax)||(b2.xmin<=b1.xmin&&b2.xmax>=b1.xmax))
			xOverlap=true;
		
		if(!xOverlap)
		if(b1.xmin >= b2.xmin && b1.xmin <= b2.xmax)
			xOverlap=true;
		
		if(!xOverlap)
		    if(b1.xmax >= b2.xmin && b1.xmax <= b2.xmax)
		    	xOverlap = true;
		    else{//System.out.println("X overlap");
		    	return false;}
		
		
		if((b1.ymin<=b2.ymin&&b1.ymax>=b2.ymax)||(b2.ymin<=b1.ymin&&b2.ymax>=b1.ymax))
		 yOverlap=true;
		
		if(!yOverlap)
		if(b1.ymin>=b2.ymin && b1.ymin<=b2.ymax)
			yOverlap=true;
		
		if(!yOverlap)
			if(b1.ymax>=b2.ymin && b1.ymax<=b2.ymax)
				yOverlap=true;
			else{
				//System.out.println("Y overlap");
				return false;}
		
		if(xOverlap&&yOverlap)
			return true;
		
		return false;
	}

	
	/*
	 * Checks if RBox b1 contains b2
	 */
	static boolean contains(RBox b1, RBox b2){
		
			
			boolean xContainment = false;
			boolean yContainment = false;
			
			if(b1.xmin<=b2.xmin && b1.xmax>=b2.xmax)
				xContainment = true;
			if(b1.ymin<=b2.ymin && b1.ymax>=b2.ymax)
				yContainment = true;
			
			if(xContainment&&yContainment)
				return true;
		
		
	     return false;
			
			
	}

}
