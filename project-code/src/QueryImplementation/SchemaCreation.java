package QueryImplementation;

import com.sun.tools.javac.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchemaCreation {
    public static boolean schemaCreation(String query,String path) throws IOException{
        String tableName = null;
        String directoryPath = path;
        //.*create\s+table\s+(.*?)($|\s+)
        //Pattern p = Pattern.compile(".*FROM\\s+(.*?)($|\\s+[WHERE,JOIN,START\\s+WITH,ORDER\\s+BY,GROUP\\s+BY])", Pattern.CASE_INSENSITIVE); for select query
        //table name
       query = QueryOperations.removeSemiColon(query);
        Pattern tablePattern = Pattern.compile(".*create\\s+table\\s+(.*?)($|\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher result = tablePattern.matcher(query);
        if (result.find()) {
            tableName = result.group(1);
            //System.out.println(tableName);
        }
        else{
            System.out.println("please enter valid query");
        }
        String columnsubstring = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
        String printSchemacolumn = columnsubstring.replace(",",",\n");
        //System.out.println(printSchemacolumn);

        String schema = "["+tableName+"]"+"\n"+printSchemacolumn;

        System.out.println("directoryPath ->" + directoryPath);
        String schemaPath = directoryPath + "/" +  "schema.txt";

        if (!schemaPath.isEmpty() && !directoryPath.isEmpty()){
            System.out.println("tablePath->" + schemaPath);

            File checkFile = new File(schemaPath);
            Boolean fileExist = checkFile.isFile();
            if(fileExist){
                System.out.println("Schema file exists..");
                try {
                    FileWriter myWriter = new FileWriter(checkFile);
                    myWriter.write(schema);
                    myWriter.close();
                    //System.out.println(schema + "Done");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
            else{
                File createFile = new File(schemaPath);
                Boolean fileCreatedSuccess = createFile.createNewFile();
                System.out.println("Schema does not exist -->" + fileCreatedSuccess);
            }
            System.out.println("File created or file exists operation complete");
        }
        else if(directoryPath.isEmpty()){
            System.out.println("Database not selected, please select database before inserting any values");
            return false;
        }
        else if(tableName.isEmpty()){
            System.out.println("Wrong query entered, please re-check");
            return false;
        }
        String tablePath = directoryPath + "/" + tableName + ".txt";

        if (!tableName.isEmpty() && !directoryPath.isEmpty()){
            System.out.println("tablePath->" + tablePath);

            File checkFile = new File(tablePath);
            Boolean fileExist = checkFile.isFile();
            if(fileExist) {
                System.out.println("Table exist");
            }
        }

        return false;
    }
    public static void main(String[] args){
        String userArgument = null;
        Scanner s = new Scanner(System.in);
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        //String q = "create table table1 (id int(10), name varchar(25))";
        //Boolean b = schemaCreation(userArgument);

    }
}



















 /* String[] animalsArray = columnsubstring.split(",");
        for (String str:
             animalsArray) {
            System.out.println(str);
        }*/

        /*System.out.println(query.indexOf("("));
        System.out.println(query.length());
        System.out.println(query);
        String variables = query.substring(query.indexOf("(") , query.lastIndexOf(")"));
        System.out.println(variables);*/



        /*Pattern p=Pattern.compile(,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(query);
        boolean b = m.matches();
        System.out.println(b);*/
        /*Pattern r = new Pattern("(exists|table)s+(?<table>S+)");

        Matcher m = r.Matcher(query);
        while (m.Success) {
            Console.WriteLine (m.Groups["table"].Value);
            m = m.NextMatch();
        }*/




        /*Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(sql);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
        } else {
            System.out.println("NO MATCH");
        }*/
