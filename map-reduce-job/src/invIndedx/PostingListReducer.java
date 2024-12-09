//package invIndedx;
//import java.io.IOException;
//import java.util.*;
//
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.Reducer.Context;
//
//public class PostingListReducer extends Reducer<Text, Text, Text, Text> {
//
//	public void reduce(Text key, Iterable<Text> values, Context context)
//			throws IOException, InterruptedException {
//		
//		// ceaser,1:3
//		
////		HashSet<String,ArrayList<String>> distinctDocID = new HashSet<String>();
//		
//		Map<String, Set<String>> distinctDocID = new HashMap<>();
//		
//		for (Text docID : values) {
//			String id = docID.toString().split(":")[0];
//			String pos = docID.toString().split(":")[1];
//			
//			// add id if not exist
//			if (!distinctDocID.containsKey(id)) {
//				distinctDocID.put(id, new HashSet<String>());
//	        }
//			
//			distinctDocID.get(id).add(pos);
//
//
//		}
//		
//		StringBuilder docIdWithPositionsStr = new StringBuilder();
//		for (Map.Entry<String, Set<String>> doc : distinctDocID.entrySet()) {
//			
//			StringBuilder positions = new StringBuilder();
//			for(String pos : doc.getValue())
//			{
//				positions.append(pos+",");
//			}
//			
//			docIdWithPositionsStr.append(doc.getKey() + ":"+positions); 
//		}
//		context.write(key, new Text(docIdWithPositionsStr.toString()));
//	}
//
//}



package invIndedx;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PostingListReducer extends Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Map to store document IDs and their associated positions
        Map<String, List<String>> distinctDocID = new HashMap<>();

        // Process all values for the current key
        for (Text docID : values) {
            String[] parts = docID.toString().split(":");
            String id = parts[0];
            String pos = parts[1];
         
            // add id if not exist
			if (!distinctDocID.containsKey(id)) {
				distinctDocID.put(id, new ArrayList<String>());
	        }
            distinctDocID.get(id).add(pos);
        }

        System.out.println("Reducer Key: " + key.toString());
        System.out.println("Reducer Value: " + values.toString());
        // Build the output string
        
        StringBuilder docIdWithPositionsStr = new StringBuilder();
        for (Map.Entry<String, List<String>> doc : distinctDocID.entrySet()) {
            
            StringBuilder positions = new StringBuilder();
			for(String pos : doc.getValue())
			{
				positions.append(pos+",");
			}
            docIdWithPositionsStr.append(doc.getKey()).append(":").append(positions).append(";");
        }

        // Emit the term and its posting list
        context.write(key, new Text(docIdWithPositionsStr.toString().trim()));
    }
}