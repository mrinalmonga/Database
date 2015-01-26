package simpledb;


import java.io.*;

/**
 * BufferPool manages the reading and writing of pages into memory from
 * disk. Access methods call into it to retrieve pages, and it fetches
 * pages from the appropriate location.
 * <p>
 * The BufferPool is also responsible for locking;  when a transaction fetches
 * a page, BufferPool which check that the transaction has the appropriate
 * locks to read/write the page.
 */
public class BufferPool {
	
    /** Bytes per page, excluding header. */
    public static final int PAGE_SIZE = 4096;
    public static final int DEFAULT_PAGES = 100;
    public static final int DEFAULT_POLICY = 0;
    public static final int LRU_POLICY = 1;
    public static final int MRU_POLICY = 2;

    int replace_policy = DEFAULT_POLICY;
    int _numhits=0;
    int _nummisses=0;
    
    Page Frame[]; // The array of Frames holds the pages.
    int SIZE; // Size of the BufferPool
    int LRUIndex; // Index of the page which was Least Recently Used
    
    //BufferPoolQueue Queue; For future implementations. NOT used in this assignment
    //int pins[];			 For future implementations. NOT used in this assignment
    //boolean dirty[];		 For future implementations. NOT used in this assignment
    
     
     /*
      * The BufferPoolQueue class WAS added to be used for the LRU policy. It is NOT being used in
      * the current Assignment 1 because of a more EFFICIENT WAY of implementing the LRU policy
      * in the evictPage() method, without using the Queue YET achieving the SAME result.
      * 
      */
     class BufferPoolQueue {  
    		int front;
    		int rear;
    		int capacity;
    		int ArrayNum[];
    		
    		public BufferPoolQueue(int capacity){
    			this.capacity = capacity;
    			front =-1;
    			rear =-1;
    			ArrayNum = new int[this.capacity];
    		}
    		
    		boolean isEmpty(){
    			if ((front==-1)&&(rear==-1))
    				return true;
    			else 
    				return false;
    		}
    		
    		boolean isFull(){
    			if( ((rear+1) % capacity)==front)
    			 return true;
    			else
    		      return false;
    		}
    		
    		boolean enqueue(int num){
    			if(isEmpty())
    			{
    				front = 0;
    				rear = 0;
    				ArrayNum[rear] = num;
    				return true;
    			}
    			else if(isFull())
    			{
    			  System.out.println("Queue is Full");
    			  return false;
    			}
    			else{
    				rear = (rear + 1)%capacity;
    				ArrayNum[rear] = num;
    				return true;
    			}
    			
    		}
    		
    		int dequeue(){
    			int element = 0;
    			if(isEmpty()){
    				System.out.println("Queue is empty");
    				return -1;
    			}
    			else if(front==rear){
    				element = ArrayNum[front];
    				front = -1;
    				rear = -1;
    				return element;
    			}
    			else{
    				
    				element = ArrayNum[front];
    				front = (front+1)%capacity;
    				return element;
    			}
    		}

    	    int front(){
    	    	if(isEmpty())
    	    		return -1;
    	    	return ArrayNum[front];
    	    }
    	    
    	    int rear(){
    	    	if(isEmpty())
    	    		return -1;
    	    	return ArrayNum[rear];
    	    }

    	}  

     
    /**
     * Constructor.
     *
     * @param numPages number of pages in this buffer pool
     */
    public BufferPool(int numPages) {
    	
    	System.out.println("CONSTRUCTOR CALLED");
        Frame = new HeapPage[numPages];
        //Queue = new BufferPoolQueue(numPages);
        LRUIndex=0;
        SIZE = numPages;
        this._numhits=0;
        this._nummisses=0;
       
       }

  
    /**
     * Retrieve the specified page with the associated permissions.
     * Will acquire a lock and may block if that lock is held by another
     * transaction.
     * <p>
     * The retrieved page should be looked up in the buffer pool.  If it
     * is present, it should be returned.  If it is not present, it should
     * be added to the buffer pool and returned.  If there is insufficient
     * space in the buffer pool, an page should be evicted and the new page
     * should be added in its place.
     *
     * @param tid the ID of the transaction requesting the page
     * @param pid the ID of the requested page
     * @param perm the requested permissions on the page
     */
    public synchronized Page getPage(TransactionId tid, PageId pid, Permissions perm)
        throws TransactionAbortedException, DbException, IOException {
    	
  
    	int locationOfPageInBufferPool = isPageInBufferPool(pid);
    	
    	if(locationOfPageInBufferPool!=-1){
    		
    		this._numhits++;
    		return Frame[locationOfPageInBufferPool];
    	}
    	else {
    		
    		if(locationOfAnEmptyFrame()!=-1){
    			
    		   locationOfPageInBufferPool = locationOfAnEmptyFrame();
    		  this._nummisses++;
    		}
    		else
    			try{
    				this._nummisses++;
    			    locationOfPageInBufferPool = evictPage();
    			}
    		    catch (Exception e){
    		    	
    		    }
    		  }
    	
        DbFile RequiredHeapFILE = Database.getCatalog().getDbFile(pid.tableid());
    	
    	Frame[locationOfPageInBufferPool] = RequiredHeapFILE.readPage(pid);
    	
    	return Frame[locationOfPageInBufferPool];
  
}

    /**
     * Releases the lock on a page.
     * Calling this is very risky, and may result in wrong behavior. Think hard
     * about who needs to call this and why, and why they can run the risk of
     * calling it.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param pid the ID of the page to unlock
     */
    public synchronized void releasePage(TransactionId tid, PageId pid) {
        // no need to implement this
       
    }

    /**
     * Release all locks associated with a given transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     */
    public synchronized void transactionComplete(TransactionId tid) throws IOException {
       // no need to implement this
     }

    /** Return true if the specified transaction has a lock on the specified page */
    public  synchronized boolean holdsLock(TransactionId tid, PageId p) {
       // no need to implement this
         return false;
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    public  synchronized void transactionComplete(TransactionId tid, boolean commit)
        throws IOException {
       // no need to implement this
    }

    /**
     * Add a tuple to the specified table behalf of transaction tid.  Will
     * acquire a write lock on the page the tuple is added to. May block if
     * the lock cannot be acquired.
     *
     * @param tid the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t the tuple to add
     */
    public synchronized void insertTuple(TransactionId tid, int tableId, Tuple t)
        throws DbException, IOException, TransactionAbortedException {
       // no need to implement this

    }

    /**
     * Remove the specified tuple from the buffer pool.
     * Will acquire a write lock on the page the tuple is added to. May block if
     * the lock cannot be acquired.
     *
     * @param tid the transaction adding the tuple.
     * @param t the tuple to add
     */
    public synchronized void deleteTuple(TransactionId tid, Tuple t)
        throws DbException, TransactionAbortedException {
       // no need to implement this

    }

    /**
     * Flush all dirty pages to disk.
     * NB: Be careful using this routine -- it writes dirty data to disk so will
     *     break simpledb if running in NO STEAL mode.
     */
    public synchronized void flushAllPages() throws IOException {
           // no need to implement this
    }

    /** Remove the specific page id from the buffer pool.
        Needed by the recovery manager to ensure that the
        buffer pool doesn't keep a rolled back page in its
        cache.
    */
    public synchronized void discardPage(PageId pid) {
     // no need to implement this
          }

    /**
     * Flushes a certain page to disk
     * @param pid an ID indicating the page to flush
     */
    private  synchronized void flushPage(PageId pid) throws IOException {
         // no need to implement this
    }

    /** Write all pages of the specified transaction to disk.
     */
    public synchronized  void flushPages(TransactionId tid) throws IOException {
             // no need to implement this
    }

    /**
     * Discards a page from the buffer pool. Return index of discarded page
     * 
     * IMPORTANT - Please see the document submitted with the Assignment 1 for explanation about this method.
     */
    private  synchronized int evictPage() throws DbException {
    
    	int location = 0;
    	
	 if(this.replace_policy==1)
	 {
		location=this.LRUIndex;
		
	    this.LRUIndex=(this.LRUIndex + 1)%this.SIZE;
	    
		return location;
	 }
	 else if(this.replace_policy==2)
	 {		 location = SIZE-1;
	     return location;}
	 
	 return 0;
	
    }

    public int getNumHits(){
	return _numhits;
    }
    public int getNumMisses(){
	return _nummisses;
    }
    
    public void setReplacePolicy(int replacement){
	this.replace_policy=replacement;
    }
    
    /*
     * This method finds the Page in the Frames[] array. If the page is FOUND the index of the Frame
     * which holds the page is returned. Otherwise -1 is returned.
     */
    public synchronized int isPageInBufferPool(PageId pid){
    	
    	for(int i=0; i<this.SIZE ; i++)
    		if(Frame[i]!=null)
    		if(Frame[i].id().equals(pid)){
    			//System.out.println("Page found at location" + i);
    			return i;}
    	
    	
    	
    	return (-1);
    }
    
    
    /*
     * This method returns the location of next available EMPTY frame, if it is there, in the Frame[] array.
     * If NO EMPTY page is there then -1 is returned.
     * 
     * The method searches for the presence of an EMPTY frame SEQUENTIALLY starting from
     * Frame[0] to the last Frame available in the Frame[] array.
     * 
     * IMPORTANT - Since this method searches sequentially for empty frames, this results in the Frame[] array 
     * being populated by the getPage() METHOD sequentially that is from Frame[0] to last available Frame.
     */
    public synchronized int locationOfAnEmptyFrame(){
    	
    	for(int i=0; i<Frame.length; i++)
    			if(Frame[i]==null)
    				return i;
    	
    	return (-1);
    }
    
    
}