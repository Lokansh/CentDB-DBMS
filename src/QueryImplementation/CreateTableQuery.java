package QueryImplementation;

import authentication.model.Session;
import exceptions.ExceptionHandler;
import loggers.EventLogger;
import loggers.GeneralLogger;
import loggers.QueryLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTableQuery {
    static final String dataStoragePath = "database_storage/";

    public static Instant instant = Instant.now();

    public static boolean createTable(String query, String path) throws ExceptionHandler {
        String table = null;
        String tableName = null;
        String dbName = null;

        query = QueryOperations.removeSemiColon(query);
        Pattern tablePattern = Pattern.compile(".*create\\s+table\\s+(.*?)($|\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher result = tablePattern.matcher(query);
        if (result.find() && validateDatatype(query) ) {
            table = result.group(1);
            System.out.println(table);
            if (table.contains(".")) {
                dbName = table.split("\\.")[0];
                tableName = table.split("\\.")[1];
            }
            if(fileCreation(tableName,dbName)) {
                createSchema(query, dbName, tableName);
            }
        }

        else {
            System.out.println("Please enter a valid query");
            String logMessage = "Please enter a valid query to create a table " + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Create " , Session.getInstance().getUser().getName() ,
                    dbName,tableName,query,"Failure " , instant);
                throw new ExceptionHandler(logMessage);
        }

        return false;
    }

    public static void createSchema(String query, String dbName, String tableName){
        System.out.println("Database Name: " + dbName);
        System.out.println("Table name: " + tableName);
        String columnsubstring = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
        String printSchemacolumn = columnsubstring.replace(",", ",\n");

        String schema = "[" + tableName + "]" + "\n" + printSchemacolumn + ";" + "\n\n";

        System.out.println("directoryPath ->" + dataStoragePath);
        String schemaName = dbName + "_" + "schema";
        System.out.println(schemaName);
        String schemaPath = dataStoragePath + dbName + "/" + schemaName;
        System.out.println(schemaPath);

        if (!schemaPath.isEmpty() && !dataStoragePath.isEmpty()) {

            try {
                System.out.println("tablePath->" + schemaPath);

                File checkFile = new File(schemaPath);
                Boolean fileExist = checkFile.isFile();

                if (fileExist) {
                    try {
                        FileWriter myWriter = new FileWriter(checkFile, true);
                        myWriter.write(schema);
                        myWriter.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else {
                    File createFile = new File(schemaPath);
                    Boolean fileCreatedSuccess = createFile.createNewFile();
                    System.out.println("Schema does not exist -->" + fileCreatedSuccess);
                    FileWriter myWriter = new FileWriter(checkFile, true);
                    myWriter.write(schema);
                    myWriter.close();
                    System.out.println("Schema file exists.");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }}

    public static boolean fileCreation(String tableName, String dbName) throws ExceptionHandler {
        String tablePath = dataStoragePath + dbName + "/" + tableName;
        File filePath = new File(tablePath);

        if (!tableName.isEmpty()){
            System.out.println("tablePath->" + tablePath);

            Boolean fileExist = filePath.isFile();
            if(fileExist){
                System.out.println("Table exists");
                String logMessage = "Table exists " + " | " +
                        "Time of Execution: " + instant + "ms";
                QueryLogger.logQueryData("Create " , Session.getInstance().getUser().getName() ,
                        dbName,tableName,null ,"Failure " , instant);
                throw new ExceptionHandler(logMessage);
                //return false;
            }
            else{
                try{
                    Boolean fileCreatedSuccess = filePath.createNewFile();
                    System.out.println("Table does not exist so table file created -->" + fileCreatedSuccess);
                }
                catch (IOException e){
                    System.out.println(e);
                }
                return true;
            }
        }
        return true;
    }

    public static boolean validateDatatype(String query) throws ExceptionHandler {
        String table = null;
        String dbName = null;
        String tableName = null;
        constants_QI constant = new constants_QI();
        List < String > validDatatype = constant.dataType;

        query = QueryOperations.removeSemiColon(query);
        Pattern tablePattern = Pattern.compile(".*create\\s+table\\s+(.*?)($|\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher result = tablePattern.matcher(query);
        if (result.find()) {
            table = result.group(1);
            System.out.println(table);
        } else {
            System.out.println("Please enter a valid query");
            String logMessage = "Valid query is not entered to create a table " + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Create " , Session.getInstance().getUser().getName() ,
                    dbName,tableName,query ,"Failure " , instant);
            throw new ExceptionHandler(logMessage);
        }

        if (table.contains(".")) {
            dbName = table.split("\\.")[0];
            tableName = table.split("\\.")[1];
        }
        System.out.println("Database Name: " + dbName);
        System.out.println("Table name: " + tableName);

        //validating datatypes
        String columnsubstring = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
        List < String > splitword1 = Arrays.asList(columnsubstring.split(","));
        System.out.println(splitword1);

        for (String eachColumn:
                splitword1) {
            if (!validDatatype.contains(eachColumn.split(" ")[1].toUpperCase())) {
                System.out.println("Invalid datatype: " +eachColumn.split(" ")[1].toUpperCase());
                String logMessage = "Invalid datatype  " + " | " +
                        "Time of Execution: " + instant + "ms";
                QueryLogger.logQueryData("Create " , Session.getInstance().getUser().getName() ,
                        dbName,tableName,query ,"Failure " , instant);
                throw new ExceptionHandler(logMessage);
                //return false;
            }
            else if(eachColumn.split(" ")[1].toUpperCase(Locale.ROOT).contains("VARCHAR")){
                System.out.println("Valid datatype: " + eachColumn.split(" ")[1].toUpperCase());
                String logMessage = "Valid datatype  " + " | " +
                        "Time of Execution: " + instant + "ms";
                QueryLogger.logQueryData("Create " , Session.getInstance().getUser().getName() ,
                        dbName,tableName,query ,"Success " , instant);
            }else
            {
                System.out.println("Valid datatype: " + eachColumn.split(" ")[1].toUpperCase());
                String logMessage = "Valid datatype  " + " | " +
                        "Time of Execution: " + instant + "ms";
                QueryLogger.logQueryData("Create " , Session.getInstance().getUser().getName() ,
                        dbName,tableName,query ,"Success " , instant);
            }
        }
        return true;
    }

    /*public static void main(String[] args) {


        String userArgument = null;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Query-------");
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        System.out.println("Input query is:" + userArgument);
        createTable(userArgument,dataStoragePath);
    }*/

}
