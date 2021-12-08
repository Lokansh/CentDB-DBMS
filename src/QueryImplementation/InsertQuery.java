package QueryImplementation;

import authentication.model.Session;
import exceptions.ExceptionHandler;
import loggers.GeneralLogger;
import loggers.QueryLogger;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQuery {
    private String dataStoragePath = "database_storage/";
    public static Instant instant = Instant.now();

    //insert query
    //insert into table values ()
    public Boolean insertQuery(String query, String directoryPath) throws IOException, ExceptionHandler {
        String database = "";
        String onlyTableName = "";
        String tableName = "";
        String tablePath;
        String schemaPath;
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
            System.out.println("Provided values: "+providedColumnsStr);
            String tempString2 = QueryOperations.removeSemiColon(providedColumnsStr
                    .substring(providedColumnsStr.indexOf("(") + 1).trim());
            System.out.println("tempString2->" + tempString2);
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
        System.out.println("Provided values: "+providedValuesStr);

        String tempString3 = QueryOperations.removeSemiColon(providedValuesStr
                                .substring(providedValuesStr.indexOf("(") + 1).trim());
        System.out.println("tempString2->" + tempString3);
        providedValues = tempString3.substring(0, tempString3.length() - 1).trim()
                .replaceAll("\"","")
                .replaceAll("'","");
        System.out.println("providedValues->" + providedValues);

        //Extracting table name
        tableName = tempArray[0];
        if (tableName.contains(".")){
            database=tableName.split("\\.")[0];
            onlyTableName=tableName.split("\\.")[1];
            tablePath = dataStoragePath + database + "/" + onlyTableName + "/";
            schemaPath = dataStoragePath + database + "/" + database + "_" + "schema";
        }
        else{
            System.out.println("directoryPath ->" + directoryPath);
            tablePath = directoryPath + "/" + tableName;
            schemaPath = directoryPath + database + "_" + "schema";
        }
        System.out.println("tablePath ->" + tablePath);

        File filePath = new File(tablePath);
        if (!tableName.isEmpty() || !onlyTableName.isEmpty()){
            System.out.println("tablePath->" + tablePath);
            Boolean fileExist = filePath.isFile();
            if(!fileExist){
                System.out.println("Table does not exist");
                return false;
            }
            /*
            System.out.println("File created or file exists operation complete");
            String logMessage = "File created or file exists operation complete  " + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Insert  " , Session.getInstance().getUser().getName() ,
                    database,tableName,query ,"Success " , instant);

             */
        }
        /*
        else if(directoryPath.isEmpty()){
            System.out.println("Database not selected, please select database before inserting any values");
            String logMessage = "Database was not selected while inserting  " + " | " +
                    "Time of Execution: " + instant + "ms";
            QueryLogger.logQueryData("Insert  " , Session.getInstance().getUser().getName() ,
                    database,tableName,query ,"Failure " , instant);
            throw new ExceptionHandler(logMessage);
            //return false;
        }

         */

        Boolean insertData = validateWriteFile(tablePath, schemaPath, providedColumns, providedValues);

        if(insertData) {
            System.out.println("Insert successful for " + tableName);
            return true;
        }
        else {
            System.out.println("Insert not successful");
            return false;
        }
    }

    public Boolean validateWriteFile(String path, String schPath, String passedColumns, String passedValues) throws IOException {
        List<Integer> indexList = new ArrayList<>();
        List<String> appendData = new ArrayList<>();
        HashMap<String, String> columnValueMap = new HashMap<String, String>();
        String[] pathList = path.split("/");
        String tableName = pathList[2];
        String finalValuesStr=null;

        List<String> providedColumnsList = null;
        List<String> providedValuesList = Arrays.asList(passedValues.split(","));
        System.out.println("providedValuesList->" + providedValuesList);

        if(passedColumns!=null) {
            providedColumnsList = Arrays.asList(passedColumns.split(","));
            System.out.println("providedColumnsList->" + providedColumnsList);

            if(providedValuesList.size() == providedColumnsList.size()) {
                for (int i = 0; i < providedColumnsList.size(); i++) {
                    columnValueMap.put(providedColumnsList.get(i), providedValuesList.get(i));
                }
            }
            else{
                System.out.println("Please enter correct number of columns and values in query");
                return false;
            }
        }

        File filePath = new File(path);
        File schemaPath = new File(schPath);
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        BufferedReader bReadSchema = new BufferedReader(new FileReader(schemaPath));

        // read header from file
        String header = br.readLine();
        List<String> headerColsList = Arrays.asList(header.split(","));
        //int totalColumnsLen = headerColsList.size();


        if(passedColumns == null && (headerColsList.size() == providedValuesList.size())){
            finalValuesStr = passedValues;
        }
        else {
            for (String s : providedColumnsList) {
                int index = headerColsList.indexOf(s);
                indexList.add(index);
            }
            for(String head : headerColsList){
                System.out.println("Isnide 1 - " + finalValuesStr);
                for(String column : providedColumnsList){
                    System.out.println("Isnide 2 - " + finalValuesStr);
                    if(head.equals(column)){
                        System.out.println("Isnide 3 - " + finalValuesStr);
                        finalValuesStr += columnValueMap.get(column) + ",";
                        continue;
                    }
                    else{
                        System.out.println("Isnide 4 - " + finalValuesStr);
                        finalValuesStr += "null,";
                        continue;
                    }
                }
            }
        }

        bw.append(finalValuesStr);
        bw.append(System.lineSeparator());
        bw.close();

        return true;
    }

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
}
