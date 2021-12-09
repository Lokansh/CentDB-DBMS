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

    List<String> columnData = new ArrayList<>();
    List<String> columnNames = new ArrayList<>();
    List<String> columnDatatypes = new ArrayList<>();
    String table = null;
    String tableName = null;
    String dbName = null;
    private  String globalPath;

    public  boolean createTable(String query, String path) throws Exception {
        constants_QI constant  = new constants_QI();
        query = QueryOperations.removeSemiColon(query);
        System.out.println(path);
        globalPath = path;
        Pattern tablePattern = Pattern.compile(".*create\\s+table\\s+(.*?)($|\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher result = tablePattern.matcher(query);


        if (result.find() ) {
            table = result.group(1);

                Pattern dbPattern = Pattern.compile(constant.EXTRACT_DB_FROM_GLOBALPATH, Pattern.DOTALL);
                Matcher dbMatcher = dbPattern.matcher(path);
                //System.out.println("Database ELSE PART");
                if (dbMatcher.find()) {
                    dbName = dbMatcher.group(0).trim();
                    //globalPath = "database_storage/";
                    //System.out.println("USE query:" + dbName);
                    if(dbName.isEmpty()){
                        throw new DatabaseNotFoundException();
                    }
                } else {throw new DatabaseNotFoundException(); }
                tableName = result.group(1);
                //System.out.println("Database Name: " + dbName);
                //System.out.println("Table name: " + tableName);
            //}
            if(validateColumnData(query)){
                if(fileCreation(tableName,dbName)) {
                    createSchema(query, dbName, tableName);
                    System.out.println("Creating schema");
                    // Piece added for Module 6
                    SqlDump obj = new SqlDump();
                    obj.schemaDump(query);
                    // end of module 6

                }}
        }
        else {
            System.out.println("please enter valid query");
        }

        return false;
    }
    public  void createSchema(String query, String dbName, String tableName){
        String columnsubstring = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
        String columnData = columnsubstring.replace(",", ",\n");
        String[] columns = columnData.split(",\n");
        List<String>  listcolumn = new ArrayList<>();
        for (String str:
                columns) {
            listcolumn.add(str.trim());
        }
        String printSchemacolumn = String.join(",\n",listcolumn);
      
        String schema = "\n[" + tableName + "]" + "\n" + printSchemacolumn + ";" + "\n";
        String schemaName = dbName + "_" + "schema";
        //System.out.println(schemaName);
        String schemaPath = globalPath + "/" + schemaName;
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
                    System.out.println("Schema file created: " + fileCreatedSuccess);
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
    public  boolean fileCreation(String tableName, String dbName) throws Exception {
        //System.out.println("Inside FILE CREATION...");
        String tablePath = globalPath + "/" + tableName;
        File filePath = new File(tablePath);
        //List<String> firstlinedata = new ArrayList<>();
        String printfirstline  = "";
        for (int i = 0; i < columnNames.size(); i++) {
            String str = columnNames.get(i);
            //firstlinedata.add(str.toLowerCase(Locale.ROOT));

            if(i==columnNames.size()-1){
                printfirstline = printfirstline+str.toLowerCase(Locale.ROOT);
            }
            else {
            printfirstline = printfirstline+str.toLowerCase(Locale.ROOT) + ",";}
        }

        if (!tableName.isEmpty()){
            Boolean fileExist = filePath.isFile();
            if(fileExist){
                System.out.println("Table already exists");
                return false;
            }
            else{
                try{
                    Boolean fileCreatedSuccess = filePath.createNewFile();
                    FileWriter myWriter = new FileWriter(filePath, true);
                    myWriter.write(printfirstline);
                    myWriter.close();
                    System.out.println("Table created -->" + fileCreatedSuccess);
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
    public  boolean validateColumnData(String query) throws Exception {

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
                    System.out.println("");
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
            System.out.println("Duplicate columns");
            throw new DuplicateColumnException();
        }
        System.out.println("Valid columnData.");
        return true;
    }


    //validating primary key
    public  void validatePrimarykey(String primarykeyvalidation) throws Exception {
        String primarykey = null;
        primarykey = primarykeyvalidation.substring(primarykeyvalidation.indexOf("PRIMARY KEY ") + 13, primarykeyvalidation.indexOf(")"));
        System.out.println("Primary key: "+ primarykey);
        if(!columnNames.contains(primarykey.toUpperCase(Locale.ROOT))){
            System.out.println("Invalid primary key");
            throw new Exception("Invalid primary key.");
        }
        //else{
        //System.out.println("valid ");
        //}
    }

    //Validating Foreign key
    public  void validateForeignkey(String foreignkeyvalidation) throws Exception {
        String foreignkey = null;
        String referredprimarykey = null;
        String referredTable = null;
        if (foreignkeyvalidation.contains("REFERENCES")){
            foreignkey = foreignkeyvalidation.substring(foreignkeyvalidation.indexOf("FOREIGN KEY ") + 13, foreignkeyvalidation.indexOf(")") );
            foreignkey = foreignkey.toUpperCase(Locale.ROOT);
            System.out.println("Foreign key : "+foreignkey);
            if(!columnNames.contains(foreignkey)){
                System.out.println("Invalid foreign key");
                throw new Exception("Invalid foreign key");
            }

            referredTable = foreignkeyvalidation.substring(foreignkeyvalidation.indexOf("REFERENCES ") +11 ,foreignkeyvalidation.lastIndexOf("("));
            System.out.println("Table: "+ referredTable);
            referredprimarykey = foreignkeyvalidation.substring(foreignkeyvalidation.indexOf(referredTable) + referredTable.length() + 1 , foreignkeyvalidation.lastIndexOf(")"));
            //referredprimarykey = referredprimarykey;
            System.out.println("Foreign key reference : "+ referredprimarykey);

            String tablePath = globalPath+ "/" + referredTable;
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
                        System.out.println("Invalid reference...");
                        throw new Exception("invalid referred column");
                    }

                }
                else{
                    System.out.println("Tabel does not exist.");
                    throw new Exception("Tabel does not exist.");
                }
            }
        }
        else{

            throw new InvalidQueryException();
        }
    }
}
