package rTree;

public class Detective {

	float a;
	float b;
	
	int x;
	int y;
	
	
	float maximalValue=-1.0F;
	
	Detective(float a, float b){
		this.a=a;
		this.b=b;
	}
	
	public float getA(){
		return a;
	}
	
	public float getB(){
		return b;
	}
	
	public void detection(RBox rb){
		if( (((float)rb.xmax*a) + ((float)rb.ymax * b)) > maximalValue ){
			this.maximalValue = ((float)rb.xmax*a) + ((float)rb.ymax * b);
			this.x = rb.xmax;
			this.y = rb.ymax;
		}
	}
	
    public RBox maximixingBox(){
    	RBox b = new RBox(x,x,y,y);
    	return b;
    }

    public float getMaximalValue(){
    	return maximalValue;
    }



}
