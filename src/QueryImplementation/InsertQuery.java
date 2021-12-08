package QueryImplementation;

import authentication.model.Session;
import exceptions.ExceptionHandler;
import loggers.GeneralLogger;
import loggers.QueryLogger;
import services.DatabaseService;
import services.TableService;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQuery {
    private String dataStoragePath = DatabaseService.getRootDatabaseFolderPath();
    public static Instant instant = Instant.now();

    //insert query
    //insert into table values ()
    public Boolean insertQuery(String query, String directoryPath) throws IOException, ExceptionHandler {
        String database = "";
        String onlyTableName = "";
        String tableName = "";
        String tablePath;
        String providedColumnsStr = null;
        String providedColumns = null;
        String providedValuesStr = null;
        String providedValues = null;

        //segregating logic from query
        String tempString = QueryOperations.removeSemiColon(query.substring(query.indexOf("insert into") + 11));
        String[] tempArray = tempString.split(" ");

        //Extracting values from query
        if(!tempArray[1].contains("values")){
            Pattern patternColumns = Pattern.compile("(?<=into)(.*)(?=values)", Pattern.DOTALL);
            Matcher matcherColumns = patternColumns.matcher(query);
            while (matcherColumns.find()) {
                providedColumnsStr = matcherColumns.group(0).trim();
            }
            //System.out.println("Provided values: "+providedColumnsStr);
            String tempString2 = QueryOperations.removeSemiColon(providedColumnsStr
                    .substring(providedColumnsStr.indexOf("(") + 1).trim());
            //System.out.println("tempString2->" + tempString2);
            providedColumns = tempString2.substring(0, tempString2.length() - 1).trim()
                    .replaceAll("\"","")
                    .replaceAll("'","");
            System.out.println("providedColumns->" + providedColumns);
        }

        //Extracting values from query
        Pattern patternValues = Pattern.compile("(?<=values).*$", Pattern.DOTALL);
        Matcher matcherValues = patternValues.matcher(query);
        while (matcherValues.find()) {
            providedValuesStr = matcherValues.group(0).trim();
        }
        //System.out.println("Provided values: "+providedValuesStr);

        String tempString3 = QueryOperations.removeSemiColon(providedValuesStr
                .substring(providedValuesStr.indexOf("(") + 1).trim());
        //System.out.println("tempString2->" + tempString3);
        providedValues = tempString3.substring(0, tempString3.length() - 1).trim()
                .replaceAll("\"","")
                .replaceAll("'","");
        //System.out.println("providedValues->" + providedValues);

        //Extracting table name
        tableName = tempArray[0];
        if (tableName.contains(".")){
            database=tableName.split("\\.")[0];
            onlyTableName=tableName.split("\\.")[1];
            tablePath = dataStoragePath + database + "/" + onlyTableName + "/";
        }
        else{
            //System.out.println("directoryPath ->" + directoryPath);
            tablePath = directoryPath + "/" + tableName;
        }
        //System.out.println("tablePath ->" + tablePath);

        File filePath = new File(tablePath);
        if (!tableName.isEmpty() || !onlyTableName.isEmpty()){
            System.out.println("tablePath->" + tablePath);
            Boolean fileExist = filePath.isFile();
            if(!fileExist){
                System.out.println("Table does not exist");
                return false;
            }
        }
        else if(directoryPath.isEmpty()){
            System.out.println("Database not selected, please select database before inserting any values");
            String logMessage = "Database was not selected while inserting  " + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Insert  " , Session.getInstance().getUser().getName() ,
                    database,tableName,query ,"Failure " , instant);
            throw new ExceptionHandler(logMessage);
        }

        Boolean insertData = validateWriteFile(tablePath, providedColumns, providedValues);

        if(insertData) {
            System.out.println("Insert successful for " + tableName);
            String logMessage = "Insert successful for " + tableName + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Insert  " , Session.getInstance().getUser().getName() ,
                    database,tableName,query ,"Success " , instant);
            return true;
        }
        else {
            System.out.println("Insert not successful");
            String logMessage = "Insert not successful  " + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Insert  " , Session.getInstance().getUser().getName() ,
                    database,tableName,query ,"Failure " , instant);
            throw new ExceptionHandler(logMessage);
        }
    }

    public Boolean validateWriteFile(String path, String passedColumns, String passedValues) throws IOException {
        HashMap<String, String> columnValueMap = new HashMap<String, String>();
        String[] pathList = path.split("/");
        String tableName = pathList[2];
        StringBuilder builder = new StringBuilder();

        List<String> providedColumnsList = null;
        List<String> providedValuesList = Arrays.asList(passedValues.replaceAll(" ","").split(","));
        System.out.println("providedValuesList->" + providedValuesList);

        // read header from file
        File filePath = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String header = br.readLine();
        List<String> headerColsList = Arrays.asList(header.replaceAll(" ","").split(","));
        System.out.println("headerColsList->" + headerColsList);
        int totalColumnsLen = headerColsList.size();

        //Specific columns defined if condition
        if(passedColumns!=null) {
            providedColumnsList = Arrays.asList(passedColumns.replaceAll(" ","").split(","));
            System.out.println("providedColumnsList->" + providedColumnsList);

            if(providedValuesList.size() != providedColumnsList.size()) {
                System.out.println("Please enter correct number of columns and values in query");
                return false;
            }
        }
        //Assigning value to columnsList to create map of column with value
        List<String> columnsList;
        if(passedColumns!=null && providedColumnsList.size()>1) {
            columnsList = providedColumnsList;
        }
        else{
            columnsList = headerColsList;
        }
        System.out.println("columnsList-" + columnsList);
        //Putting data into map
        for (int i = 0; i < columnsList.size(); i++) {
            columnValueMap.put(columnsList.get(i), providedValuesList.get(i));
        }
        System.out.println("columnValueMap--" + columnValueMap);
        //Method to get value from TableService.getPrimaryKey to get primary key from schema
        String primaryKeyColumn=null;
        String primaryKeyValue=null;
        try{
            primaryKeyColumn = TableService.getPrimaryKey(dataStoragePath + pathList[1],tableName);
            if(primaryKeyColumn != null) {
                primaryKeyValue = columnValueMap.get(primaryKeyColumn);
            }
        } catch (ExceptionHandler exceptionHandler) {
            exceptionHandler.printStackTrace();
        }

        Boolean primaryKeyCheckBol = false;
        QueryOperations selectObj = new QueryOperations();
        if(primaryKeyColumn != null && primaryKeyValue != null) {
            ArrayList<String> valuesList = selectObj.selectTableQuery("select " + primaryKeyColumn + " from " +
                                            pathList[1] + "." + pathList[2] + ";" , "PK_CHECK");
            System.out.println("PRIMARY KEY -----" + valuesList.contains(primaryKeyValue));
            primaryKeyCheckBol = valuesList.contains(primaryKeyValue);
        }

        if(primaryKeyCheckBol){
            System.out.println("Value already exists with Primary key - " + primaryKeyValue);
            return false;
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));

        if(passedColumns == null){
            if(headerColsList.size() == providedValuesList.size()) {
                builder.append(passedValues);
            }
            else{
                System.out.println("Please provide correct number of arguments in string");
                return false;
            }
        }
        else{
            ArrayList<String> headerColsArrayList = new ArrayList<String>(headerColsList);
            ArrayList<String> providedColumnsArrayList = new ArrayList<String>(providedColumnsList);

            for(String s : headerColsArrayList){
                int getIndex = providedColumnsArrayList.indexOf(s);
                //System.out.println(getIndex);
                if(getIndex == -1){
                    builder.append("null,");
                } else {
                    builder.append(columnValueMap.get(s) + ",");
                }
            }
        }

        String finalString = builder.toString();
        //System.out.println("builder->" + builder);
        bw.append(finalString.substring(0, finalString.length() - 1));
        bw.append(System.lineSeparator());
        bw.close();

        return true;
    }

/*
    public static void main(String[] args) throws ExceptionHandler, IOException {
        String userArgument = null;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Query-------");
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        System.out.println("Input query is:" + userArgument);
        InsertQuery insertObj = new InsertQuery();
        insertObj.insertQuery(userArgument,null);
    }
    */
}