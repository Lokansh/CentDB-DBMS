package QueryImplementation;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryOperations {
    private String dataStoragePath = "database_storage/";
    public Boolean createDatabase(String query){
        Pattern ptr = Pattern.compile("/^create database$/");
        //Matcher m;
        if (query.contains("create database")){
        //if (userArgument.matches("^create database$")) {
            String subQuery = query.replace("create database", "");
            subQuery.trim();
            String dbname = removeSemiColon(subQuery);
            System.out.println("dbname: "+ dbname);
            File theDir = new File(dataStoragePath+dbname);
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

    //Method used for use database query
    public String useDatabase(String query){
        //implement pattern matcher
        if(query.contains("use database")){
            String subQuery = query.replace("use database", "");
            subQuery.trim();
            String dbname = removeSemiColon(subQuery);
            System.out.println("dbname->"+ dbname);
            String directoryPath  = dataStoragePath+dbname;
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

    // select query
    // select * from a.b;
    // select * from b;
    // select name,age from a.b;
    public Boolean selectTableQuery(String Query){
        String database = null;
        String table = null;
        String providedColumns = null;
        Formatter fmt = new Formatter();

        // Logic to extract table name and database name
        String tablename = removeSemiColon(Query.substring(Query.indexOf("from") + 4).trim());
        if (tablename.contains(".")){
            database=tablename.split("\\.")[0];
            table=tablename.split("\\.")[1];
        }
        String tabledataStoragePath = dataStoragePath+database+"/"+table+"/";

        // Logic to extract columns
        Pattern pattern = Pattern.compile("select(.*?)from", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(Query);
        while (matcher.find()) {
            providedColumns = matcher.group(1).trim();
        }
        System.out.println(providedColumns);
        List<String> providedColumnsList = Arrays.asList(providedColumns.split(","));

        // Logic to get data from the generated path for the required columns
        List<String> data = readFile(tabledataStoragePath, providedColumns);

        int colsLength = data.get(0).split(",").length;
        int counter=0;
        for (String s: data)
        {
            while (counter<=colsLength-1){
                fmt.format("%20s ", s.split(",")[counter]);
                counter++;
            }
            counter=0;
            fmt.format("\n");
        }
        System.out.println(fmt);
        return true;
    }

    // Read and Process data file
    public List<String> readFile(String path, String columns) {
        try {
            // initialization
            String st;
            List<Integer> indexList = new ArrayList<>();
            List<String> appendData = new ArrayList<>();

            // read file through BufferedReader
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            // read header from file
            String header = br.readLine();
            List<String> headercolsList = Arrays.asList(header.split(","));
            int fileColumnsLen = headercolsList.size();

            // read given columns
            int provColumnsLen = fileColumnsLen;
            List<String> provColsList = headercolsList;
            if (!columns.equals("*")) {
                provColsList = Arrays.asList(columns.split(","));
                provColumnsLen = provColsList.size();
            }

            // if all columns not selected then below
            // logic to get the respective index for which data is to be fetched
            if (provColumnsLen!=fileColumnsLen){
                for (String s : provColsList) {
                    int index = headercolsList.indexOf(s);
                    indexList.add(index);
                }
                // adding header
                appendData.add(columns);
            }
            else {  // for all columns
                for(int i=0;i<headercolsList.size();i++){
                    indexList.add(i);
                }
                // adding header
                appendData.add(header);
            }

            // Reading file data separated by line into list
            while ((st = br.readLine()) != null) {
                String[] splitSt = st.split(",");
                String reqString = "";
                for (int t: indexList){
                    reqString+=splitSt[t]+",";
                }
                appendData.add(reqString.substring(0, reqString.length() - 1));
            }
            return appendData;
        } catch (IOException e) {System.out.println(Arrays.toString(e.getStackTrace()));}
        return null;
    }
}
