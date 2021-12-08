package QueryImplementation;

import authentication.model.Session;
import exceptions.ExceptionHandler;
import loggers.QueryLogger;

import javax.imageio.IIOException;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.io.IOException;
import java.util.regex.Pattern;

import static QueryImplementation.CreateTableQuery.createTable;
import static QueryImplementation.DropTableQuery.dropTable;

public class QueryOperations {
    private String dataStoragePath = "database_storage/";
    public Instant instant = Instant.now();
    public Boolean createDatabase(String query) throws ExceptionHandler {
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
                String logMessage = "New database folder created  " + " | " +
                        "Time of Execution: " + instant + "ms";
                QueryLogger.logQueryData("Query Operation  " , Session.getInstance().getUser().getName() ,
                        dbname,null,query ,"Success " , instant);
                return true;
            }
            else {
                System.out.println("Database already present: "+dbname);
                String logMessage = "New database folder created  " + " | " +
                        "Time of Execution: " + instant + "ms";
                QueryLogger.logQueryData("Query Operation  " , Session.getInstance().getUser().getName() ,
                        dbname,null,query ,"Failure " , instant);
                throw new ExceptionHandler(logMessage);
                //return false;
            }
        } else {
            System.out.println("Invalid Query Received");
            String logMessage = "Invalid Query Received  " + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Query Operation  " , Session.getInstance().getUser().getName() ,
                    null,null,query ,"Failure " , instant);
            throw new ExceptionHandler(logMessage);
            //return false;
        }
    }
    public boolean createSchema(String query, String path) throws Exception {
        if(query.contains("create table")){
            createTable(query,path);
        }
        return true;
    }
    public boolean dropTableQuery(String query, String path) throws ExceptionHandler {
        if(query.contains("drop table")){
            dropTable(query,path);
        }
        return true;
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
        String provWhereClause = null;
        String tablename = null;
        Formatter fmt = new Formatter();

        // converting query to lower case
        //Query = Query.toLowerCase();

        // Logic to extract table name and database name
        Pattern patternDBTable;
        if (Query.contains("where")){
            patternDBTable = Pattern.compile("(?<=from)(.*)(?=where)", Pattern.DOTALL);
        } else {
            patternDBTable = Pattern.compile("(?<=from)(.*)(?=;|)", Pattern.DOTALL);
        }
        Matcher matcherDBTable = patternDBTable.matcher(Query);
        while (matcherDBTable.find()) {
            tablename = removeSemiColon(matcherDBTable.group(0).trim());
        }
        System.out.println("TableName: "+tablename);

        //String tablename = removeSemiColon(Query.substring(Query.indexOf("from") + 4).trim());
        if (tablename.contains(".")){
            database=tablename.split("\\.")[0];
            table=tablename.split("\\.")[1];
        }
        String tabledataStoragePath = dataStoragePath+database+"/"+table+"/";
        System.out.println("Table data location: "+tabledataStoragePath);

        // Logic to check if where clause is present
        Pattern patternWhereClause = Pattern.compile("(?<=where).*$", Pattern.DOTALL);
        Matcher matcherWhere = patternWhereClause.matcher(Query);
        while (matcherWhere.find()) {
            provWhereClause = matcherWhere.group(0).trim();
        }
        System.out.println("Filter Condition: "+provWhereClause);

        // Logic to extract columns
        Pattern patternColumns = Pattern.compile("select(.*?)from", Pattern.DOTALL);
        Matcher matcherColumns = patternColumns.matcher(Query);
        while (matcherColumns.find()) {
            providedColumns = matcherColumns.group(1).trim();
        }
        System.out.println("Columns Required: "+providedColumns);

        List<String> providedColumnsList = Arrays.asList(providedColumns.split(","));

        // Logic to get data from the generated path for the required columns
        List<String> data = readFile(tabledataStoragePath, providedColumns, provWhereClause);

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
    public List<String> readFile(String path, String columns, String filter) {
        try {
            // initialization
            String st;
            Pattern patternWhereKey = null;
            Pattern patternWhereValue = null;
            List<Integer> indexList = new ArrayList<>();
            List<String> appendData = new ArrayList<>();

            // read file through BufferedReader
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            // read header from file
            String header = br.readLine();
            List<String> headercolsList = Arrays.asList(header.split(","));
            int fileColumnsLen = headercolsList.size();

            // read filter column position
            int filterColPos = 0;
            String filterColumn=null;
            String filterValue=null;
            if (filter != null) {
                patternWhereKey = Pattern.compile("^[^=]+", Pattern.DOTALL);
                patternWhereValue = Pattern.compile("(?<==)(.*)(?=|;)", Pattern.DOTALL);
                Matcher matcherWhereKey = patternWhereKey.matcher(filter);
                Matcher matcherWhereValue = patternWhereValue.matcher(filter);
                while (matcherWhereKey.find()) {
                    filterColumn = matcherWhereKey.group(0).trim();
                }
                while (matcherWhereValue.find()) {
                    filterValue = removeSemiColon(matcherWhereValue.group(0).trim()).replace("\"", "");;
                }
                filterColPos = headercolsList.indexOf(filterColumn);
            }

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
                if (filter == null) {
                    // when select without where clause
                    for (int t : indexList) {
                        reqString += splitSt[t] + ",";
                    }
                    appendData.add(reqString.substring(0, reqString.length() - 1));
                }
                else {
                    // when select with where clause
                    if (splitSt[filterColPos].equals(filterValue)){  // XXX hardcoded needs to be removed
                        // when select without where clause
                        for (int t : indexList) {
                            reqString += splitSt[t] + ",";
                        }
                        appendData.add(reqString.substring(0, reqString.length() - 1));
                    }
                }
            }
            return appendData;
        } catch (IOException e) {System.out.println(Arrays.toString(e.getStackTrace()));}
        return null;
    }
}
