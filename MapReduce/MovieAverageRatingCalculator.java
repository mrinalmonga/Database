
    package org.myorg;
 
	import java.io.IOException;
 	import java.util.*;

	import org.apache.hadoop.fs.Path;
 	import org.apache.hadoop.conf.*;
 	import org.apache.hadoop.io.*;
 	import org.apache.hadoop.mapred.*;
 	import org.apache.hadoop.util.*;

 	public class MovieAverageRatingCalculator {

 	   public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, FloatWritable> {
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

 	   public static class Reduce extends MapReduceBase implements Reducer<Text, FloatWritable, Text, FloatWritable> {
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
 	     JobConf conf = new JobConf(MovieAverageRatingCalculator.class);
 	     conf.setJobName("MovieAverageRatingCalculator");

 	     conf.setOutputKeyClass(Text.class);
 	     conf.setOutputValueClass(FloatWritable.class);

 	     conf.setMapperClass(Map.class);
 	     conf.setCombinerClass(Reduce.class);
 	     conf.setReducerClass(Reduce.class);

 	     conf.setInputFormat(TextInputFormat.class);
 	     conf.setOutputFormat(TextOutputFormat.class);

 	     FileInputFormat.setInputPaths(conf, new Path(args[0]));
 	     FileOutputFormat.setOutputPath(conf, new Path(args[1]));

 	     JobClient.runJob(conf);
	     System.exit(0);
 	   }
 	}







