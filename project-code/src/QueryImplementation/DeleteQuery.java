package QueryImplementation;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteQuery {
    final String dataStoragePath = "database_storage/";

    public Boolean deleteQuery(String query, String directoryPath) throws IOException {
        constants_QI constant = new constants_QI();
        String database = "";
        String onlyTableName = "";
        String tableName = "";
        String tablePath;
        String filterColumn = null;
        String filterValue = null;
        String line;


        //segregating logic from query
        String tempString = QueryOperations.removeSemiColon(query.substring(query.indexOf("delete from") + 11));
        String[] tempArray = tempString.split(" ");
        tableName = tempArray[0];

        //Extracting database and table name
        if (tableName.contains(".")){
            database=tableName.split("\\.")[0];
            onlyTableName=tableName.split("\\.")[1];
            tablePath = dataStoragePath + database + "/" + onlyTableName + "/";
        }
        else{
            tablePath = directoryPath + "/" + tableName;
        }

        System.out.println("tablePath ->" + tablePath);

        // extracting where
        Pattern wherePattern = Pattern.compile(constant.WHERE_CLAUSE, Pattern.DOTALL);
        Matcher whereMatcher = wherePattern.matcher(query);
        if (whereMatcher.find()){
            filterColumn = whereMatcher.group(0).trim().split(constant.EQUAL_OPR)[0];
            filterValue = whereMatcher.group(0).trim().split(constant.EQUAL_OPR)[1]
                    .replaceAll("\"","").replaceAll("'","")
                    .replace(";","");
        }

        if (!tableName.isEmpty() || !onlyTableName.isEmpty()) {
            File filePath = new File(tablePath);
            String tempFilePath = tablePath.substring(0, tablePath.length() - 1) + "_temp";
            BufferedReader bReader = new BufferedReader(new FileReader(tablePath));
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(tempFilePath));

            if(filePath.exists()){
                while((line = bReader.readLine()) != null){
                    if(line.contains(filterValue)){
                        continue;
                    }
                    else{
                        bWriter.append(line);
                        bWriter.newLine();
                    }
                }
                bReader.close();
                bWriter.close();
                filePath.delete();
                File newfile = new File(tempFilePath);
                newfile.renameTo(filePath);
                System.out.println("Delete successful");
            }
            else{
                System.out.println("Table file does not exist.");
            }
        }

        return false;
    }
}
