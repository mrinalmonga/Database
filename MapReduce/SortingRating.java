
   package org.myorg;
 
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;


 	public class SortingRating {

 	   public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, FloatWritable> {
 	     private final static FloatWritable one = new FloatWritable(1);
 	     private Text word = new Text();

 	     public void map(LongWritable key, Text value, OutputCollector<Text, FloatWritable> output, Reporter reporter) throws IOException {
 	       String line = value.toString();
 	       StringTokenizer tokenizer = new StringTokenizer(line);
 	      
 	         one.set(Float.parseFloat(tokenizer.nextToken()));
 	         word.set(tokenizer.nextToken());
 	         output.collect(word, one);
 	     
 	     }
 	   }

 	   public static class Reduce extends MapReduceBase implements Reducer<Text, FloatWritable, Text, FloatWritable> {
 	     public void reduce(Text key, Iterator<FloatWritable> values, OutputCollector<Text, FloatWritable> output, Reporter reporter) throws IOException {
 	       
 	       while (values.hasNext()) {
 	    	  output.collect(key, values.next());
 	       }
 	       
 	     }
 	     
 	     
 	   }

 	   
 	   
 	  public static class Map1 extends MapReduceBase implements Mapper<LongWritable, Text, Text, FloatWritable> {
  	     private final FloatWritable one = new FloatWritable(1);
  	     private Text word = new Text();

  	     public void map(LongWritable key, Text value, OutputCollector<Text, FloatWritable> output, Reporter reporter) throws IOException {
  	       String line = value.toString();
  	       StringTokenizer tokenizer = new StringTokenizer(line);
  	       
  	       if(tokenizer.hasMoreTokens())
  	        tokenizer.nextToken();
  	       
  	       if(tokenizer.hasMoreTokens())
  	         word.set(tokenizer.nextToken());
  	         
  	       String two = "-1";
  	      if(tokenizer.hasMoreTokens())
  	         two = tokenizer.nextToken();
  	         boolean didExceptionOccurDuringParsing = false;
  	         float rating = -1.0F;
  	         
  	         try{
 	              rating = Float.parseFloat(two);
 	            }
 	         catch(Exception e){
 		           didExceptionOccurDuringParsing = true;
 	          }
 	
 	         if(!didExceptionOccurDuringParsing)
  	           one.set(rating);
  	         
  	         if(!didExceptionOccurDuringParsing)
  	          output.collect(word, one);
  	        
  	     }
  	   }

 	  
 	  
 	  
 	 public static class Reduce1 extends MapReduceBase implements Reducer<Text, FloatWritable, Text, FloatWritable> {
 	     public void reduce(Text key, Iterator<FloatWritable> values, OutputCollector<Text, FloatWritable> output, Reporter reporter) throws IOException {
 	       float sum = 0.0F;
 	       float counter = 0.0F;
 	       while (values.hasNext()) {
 	         sum += values.next().get();
 	         counter = counter + 1.0F;
 	       }
 	       sum = sum / counter;
 	       output.collect(key, new FloatWritable(sum));
 	     }
 	   }
 	   
 	   
 	   
 	   
 	   
 	   
 	   public static void main(String[] args) throws Exception {
 		 JobConf conf1 = new JobConf(SortingRating.class);
  	     conf1.setJobName("MovieAverageRatingCalculator");

  	     conf1.setOutputKeyClass(Text.class);
  	     conf1.setOutputValueClass(FloatWritable.class);

  	     conf1.setMapperClass(Map1.class);
  	     conf1.setCombinerClass(Reduce1.class);
  	     conf1.setReducerClass(Reduce1.class);

  	     conf1.setInputFormat(TextInputFormat.class);
  	     conf1.setOutputFormat(TextOutputFormat.class);

  	     FileInputFormat.setInputPaths(conf1, new Path(args[0]));
  	     FileOutputFormat.setOutputPath(conf1, new Path(args[1]));

  	     JobClient.runJob(conf1); 
 		   
 		   
 		   
 		 ////  
 		   
 	     JobConf conf = new JobConf(SortingRating.class);
 	     conf.setJobName("Sorting");

 	     conf.setOutputKeyClass(Text.class);
 	     conf.setOutputValueClass(FloatWritable.class);
 	     
 	     conf.setOutputKeyComparatorClass(ReverseComparator.class);

 	     conf.setMapperClass(Map.class);
 	     conf.setCombinerClass(Reduce.class);
 	     conf.setReducerClass(Reduce.class);

 	     conf.setInputFormat(TextInputFormat.class);
 	     conf.setOutputFormat(TextOutputFormat.class);

 	     FileInputFormat.setInputPaths(conf, new Path(args[1]));
 	     FileOutputFormat.setOutputPath(conf, new Path(args[2]));

 	     JobClient.runJob(conf);
	     System.exit(0);
 	   }
 	   
 	   
 	  static class ReverseComparator extends WritableComparator {
 	        private static final Text.Comparator TEXT_COMPARATOR = new Text.Comparator();

 	        public ReverseComparator() {
 	            super(Text.class);
 	        }

 	        @Override
 	        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
 	            try {
 	                return (-1)* TEXT_COMPARATOR
 	                        .compare(b1, s1, l1, b2, s2, l2);
 	            } catch (Exception e) {
 	                throw new IllegalArgumentException(e);
 	            }
 	        }

 	        @Override
 	        public int compare(WritableComparable a, WritableComparable b) {
 	            if (a instanceof Text && b instanceof Text) {
 	                return (-1)*(((Text) a)
 	                        .compareTo((Text) b));
 	            }
 	            return super.compare(a, b);
 	        }
 	    }
 	   
 	   
 	   
 	   
 	   
 	   
 	   
 	   
 	   
 	}




