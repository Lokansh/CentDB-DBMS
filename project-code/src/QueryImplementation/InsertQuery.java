package QueryImplementation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InsertQuery {
    private String dataStoragePath = "database_storage/";

    //insert query
    //insert into table values ()
    public Boolean insertQuery(String query, String directoryPath) throws IOException {
        String tableName = "";
        String columnValues = "";
        String onlyTableName = "";
        String database = "";
        String tablePath;

        //Validate query logic
        //Regex needs to written

        //segregating logic from query
        if (query.contains("insert into")) {
            String tempString = QueryOperations.removeSemiColon(query.substring(query.indexOf("into") + 4));
            String[] tempArray = tempString.split(" ");
            tableName = tempArray[0];
        }
        else{
            System.out.println("Not an Insert query");
            return false;
        }

        if (tableName.contains(".")){
            database=tableName.split("\\.")[0];
            onlyTableName=tableName.split("\\.")[1];
            tablePath = dataStoragePath + database + "/" + onlyTableName + "/";
        }
        else{
            System.out.println("directoryPath ->" + directoryPath);
            tablePath = directoryPath + "/" + tableName;
        }

        System.out.println("tablePath ->" + tablePath);

        File filePath = new File(tablePath);

        if (!tableName.isEmpty() || !onlyTableName.isEmpty()){
            System.out.println("tablePath->" + tablePath);

            Boolean fileExist = filePath.isFile();
            if(fileExist){
                System.out.println("Table exist");
            }
            else{
                Boolean fileCreatedSuccess = filePath.createNewFile();
                System.out.println("Table does not exist so table file created -->" + fileCreatedSuccess);
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

        String tempString2 = QueryOperations.removeSemiColon(query.substring(query.indexOf("(") + 1).trim());
        System.out.println("tempString2->" + tempString2);
        columnValues = tempString2.substring(0, tempString2.length() - 1).trim();
        System.out.println("columnValues->" + columnValues);

        List<String> columnValuesList = Arrays.asList(columnValues.split(","));
        System.out.println("columnValuesList->" + columnValuesList);

        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
        bw.append(columnValues);
        bw.append(System.lineSeparator());
        bw.close();

        return false;
    }
}
