package analytics;

import java.io.*;
import java.util.*;

public class Analytics {
    File[] files;
    File databaseFolder;
    String logFileName;
    String databaseName;
    int countQueries = 0;
    String line;
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    String[] columnSplit = null;
    int count = 0;
    HashMap<String, Integer> countQueriesByTable = new HashMap<>();
    String tableName;
    String queryType;
    String queryStatus;

    public void countQueries(String masterFolder, String username) throws IOException {
        File dir = new File(masterFolder + "\\" + username);
        files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                databaseFolder = file;
                databaseName = file.toString().split("\\\\")[file.toString().split("\\\\").length - 1];
                logFileName = databaseName + "_logs.txt";
                BufferedReader br = new BufferedReader(new FileReader(databaseFolder + "\\" + logFileName));
                StringTokenizer st1 = new StringTokenizer(br.readLine(), "\t");
                while (st1.hasMoreTokens()) {
                    columns.add(st1.nextToken());
                    count++;
                }

                if (columns.size() > 0) {
                    for (String column : columns) {
                        columnSplit = column.split("\\|");
                    }
                    while ((line = br.readLine()) != null) {
                        StringTokenizer st2 = new StringTokenizer(line, "\t");
                        for (int i = 0; i < count; i++) {
                            if (st2.hasMoreTokens()) {
                                data.add(st2.nextToken());
                            }
                        }
                    }


                }
                countQueriesByTable.forEach((tableKey, queryCount) -> {
                    System.out.println("Total " + queryCount + " Update operations are performed on " + tableKey);
                });
                br.close();
            }
            data.clear();
            countQueriesByTable.clear();
            countQueries = 0;
            System.out.println("\n");
        }
    }

    public static void main(String[] args) throws IOException {
        Analytics analytics = new Analytics();
        analytics.countQueries("centDb", "user1");
    }
}