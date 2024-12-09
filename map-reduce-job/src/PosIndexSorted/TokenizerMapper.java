//package invIndedx;
//import java.io.IOException;
//import java.util.StringTokenizer;
//
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Mapper.Context;
//
//public class TokenizerMapper extends Mapper<Object, Text, Text, Text> {
//	
//
//	public void map(Object key, Text value, Context context)
//			throws IOException, InterruptedException {
//
//
//	    String DocId = value.toString().substring(0, value.toString().indexOf("\t"));
//	    String value_raw =  value.toString().substring(value.toString().indexOf("\t") + 1);
//
//	    String fullLine[] = value_raw.split(" ");
//	    
//	    int eol = 0;
//		
//	    for(int index = 0; eol == 0;) {
//	    	for(String st : fullLine)
//	    	{
//	    		context.write(new Text(st), new Text(DocId+":"+index)); // ceaser,1:3
//	    		index++;
//	    	}
//	    	eol = 1;
//		}
//		
//
//	}
//
//}


package PosIndexSorted;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TokenizerMapper extends Mapper<Object, Text, Text, Text> {

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Extract the document ID and content
        String docId = value.toString().substring(0, value.toString().indexOf("\t"));
        String content = value.toString().substring(value.toString().indexOf("\t") + 1);

        // Tokenize the content into words
        String[] words = content.split(" ");

        // Emit each word with its document ID and position
        for (int index = 0; index < words.length; index++) {
        	context.write(new Text(words[index]), new Text(docId + ":" + index)); // emit -> ceaser,1:2
        }
    }
}


