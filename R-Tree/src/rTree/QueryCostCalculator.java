package rTree;

public class QueryCostCalculator {
	
	private  int numberOfPagesAccessed=0;
	private int numberOfOccurence=0;
	
	 void increment(){
		numberOfPagesAccessed++;
	}
	
	 int numberOfPagesAccessed(){
		return numberOfPagesAccessed;
	}
	
	 void found(){
		numberOfOccurence++;
	}
	
	 int numberOfOccurence(){
		return numberOfOccurence;
	}

}
