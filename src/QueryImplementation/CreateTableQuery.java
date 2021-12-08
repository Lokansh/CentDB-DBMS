package QueryImplementation;

import sqlDump.SqlDump;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class InvalidDatatypeException extends Exception{
}
class DuplicateColumnException extends Exception{
}
class InvalidQueryException extends Exception{
}


public class CreateTableQuery {
    static final String dataStoragePath = "database_storage/";

    static List<String> columnData = new ArrayList<>();
    static List<String> columnNames = new ArrayList<>();
    static List<String> columnDatatypes = new ArrayList<>();
    static String table = null;
    static String tableName = null;
    static String dbName = null;
    private static String globalPath;

    public static boolean createTable(String query, String path) throws Exception {
        constants_QI constant  = new constants_QI();
        query = QueryOperations.removeSemiColon(query);
        globalPath = path;
        Pattern tablePattern = Pattern.compile(".*create\\s+table\\s+(.*?)($|\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher result = tablePattern.matcher(query);


        if (result.find() ) {
            table = result.group(1);
            //System.out.println(table);
            if (table.contains(".")) {
                dbName = table.split("\\.")[0];
                tableName = table.split("\\.")[1];
                //System.out.println("Database Name: " + dbName);
                //System.out.println("Table name: " + tableName);

            }
            else{
                //dbName = ;
                Pattern dbPattern = Pattern.compile(constant.EXTRACT_DB_FROM_GLOBALPATH, Pattern.DOTALL);
                Matcher dbMatcher = dbPattern.matcher(globalPath);
                //System.out.println("Database ELSE PART");
                if (dbMatcher.find()) {
                    dbName = dbMatcher.group(0).trim();
                    if(dbName.isEmpty()){
                        throw new DatabaseNotFoundException();
                    }
                } else {throw new DatabaseNotFoundException(); }
                tableName = result.group(1);
                //System.out.println("Database Name: " + dbName);
                //System.out.println("Table name: " + tableName);
            }
            if(validateColumnData(query)){
            if(fileCreation(tableName,dbName)) {
                // Piece added for Module 6
                SqlDump obj = new SqlDump();
                obj.schemaDump(query);
                // end of module 6
                createSchema(query, dbName, tableName);
            }}
        }
        else {
            System.out.println("please enter valid query");
        }

        return false;
    }
    public static void createSchema(String query, String dbName, String tableName){
        String columnsubstring = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
        String printSchemacolumn = columnsubstring.replace(",", ",\n");


        String schema = "[" + tableName + "]" + "\n" + printSchemacolumn + ";" + "\n\n";
        String schemaName = dbName + "_" + "schema";
        //System.out.println(schemaName);
        String schemaPath = dataStoragePath + dbName + "/" + schemaName;
        //System.out.println(schemaPath);

        if (!schemaPath.isEmpty() && !globalPath.isEmpty()) {
            try {
                //System.out.println("tablePath->" + schemaPath);

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
                    //System.out.println("Schema file exists..");
                    //System.out.println("Inside else");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }}

    //creating table
    public static boolean fileCreation(String tableName, String dbName){
        //System.out.println("Inside FILE CREATION...");
        String tablePath = dataStoragePath + dbName + "/" + tableName;
        File filePath = new File(tablePath);

        if (!tableName.isEmpty()){
            Boolean fileExist = filePath.isFile();
            if(fileExist){
                //System.out.println("Table exists");
                return false;
            }
            else{
                try{
                    Boolean fileCreatedSuccess = filePath.createNewFile();
                    FileWriter myWriter = new FileWriter(filePath, true);
                    myWriter.write(String.valueOf(columnNames));
                    myWriter.close();
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

    //validating column data
    public static boolean validateColumnData(String query) throws Exception {

        query = QueryOperations.removeSemiColon(query);
        Pattern tablePattern = Pattern.compile(".*create\\s+table\\s+(.*?)($|\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher result = tablePattern.matcher(query);
        if (result.find()) {
            table = result.group(1);
        } else {
            System.out.println("please enter valid query");
        }

        //validating datatypes
        String columnsubstring = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
        List < String > splitword1 = Arrays.asList(columnsubstring.split(","));

        for (int i = 0; i < splitword1.size(); i++) {
            //System.out.println(splitword1.get(i));
            columnData.add(splitword1.get(i).trim());
            }
        //System.out.println("Column data: "+columnData);

        for (String str:
             columnData) {
            if(!str.contains("PRIMARY KEY") && !str.contains("FOREIGN KEY")){
                //System.out.println("Checking primary key :    ---------"+str);

                    columnNames.add(str.split(" ")[0].toUpperCase(Locale.ROOT).trim());
                    columnDatatypes.add(str.split(" ")[1].toUpperCase(Locale.ROOT).trim());

                //System.out.println("Column names:"+columnNames);
                //System.out.println("Column datatypes: "+columnDatatypes);
                    if (str.toUpperCase(Locale.ROOT).contains("INT")  || str.toUpperCase(Locale.ROOT).contains("VARCHAR")|| str.toUpperCase(Locale.ROOT).contains("BOOLEAN")) {
                        continue;
                    } else {
                        //System.out.println(str);
                        throw new InvalidDatatypeException();
                    }
            }
            else if(str.contains("PRIMARY KEY")){
                validatePrimarykey(str);
            }
            else if(str.contains("FOREIGN KEY")){
                validateForeignkey(str);
            }
        }

        //validating duplicate column
            HashSet<String> set = new HashSet<String>(columnNames);
            if(set.size()<columnNames.size()){
                throw new DuplicateColumnException();
            }
        return true;
    }


    //validating primary key
    public static void validatePrimarykey(String primarykeyvalidation) throws Exception {
        String primarykey = null;
        primarykey = primarykeyvalidation.substring(primarykeyvalidation.indexOf("PRIMARY KEY ") + 13, primarykeyvalidation.indexOf(")"));
        //System.out.println("Primary key: "+ primarykey);
        if(!columnNames.contains(primarykey.toUpperCase(Locale.ROOT))){
            throw new Exception("Invalid primary key.");
        }
        //else{
            //System.out.println("valid ");
        //}
    }

    //Validating Foreign key
    public static void validateForeignkey(String foreignkeyvalidation) throws Exception {
        String foreignkey = null;
        String referredprimarykey = null;
        String referredTable = null;
        if (foreignkeyvalidation.contains("REFERENCES")){
            foreignkey = foreignkeyvalidation.substring(foreignkeyvalidation.indexOf("FOREIGN KEY ") + 13, foreignkeyvalidation.indexOf(")") );
            foreignkey = foreignkey.toUpperCase(Locale.ROOT);
            //System.out.println("Foreign key : "+foreignkey);
            if(!columnNames.contains(foreignkey)){
                throw new Exception("Invalid foreign key");
            }

            referredTable = foreignkeyvalidation.substring(foreignkeyvalidation.indexOf("REFERENCES ") +11 ,foreignkeyvalidation.lastIndexOf("("));
            //System.out.println("Table: "+ referredTable);
            referredprimarykey = foreignkeyvalidation.substring(foreignkeyvalidation.indexOf(referredTable) + referredTable.length() + 1 , foreignkeyvalidation.lastIndexOf(")"));
            referredprimarykey = referredprimarykey.toUpperCase(Locale.ROOT);
            //System.out.println("Foreign key reference : "+ referredprimarykey);

            String tablePath = dataStoragePath + dbName + "/" + referredTable;
            File filePath = new File(tablePath);
            //System.out.println("ReferenceTable Path: " +tablePath);

            if (!tableName.isEmpty()) {

                Boolean fileExist = filePath.isFile();
                if (fileExist) {
                    //System.out.println("Table exists");
                    BufferedReader brTest = new BufferedReader(new FileReader(filePath));
                    String filecolumnname = brTest .readLine();
                    filecolumnname = filecolumnname.trim();
                    filecolumnname = filecolumnname.substring(filecolumnname.indexOf("["),filecolumnname.indexOf("]"));
                    List < String > splitword1 = Arrays.asList(filecolumnname.split(","));
                    if(!filecolumnname.contains(referredprimarykey)){
                        throw new Exception("invalid referred column");
                    }
                }
                else{
                    throw new Exception("Tabel does not exist.");
                }
            }
        }
        else{
            throw new InvalidQueryException();
        }
    }

    //public static void main(String[] args) throws Exception {


        //String userArgument = null;
        //Scanner s = new Scanner(System.in);
        //System.out.println("Enter Query-------");
        //userArgument = s.nextLine();
        //userArgument = userArgument.trim();
        //System.out.println("Input query is:" + userArgument);
        //createTable( userArgument,"database_storage/");

        //create table db1.table1 (id int, name varchar(20));
        //CREATE TABLE Orders (OrderID int ,OrderNumber int ,PersonID int,PRIMARY KEY (OrderID),FOREIGN KEY (PersonID) REFERENCES Persons(PersonID));
        //CREATE TABLE db1.testTable1 (OrderID int ,OrderNumber int ,PersonID int);
        //CREATE TABLE Orders (OrderID int ,OrderNumber int ,PersonID int,PRIMARY KEY (OrderID));
        //CREATE TABLE db1.testTable3 (OrderID int ,OrderNumber int ,PersonID int,PRIMARY KEY (OrderID),FOREIGN KEY (PersonID) REFERENCES testTable1(PersonID));
   // }



}
