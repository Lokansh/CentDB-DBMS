package QueryImplementation;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class QueryOperations {
    public Boolean createDatabase(String query){
        Pattern ptr = Pattern.compile("/^create database$/");
        //Matcher m;
        if (query.contains("create database")){
        //if (userArgument.matches("^create database$")) {
            String subQuery = query.replace("create database", "");
            subQuery.trim();
            String dbname = removeSemiColon(subQuery);
            System.out.println("dbname: "+ dbname);
            File theDir = new File("database_storage/"+dbname);
            if (!theDir.exists()){
                theDir.mkdirs();
                System.out.println("New database folder created: "+dbname);
                return true;
            }
            else {
                System.out.println("Database already present: "+dbname);
                return false;
            }
        } else {
            System.out.println("Invalid Query Received");
            return false;
        }
    }
    public boolean createSchema(String query, String path) throws IOException {
        if(query.contains("create table")){
            SchemaCreation.schemaCreation(query,path);
        }
        return true;
    }

    //Method used for use database query
    public String useDatabase(String query){
        //implement pattern matcher
        if(query.contains("use database")){
            String subQuery = query.replace("use database", "");
            subQuery.trim();
            String dbname = removeSemiColon(subQuery);
            System.out.println("dbname->"+ dbname);
            String directoryPath  = "database_storage/"+dbname;
            //System.out.println(directoryPath);
            File theDir = new File(directoryPath);
            if (theDir.exists()){
                System.out.println("directory changed after use database query");
                return directoryPath;
            }else{
                System.out.println("Database not present, please create database before using it");
                return null;
            }
        }
        return null;
    }

    // Generic Method for removing Semi Colon from end of the string
public static String removeSemiColon(String inputString){
        String outputString = "";
        if (!inputString.isBlank() && !inputString.isEmpty() && inputString.charAt(inputString.length() - 1) == ';') {
            outputString = inputString.substring(0, inputString.length() - 1);
        }
        else {
            outputString = inputString;
        }
        return outputString.trim();
    }


}
