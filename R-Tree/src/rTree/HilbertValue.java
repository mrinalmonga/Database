package rTree;

public class HilbertValue {
	static int BITS_PER_DIM=18;
	
	static long getHilbertValue(int x1, int x2) {
        long res = 0;


        for (int ix = BITS_PER_DIM - 1; ix >= 0; ix--) {
            long h = 0;
            long b1 = (x1 & (1 << ix)) >> ix;
            long b2 = (x2 & (1 << ix)) >> ix;

            if (b1 == 0 && b2 == 0) {
                h = 0;
            } else if (b1 == 0 && b2 == 1) {
                h = 1;
            } else if (b1 == 1 && b2 == 0) {
                h = 3;
            } else if (b1 == 1 && b2 == 1) {
                h = 2;
            }
            res += h << (2 * ix);
        }
        return res;
    }
	
	public static void main(String args[]){
		//for(int i=0; i<=128; i++){
		//BITS_PER_DIM=i;
		System.out.println("BITS_PER_DIM = " + BITS_PER_DIM + " HilbertValue " + getHilbertValue(10000,10000));
		//}
	}

}
