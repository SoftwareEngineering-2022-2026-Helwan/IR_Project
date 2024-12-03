
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
    public static Map<String, String> tf_idf = new TreeMap<>();
    public static Map<String, Double> document_weight_length = new TreeMap<>();
    public static Map<String, String> unit_vector = new TreeMap<>();
    public static Map<String, String> query_unit_vector = new TreeMap<>();

    public static void main(String[] args) throws IOException {
        String folderPath = (new File("").getAbsolutePath()) + "\\docs";
        readFileAndBuildPostingList(folderPath);

        // Print out sorted terms and posting list
        System.out.println("Distinct Terms: " + distinctTerms);
        System.out.println("Posting List: " + postingList);

        calculateTF(postingList,tf);
        calculateTFWeight(tf,tfWeight);
        calculateDF(postingList,df);
        calculateIDF(df,10,idf);
        calculateTFIDF(tfWeight,idf,tf_idf);
        calculateDocumentWeightLength(tf_idf,document_weight_length);
        calculateNormalizeTFIDF(tf_idf,document_weight_length,unit_vector);
//        query_unit_vector.put("fools","0:0.518");
//        query_unit_vector.put("fear","1:0.6807");
//        query_unit_vector.put("in","2:0.518");
        String ans;
        do {
        System.out.print("0) To Exit\nQuery: ");
        Scanner in = new Scanner(System.in);
        ans = in.nextLine();
        if(ans.contains("0"))
        {
            System.out.println("Exiting.....");
            return;
        }
        query_unit_vector = query(ans);
        calculateSimilarity(query_unit_vector,unit_vector);

        }while (!ans.contains("0"));

    }

    public static Double documentRounder(Double value)
    {
        return  new BigDecimal(value).setScale(7, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double tf_idfRounder(Double value)
    {
        return  new BigDecimal(value).setScale(6, RoundingMode.HALF_UP).doubleValue();
    }
    public static Double scoreRounder(Double value)
    {
        return  new BigDecimal(value).setScale(5, RoundingMode.HALF_UP).doubleValue();
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
      System.out.println("Document Frequency : "+ df);

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
    public static void calculateTFIDF(Map<String, String> tfWeight, Map<String, Double> idf, Map<String, String> tf_idf) {
        // To-Do: Implement logic to calculate TF-IDF using tfWeight and IDF
        for(Map.Entry<String,String> entry : tfWeight.entrySet()){
            String term = entry.getKey();
            String documents = entry.getValue();
            String[] document = documents.split(";");
            StringBuilder pl = new StringBuilder();
            for(String parts: document){
                String[] part = parts.split(":");
                String docID = part[0];
                String weight = part[1];
                Double inverted = idf.get(term);
                Double result = tf_idfRounder(Double.valueOf(weight) * inverted);
                pl.append(docID + ":" + result + ";");
            }

            tf_idf.put(term, pl.toString());
        }
        System.out.println("TF.IDF : "+ tf_idf);

    }

    // __________________(Document Weight Length TASK)____________________

    // Method to calculate the document weight length from TF-IDF values
    public static void calculateDocumentWeightLength(Map<String, String> tf_idf, Map<String, Double> document_weight_length) {
        // To-Do: Implement logic to calculate the document weight length using TF-IDF
        Map<String,Double> docLen = new TreeMap<>();
        for(Map.Entry<String,String> entry : tf_idf.entrySet()){
            String documents = entry.getValue();
            for (String document : documents.split(";")){
                String[] parts = document.split(":");
                String docID = parts[0];
                String term_inverted = parts[1];
                if(!docLen.containsKey(docID)){
                    docLen.put(docID,Math.pow(Double.valueOf(term_inverted),2));
                }
                else {
                    Double value = docLen.get(docID);
                    value += Math.pow(Double.valueOf(term_inverted),2);
                    docLen.replace(docID,value);
                }
            }
            for(Map.Entry<String,Double> entry2 : docLen.entrySet()){
                String docID = entry2.getKey();
                Double result = documentRounder(Math.sqrt(entry2.getValue()));
                document_weight_length.put(docID,result);
            }

        }

        System.out.print("Document Length : ");
        sortLength(document_weight_length);
    }

    // __________________(Normalized tf.idf TASK)____________________

    // Method to normalize the TF-IDF and calculate unit vector for each document
    public static void calculateNormalizeTFIDF(Map<String, String> tf_idf, Map<String, Double> document_weight_length, Map<String, String> unit_vector) {
        // To-Do: Implement logic to normalize TF-IDF and calculate unit vector for each document
        for(Map.Entry<String,String> entry : tf_idf.entrySet()){
            String term = entry.getKey();
            String term_inverted = entry.getValue();
            StringBuilder pl = new StringBuilder();
            for (String i : term_inverted.split(";")){
                String docID = i.split(":")[0];
                Double result = tf_idfRounder(Double.valueOf(i.split(":")[1]) / Double.valueOf(document_weight_length.get(docID)));
                pl.append(docID + ":" + result + ";");
            }
            unit_vector.put(term,pl.toString());

        }
        System.out.println("Unit Vector : " + unit_vector);
    }

    // __________________(Calculate Similarity TASK)____________________

    // Method to calculate the similarity between two unit vectors
    public static void calculateSimilarity(Map<String, String> unitVector1, Map<String, String> unitVector2) {
        // To-Do: Implement logic to calculate the similarity between two unit vectors
        // Hint: Use dot product and magnitude calculations
        //query_unit_vector.put("in","2:0.518");
        Map<String,Map<String,Double>> documentsRank = new TreeMap<>();

        for(Map.Entry<String,String> entry : unitVector1.entrySet()){
            String term = entry.getKey();
            Double uv1 = Double.valueOf(entry.getValue().split(":")[1]);

            for (String i: unitVector2.get(term).split(";")){
                if(!documentsRank.containsKey(term))
                {
                    documentsRank.put(term, new TreeMap<>());
                }

                Double uv2 = Double.parseDouble(i.split(":")[1]);
                Double dotProduct = uv1 * uv2;
                String docID = i.split(":")[0];

                if(!documentsRank.get(term).containsKey(docID))
                {
                    documentsRank.get(term).put(docID,0.0);
                }

                Double previousProduct = documentsRank.get(term).get(docID);
                documentsRank.get(term).replace(docID,previousProduct + dotProduct);
            }



        }

        // sum all dot product for same doc id that is common in all of them
        Map<String,Double> documentScore = new TreeMap<>();

        for (Map.Entry<String, Map<String, Double>> entry: documentsRank.entrySet())
        {
            for(Map.Entry<String,Double> doc : entry.getValue().entrySet())
            {
                if(!isCommon(doc.getKey(),documentsRank, unitVector1))
                {
                    continue;
                }
                if(documentScore.isEmpty() || !documentScore.containsKey(doc.getKey()))
                {
                    documentScore.put(doc.getKey(),0.0);
                }
                Double prevValue = documentScore.get(doc.getKey());
                documentScore.replace(doc.getKey(),scoreRounder(prevValue + doc.getValue()));
            }

        }
        System.out.print("Related Documents With Score: ");
        sort(documentScore);
    }

    public static void sort(Map<String, Double> documentScore)
    {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(documentScore.entrySet());

        // Sort the List by values in descending order
        Collections.sort(entryList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Reconstruct the Map in sorted order
        Map<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        StringBuilder docs = new StringBuilder();
        docs.append("Result: ");
        for (Map.Entry<String,Double>entry: sortedMap.entrySet())
        {
            docs.append(entry.getKey()).append(" ");
        }
        System.out.println(sortedMap);
        System.out.println(docs);
    }

    public static void sortLength(Map<String, Double> documentScore)
    {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(documentScore.entrySet());

        // Sort the List by values in descending order
        Collections.sort(entryList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                Integer k1 = Integer.valueOf(o1.getKey());
                Integer k2 = Integer.valueOf(o2.getKey());

                return k1.compareTo(k2);
            }
        });

        // Reconstruct the Map in sorted order
        Map<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        System.out.println(sortedMap);
    }
    public static boolean isCommon(String key, Map<String,Map<String,Double>> documentsRank , Map<String,String> query)
    {

        int actual = 0;
        int expected = query.size();
        for (Map.Entry<String,String> entry: query.entrySet())
        {
            for (Map.Entry<String, Double> doc: documentsRank.get(entry.getKey()).entrySet())
            {

                if(Objects.equals(key, doc.getKey()))
                {
                    actual++;
                    break;
                }
            }

        }

        return actual == expected ;
    }



    public static Map<String, String> query(String queryString){
        String[] queryStringArray = queryString.split(" ");
        ArrayList<String> terms = new ArrayList<>();
        Map<String,String> Result = new HashMap<>();
        Collections.addAll(terms, queryStringArray);
        normalized(terms);

        for(Map.Entry<String,Double> entry : normalized(terms).entrySet()){
            Result.put(entry.getKey(), queryTF(terms).get(entry.getKey()) + ":" + String.format("%.4f", entry.getValue()));
        }
        return Result;

    }

    public static Map<String,Integer> queryTF(ArrayList<String> terms){  // Calculates The Term Frequency in the query
        Map<String,Integer> termFerq = new HashMap<>();
        for (String element : terms) {
            termFerq.put(element, termFerq.getOrDefault(element, 0) + 1);
        }
        return termFerq;
    }

    public static Map<String,Double> normalized (ArrayList<String> terms){
        Map<String,Double> TermWT = new HashMap<>();  // A HashMap to Store Each distinct Term and its corresponding tf_idf
        Set<String> distinctTerms = new HashSet<>();
        distinctTerms.addAll(terms);
        double tf_idf;
        for(String i : distinctTerms){
            tf_idf = (Math.log10(queryTF(terms).get(i)) + 1) * idf.get(i); // THE IDF IS CALLED HERE!!!!
            TermWT.put(i,tf_idf);
            tf_idf = 0;
        }
        double length = 0;
        for(Double n : TermWT.values()){
            length += n * n;
        }
        length = Math.sqrt(length);
        Map<String,Double> normalization = new HashMap<>();
        for(Map.Entry<String,Double> entry : TermWT.entrySet()){
            normalization.put(entry.getKey(),(entry.getValue()/length));
        }
        return normalization;
    }


}
