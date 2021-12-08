package sqlDump;

import QueryImplementation.constants_QI;
import services.DatabaseService;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlDump {

    BufferedWriter bw;
    constants_QI constant = new constants_QI();
    public void exportDataAndStructure(Scanner scanner) {
        try {
            String databaseToExport;
            System.out.println("Enter Database name that is to be exported:");
            databaseToExport = scanner.nextLine();
            databaseToExport = databaseToExport.trim();
            prepareDataDump(databaseToExport);
            combineDataAndStructureDump(databaseToExport);
        } catch (IOException e) {e.printStackTrace();}

    }

    public void combineDataAndStructureDump(String databaseProv) throws IOException {
        try {
            BufferedWriter bw_data_struct = new BufferedWriter(new FileWriter(constant.SQLDUMP_PATH + databaseProv + "_schemaAndData.sql", false));
            bw_data_struct.write("--------------------------------------\n" +
                    "-- SQL SCHEMA EXPORT FOR DATABASE: `" + databaseProv + "`\n" +
                    "--------------------------------------\n\n");
            bw_data_struct.write("USE DATABASE " + databaseProv + ";\n");
            BufferedReader br = new BufferedReader(new FileReader(constant.ARCHIVE_DUMP + databaseProv + "_schema.sql"));
            String line = br.readLine();
            while (line != null) {
                bw_data_struct.write(line);
                bw_data_struct.newLine();
                line = br.readLine();
            }
            br = new BufferedReader(new FileReader(constant.ARCHIVE_DUMP + databaseProv + "_dump.sql"));
            line = br.readLine();
            while (line != null) {
                bw_data_struct.write(line);
                bw_data_struct.newLine();
                line = br.readLine();
            }

            bw_data_struct.flush();

            // closing resources
            br.close();
            bw_data_struct.close();

            System.out.println("SQL Data + Structure Dump exported successfully. Please refer:\n"+constant.SQLDUMP_PATH + databaseProv + "_schemaAndData.sql");
        } catch (FileNotFoundException e) {System.out.println("No Tables found for the mentioned database.");}
    }

    // fetch data for all the tables present within the database
    public void prepareDataDump(String database) throws IOException {
        String dbFolderPath = "database_storage/"+database+"/";
        File folder = new File(dbFolderPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            bw = new BufferedWriter(new FileWriter(constant.ARCHIVE_DUMP + database + "_dump.sql", false));
            bw.write("--------------------------------------\n" +
                    "-- SQL DATA EXPORT FOR DATABASE: `" + database + "`\n" +
                    "--------------------------------------\n\n");
            bw.write("USE DATABASE " + database + ";\n");


            // get the data dump
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    //System.out.println(file.getName());
                    String tableName = file.getName();
                    String tbFilePath = dbFolderPath + tableName + "/";
                    readFile(tbFilePath, tableName);
                }
                //System.out.println("********************");
            }
            bw.close();
        } else {System.out.println("No Tables found in the database mentioned.");}
    }

    // read the datafiles
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
                    //System.out.println(line);
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


    // create a separate schema dump at the time of create query for the respective database
    public void schemaDump(String createQuery){
        String database;

        try {
            // extract the database from the query
            Pattern dbTBPattern = Pattern.compile("(?<=create table\\s).*(?=\\.)", Pattern.DOTALL);
            Matcher dbTBMatcher = dbTBPattern.matcher(createQuery);
            if (dbTBMatcher.find()){
                database = dbTBMatcher.group(0).trim();
            } else {database="db2";}        // XXX to be removed later
            BufferedWriter bwq = new BufferedWriter(
                    new FileWriter(constant.ARCHIVE_DUMP + database+ "_schema.sql",
                            true));
            bwq.write(createQuery+"\n");
            bwq.close();

        } catch(IOException e){e.getStackTrace();}
    }

    public static void main(String[] args) throws IOException {
        SqlDump obj = new SqlDump();
        //obj.prepareDataDump("db1");
        //obj.schemaDump("create table db1.tb1 (id int(10), varchar(1));");
        Scanner s = new Scanner(System.in);
        obj.exportDataAndStructure(s);
    }

}