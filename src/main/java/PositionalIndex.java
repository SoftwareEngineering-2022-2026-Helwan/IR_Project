
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.*;
import java.util.*;

public class PositionalIndex {

    public static Set<String> distinctTerms = new TreeSet<>();
    public static Map<String, String> postingList = new TreeMap<>();
    public static Map<String, Integer> df = new TreeMap<>();
    public static Map <String, String > tf  = new TreeMap<>();
    public static Map<String, Double> idf = new TreeMap<>();
    public static Map<String, String> tfWeight = new TreeMap<>();
    public static Map<String, Map<String, Double>> tf_idf = new TreeMap<>();
    public static Map<String, Double> document_weight_length = new TreeMap<>();
    public static Map<String, Map<String, List<Double>>> unit_vector = new TreeMap<>();
    public static Map<String, List<Double>> query_unit_vector = new TreeMap<>();

    public static void main(String[] args) throws IOException {
        String folderPath = (new File("").getAbsolutePath()) + "\\docs";
        readFileAndBuildPostingList(folderPath);

        // Print out sorted terms and posting list
        System.out.println("Distinct Terms: " + distinctTerms);
        System.out.println("Posting List: " + postingList);

        calculateTF(postingList,tf);
        calculateTFWeight(tf,tfWeight);
        calculateDF(postingList,df);
//        System.out.println("df= "+ df);
//        calculateIDF(df,10,idf);

//        calculateTFIDF(tfWeight,idf,tf_idf);
//        calculateDocumentWeightLength(tf_idf,document_weight_length);

//        calculateDocumentWeightLength(tf_idf,document_weight_length);
//        calculateNormalizeTFIDF(tf_idf,document_weight_length,unit_vector);
//        calculateSimilarity(query_unit_vector,unit_vector);

    }

    public static Double documentRounder(Double value)
    {
        return  new BigDecimal(value).setScale(7, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double idfLog(int N, int df)
    {
        return BigDecimal.valueOf(Math.log10(((double) N) / df)).setScale(6, RoundingMode.HALF_UP).doubleValue();
    }


    public static void readFileAndBuildPostingList(String folderPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(folderPath + "/index.txt"));
        try {
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split("\t");
                String term = parts[0];
                String docIds_with_positions = parts[1];
                // Add term to the distinct terms set (sorted by default in TreeSet)
                distinctTerms.add(term);

//                List<String> docIds = new ArrayList<>();
//                if (parts.length > 1) {
//                    String[] numbers = parts[1].split(";");
//                    for (String num : numbers) {
//                        if (!num.isEmpty()) {
//                            docIds.add(num.substring(0,num.length()-1));
//                        }
//                    }
//                }

//                Collections.sort(term);
                postingList.put(term, docIds_with_positions);

                line = br.readLine();
            }
        } finally {
            br.close();
        }
    }


    // __________________(TF TASK)____________________

    // Method to calculate term frequency (TF) from the positional index
    public static void calculateTF(Map<String, String> positionalIndex, Map<String, String> tf) {
        // To-Do: Implement logic to calculate TF based on positional index
        for (Map.Entry<String,String> entry : positionalIndex.entrySet()){
            String term = entry.getKey();
            String postingList = entry.getValue();

            StringBuilder pl = new StringBuilder();

            String[] document = postingList.split(";");
            for (String parts : document){
                String[] part = parts.split(":");
                String docID = part[0];
                Integer positions = part[1].split(",").length;
                pl.append(docID + ":" + positions + ";");
            }

            tf.put(term,pl.toString());



        }

        System.out.println("Term Frequency : "+tf);


    }

    // __________________(TF Weight TASK)____________________

    // Method to calculate the TF weight for each term
    public static void calculateTFWeight(Map<String, String> tf, Map<String, String> tfWeight) {
        // To-Do: Implement logic to calculate the TF weight based on TF values
        for(Map.Entry<String,String> entry : tf.entrySet()){
            String term = entry.getKey();
            String documents = entry.getValue();
            Double weight = new Double(0.0);
            StringBuilder pl = new StringBuilder();

            String[] document = documents.split(";");
            for (String parts: document){
                String[] part = parts.split(":");
                String docID = part[0];
                weight = 1 + Math.log10(Integer.valueOf(part[1]));
                pl.append(docID + ":" + weight + ";");
            }

            tfWeight.put(term,pl.toString());
        }

        System.out.println("Term Frequency Weight : " + tfWeight);

    }

    // __________________(DF TASK)____________________


    // Method to calculate document frequency (DF) from the positional index
    public static void calculateDF(Map<String, String> positionalIndex, Map<String, Integer> df) {
      for (Map.Entry<String, String> entry : positionalIndex.entrySet()) {
           String term = entry.getKey();
           String documents = entry.getValue();

//          System.out.println(term + " --> "+ documents);

          // Calculate unique document count for the term
          df.put(term, documents.split(";").length);
       }

    }

    // __________________(IDF TASK)____________________

    // Method to calculate inverse document frequency (IDF) based on DF
    public static void calculateIDF(Map<String, Integer> df, int N, Map<String, Double> idf) {
        // To-Do: Implement logic to calculate IDF using DF and total number of documents N
        for(Map.Entry<String,Integer> entry : df.entrySet()){
            String term = entry.getKey();
            Integer documentF = entry.getValue();
            Double inverted = idfLog(N,documentF);
            idf.put(term,inverted);
        }
        System.out.println("Inverted Document Frequency : " + idf);
    }

    // __________________(TF-IDF TASK)____________________

    // Method to calculate TF-IDF from TF and IDF values
    public static void calculateTFIDF(Map<String, Double> tfWeight, Map<String, Double> idf, Map<String, Map<String, Double>> tf_idf) {
        // To-Do: Implement logic to calculate TF-IDF using tfWeight and IDF
    }

    // __________________(Document Weight Length TASK)____________________

    // Method to calculate the document weight length from TF-IDF values
    public static void calculateDocumentWeightLength(Map<String, Map<String, Double>> tf_idf, Map<String, Double> document_weight_length) {
        // To-Do: Implement logic to calculate the document weight length using TF-IDF
    }

    // __________________(Normalized tf.idf TASK)____________________

    // Method to normalize the TF-IDF and calculate unit vector for each document
    public static void calculateNormalizeTFIDF(Map<String, Map<String, Double>> tf_idf, Map<String, Double> document_weight_length, Map<String, Map<String, List<Double>>> unit_vector) {
        // To-Do: Implement logic to normalize TF-IDF and calculate unit vector for each document
    }

    // __________________(Calculate Similarity TASK)____________________

    // Method to calculate the similarity between two unit vectors
    public static List<String> calculateSimilarity(Map<String, List<Double>> unitVector1, Map<String, Map<String, List<Double>>> unitVector2) {
        // To-Do: Implement logic to calculate the similarity between two unit vectors
        // Hint: Use dot product and magnitude calculations

        return new ArrayList<>(); // Replace with the sorted list of document IDs
    }


}
