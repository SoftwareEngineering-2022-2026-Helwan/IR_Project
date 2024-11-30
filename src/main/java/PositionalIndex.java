
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.*;
import java.util.*;

public class PositionalIndex {

    public static Set<String> distinctTerms = new TreeSet<>();
    public static Map<String, List<String>> postingList = new TreeMap<>();
    public static Map<String, Integer> df = new HashMap<>();
    public static Map <String, Map<String, Integer>> tf  = new HashMap<>();
    public static Map<String, Double> idf = new HashMap<>();
    public static Map<String, Double> tfWeight = new HashMap<>();
    public static Map<String, Map<String, Double>> tf_idf = new HashMap<>();
    public static Map<String, Double> document_weight_length = new HashMap<>();
    public static Map<String, Map<String, List<Double>>> unit_vector = new HashMap<>();
    public static Map<String, List<Double>> query_unit_vector = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String folderPath = (new File("").getAbsolutePath()) + "\\docs";
        readFileAndBuildPostingList(folderPath);

        // Print out sorted terms and posting list
        System.out.println("Distinct Terms: " + distinctTerms);
        System.out.println("Posting List: " + postingList);

//        calculateTF(postingList,tf);
//        calculateTFWeight(tf,tfWeight);
//        calculateDF(postingList,df);
//        calculateIDF(df,10,idf);
//        calculateTFIDF(tfWeight,idf,tf_idf);
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

                // Add term to the distinct terms set (sorted by default in TreeSet)
                distinctTerms.add(term);

                List<String> docIds = new ArrayList<>();
                if (parts.length > 1) {
                    String[] numbers = parts[1].split(";");
                    for (String num : numbers) {
                        if (!num.isEmpty()) {
                            docIds.add(num.substring(0,num.length()-1));
                        }
                    }
                }

                Collections.sort(docIds);
                postingList.put(term, docIds);

                line = br.readLine();
            }
        } finally {
            br.close();
        }
    }


    // __________________(TF TASK)____________________

    // Method to calculate term frequency (TF) from the positional index
    public static void calculateTF(Map<String, List<String>> positionalIndex, Map<String, Map<String, Integer>> tf) {
        // To-Do: Implement logic to calculate TF based on positional index
    }

    // __________________(TF Weight TASK)____________________

    // Method to calculate the TF weight for each term
    public static void calculateTFWeight(Map<String, Map<String, Integer>> tf, Map<String, Double> tfWeight) {
        // To-Do: Implement logic to calculate the TF weight based on TF values
    }

    // __________________(DF TASK)____________________


    // Method to calculate document frequency (DF) from the positional index
    public static void calculateDF(Map<String, List<String>> positionalIndex, Map<String, Integer> df) {
      for (Map.Entry<String, List<String>> entry : positionalIndex.entrySet()) {
           String term = entry.getKey();
           List<String> documents = entry.getValue();

          // Calculate unique document count for the term
          Set<String> uniqueDocuments = new HashSet<>(documents);
          df.put(term, uniqueDocuments.size());
       }

      System.out.println("Document Frequencies (DF): " + df);
    }

    // __________________(IDF TASK)____________________

    // Method to calculate inverse document frequency (IDF) based on DF
    public static void calculateIDF(Map<String, Integer> df, int N, Map<String, Double> idf) {
        // To-Do: Implement logic to calculate IDF using DF and total number of documents N

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
