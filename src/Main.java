import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        //set default values
        String query = "title:freedom";
        String endpoint = "http://chinkapin.pti.indiana.edu:9994/solr/meta/select/";
        String outputFile = "volumeList.txt";

        if (args.length > 0) query = args[0];
        if (args.length > 1) endpoint = args[1];
        if (args.length > 2) outputFile = args[2];

        SolrApiClient solrApiClient = new SolrApiClient(endpoint);
        File file = new File(outputFile);
        FileWriter fw = null;

        // if file does not exist,then create it
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String rowsString;
        int batchNum = 0;
        int batchSize = 1000;
        int totalBatches = 0;

        HashSet<String> volSet = new HashSet<String>();
        List<String> batchVolList = new ArrayList<String>();
        String listString = "";

        try {

            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);


            System.out.println("query url: " + endpoint + "?q=" + query + "&fl=id");
            int maxRows = solrApiClient.getCount(query);
            System.out.println("Total ids found: " + maxRows);


            totalBatches = 1 + maxRows / batchSize;

            for (int i = 0; i <= (maxRows + 1); i += batchSize) {

                if (i == 0) {
                    rowsString = "&rows=" + (batchSize);
                    System.out.println("fetching up to the first " + batchSize + " ids ...");

                } else {
                    batchNum = i / batchSize;
                    rowsString = "&start=" + (batchSize * batchNum) + "&rows=" + (batchSize);
                    if ((batchNum % 10) == 0) {
                        System.out.println("fetching id " + (batchSize * batchNum) + " and up to the next " + batchSize + " ids ...");
                    }
                }

                if ((batchNum % 10) == 0) {

                    listString = solrApiClient.getCommaSeparatedValueList((query + rowsString));
                    batchVolList = Arrays.asList(listString.split("\\s*,\\s*"));
                    for (String id : batchVolList) {
                        volSet.add(id);
                    }
                }

                //bw.write(solrApiClient.getCommaSeparatedValueList((query + rowsString)));

                System.out.println("completed batch " + (batchNum + 1) + " of " + totalBatches);
            }

            for (String volId : volSet) {
                bw.write(volId);
            }

            bw.close();
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            System.out.println("xpath trouble\n");
            e.printStackTrace();
        }

    }
}
