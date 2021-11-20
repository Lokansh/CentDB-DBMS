package QueryImplementation;

import java.io.File;
import java.util.regex.Pattern;

public class QueryOperations {
    public Boolean createDatabase(String query){
        Pattern ptr = Pattern.compile("/^create database$/");
        //Matcher m;
        if (query.contains("create database")){
        //if (userArgument.matches("^create database$")) {
            String dbname = query.replace("create database", "");
            dbname = dbname.trim();
            if (!dbname.isBlank() && !dbname.isEmpty() && dbname.charAt(dbname.length() - 1) == ';') {
                dbname = dbname.substring(0, dbname.length() - 1);
            }
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


}
