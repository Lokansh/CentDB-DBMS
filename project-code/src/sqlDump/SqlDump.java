package sqlDump;

import java.io.*;

public class SqlDump {

    BufferedWriter bw;
    public void fetchData(String database) throws IOException {
        String dbFolderPath = "database_storage/"+database+"/";
        File folder = new File(dbFolderPath);
        File[] listOfFiles = folder.listFiles();
        bw = new BufferedWriter(new FileWriter("sqldump.sql", false));
        bw.write("--------------------------------------\n" +
                "-- SQL EXPORT FOR DATABASE: `"+database+"`\n" +
                "--------------------------------------\n\n");
        bw.write("USE DATABASE "+database+";\n");
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
                String tableName = file.getName();
                String tbFilePath = dbFolderPath + tableName + "/";
                readFile(tbFilePath, tableName);
            }
            System.out.println("********************");
        }
        bw.close();
    }


    public void readFile(String tbFilePath, String tableName){
        String line;
        int header=0;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(tbFilePath));

            bw.write("--\n" +
                    "-- Dumping data for table `"+tableName+"`\n" +
                    "--\n\n");
            bw.write("LOCK TABLES `"+tableName+"` WRITE;\n");

            while ((line=br.readLine()) != null) {
                if(header==0){
                    // header
                    header++;
                }
                else {
                    bw.write("INSERT INTO `"+tableName+"` VALUES ");
                    String row = line.replace(",","','");
                    bw.write("('"+row+"');\n");
                    System.out.println(line);
                }
            }
            bw.write("UNLOCK TABLES;\n");
            bw.write("\n");
            // end of the data

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                if (br != null)
                    br.close();
            }
            catch (IOException e)
            {
                System.out.println("Error in closing the BufferedReader");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SqlDump obj = new SqlDump();
        obj.fetchData("db1");
    }
    
}