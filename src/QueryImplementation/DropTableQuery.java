package QueryImplementation;

import exceptions.ExceptionHandler;
import loggers.GeneralLogger;

import java.io.File;
import java.time.Instant;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropTableQuery {
    static final String dataStoragePath = "database_storage/";

    public static Instant instant = Instant.now();
    public static void dropTable(String query, String globalPath) throws ExceptionHandler {
        final String dataStoragePath = "database_storage/";
        String table = null;
        String dbName = null;
        String tableName = null;
        query = QueryOperations.removeSemiColon(query);
        Pattern tablePattern = Pattern.compile(".*drop\\s+table\\s+(.*?)($|\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher result = tablePattern.matcher(query);
        if (result.find()) {
            table = result.group(1);
            //System.out.println(table);
        } else {
            //System.out.println("Please enter a valid query");
            String logMessage = "Please enter a valid query to drop the table" + " | " +
                    "Time of Execution: " + instant + "ms";
            GeneralLogger.logGeneralData(query,logMessage);
            throw new ExceptionHandler(logMessage);
        }
        if (table.contains(".")) {
            dbName = table.split("\\.")[0];
            tableName = table.split("\\.")[1];
        }
        //System.out.println("Database Name: " + dbName);
        //System.out.println("Table name: " + tableName);
        String tablePath = dataStoragePath + "/" + dbName + "/" + tableName /*+ ".txt"*/;
        try {
            File f = new File(tablePath);           //file to be delete
            if (f.delete())                      //returns Boolean value
            {
                System.out.println(f.getName() + " Drop Successful");   //getting and printing the file name
                String logMessage = "Drop Successful" + " | " +
                        "Time of Execution: " + instant + "ms";
                GeneralLogger.logGeneralData(query,logMessage);
            } else {
                System.out.println("Drop Failed");
                String logMessage = "Drop Failed" + " | " +
                        "Time of Execution: " + instant + "ms";
                GeneralLogger.logGeneralData(query,logMessage);
                throw new ExceptionHandler(logMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*public static void main(String[] args){
        String userArgument = null;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Query-------");
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        System.out.println("Input query is:" + userArgument);
        dropTable(userArgument,dataStoragePath);
    }*/
}
