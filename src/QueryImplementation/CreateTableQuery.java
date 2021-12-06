package QueryImplementation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTableQuery {
    static final String dataStoragePath = "database_storage/";

    public static boolean createTable(String query, String path) {
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
            System.out.println("please enter valid query");
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
                    System.out.println("Schema file exists..");
                    System.out.println("Inside else");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }}

    public static boolean fileCreation(String tableName, String dbName){
        String tablePath = dataStoragePath + dbName + "/" + tableName;
        File filePath = new File(tablePath);

        if (!tableName.isEmpty()){
            System.out.println("tablePath->" + tablePath);

            Boolean fileExist = filePath.isFile();
            if(fileExist){
                System.out.println("Table exist");
                return false;
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

    public static boolean validateDatatype(String query) {
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
            System.out.println("please enter valid query");
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
                System.out.println("invalid datatype: " +eachColumn.split(" ")[1].toUpperCase());
                return false;
            }
            else if(eachColumn.split(" ")[1].toUpperCase(Locale.ROOT).contains("VARCHAR")){
                System.out.println("Valid datatype: " + eachColumn.split(" ")[1].toUpperCase());
            }else
            {
                System.out.println("Valid datatype: " + eachColumn.split(" ")[1].toUpperCase());
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
