package QueryImplementation;

import java.io.File;

public class UseQuery {

    private String dataStoragePath = "database_storage/";

    //Method used for use database query
    public String useDatabase(String query){
        //implement pattern matcher
        if(query.contains("use database")){
            String subQuery = query.replace("use database", "").trim();
            String dbname = QueryOperations.removeSemiColon(subQuery);
            System.out.println("dbname->"+ dbname);
            String directoryPath  = dataStoragePath+dbname;
            System.out.println(directoryPath);
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

}
