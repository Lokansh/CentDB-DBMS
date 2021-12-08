package QueryImplementation;

import authentication.model.Session;
import exceptions.ExceptionHandler;
import loggers.QueryLogger;
import services.DatabaseService;
import services.TableService;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DatabaseNotSpecifiedException extends Exception {
}

class DatabaseNotFoundException extends Exception {
}

public class UpdateQuery {
    // update table db1.tb1 set name="updated" where id=1
    public void updateQuery(String Query, String globalPath) {
        String databaseName;
        String tableName;
        constants_QI constant = new constants_QI();
        Pattern dbTBPattern = Pattern.compile(constant.UPD_QUERY_DBTB_NAME, Pattern.DOTALL);
        Matcher dbTBMatcher = dbTBPattern.matcher(Query);

        try {
            if (dbTBMatcher.find() && (globalPath == null || globalPath.isEmpty() || globalPath.isBlank())) {
                // extract database name and table name from query provided
                String extractedStr = dbTBMatcher.group(0).trim();
                if (extractedStr.contains(".")) {
                    databaseName = extractedStr.split(constant.DOT_OPR)[0];
                    tableName = dbTBMatcher.group(0).trim().split(constant.DOT_OPR)[1];
                } else {
                    // raise exception
                    throw new DatabaseNotSpecifiedException();
                }
            } else {
                // if database not specified in the query
                Pattern dbPattern = Pattern.compile(constant.EXTRACT_DB_FROM_GLOBALPATH, Pattern.DOTALL);
                Matcher dbMatcher = dbPattern.matcher(globalPath);
                if (dbMatcher.find()) {
                    databaseName = dbMatcher.group(0).trim();
                } else {
                    throw new DatabaseNotFoundException();
                }
                tableName = dbTBMatcher.group(0).trim();
            }
            // call main update logic
            performUpdateLogic(Query, databaseName, tableName);
        } catch (DatabaseNotSpecifiedException e) {
            System.out.println("DatabaseNotSpecifiedException Raised. Please specify the database name.");
        } catch (DatabaseNotFoundException e) {
            System.out.println("DatabaseNotFoundException Raised.");
        }
    }


    public void performUpdateLogic(String query, String databaseName, String tableName) {
        try {
            String line;
            String filterColumn = null;
            String filterValue = null;
            String updateColumn = null;
            String updateValue = null;
//            String updatePAth = dataStoragePath + databaseName + "/" + tableName;
//            String tempPath = dataStoragePath + databaseName + "/" + tableName + "_temp";
            String updatePAth = DatabaseService.CURRENT_DATABASE_PATH + "/" + tableName;
            String tempPath = DatabaseService.CURRENT_DATABASE_PATH + "/" + tableName + "_temp";

            int indexFilterCol = 0;
            int updateColIndex = 0;
            int rowCounter = 0;

            constants_QI constant = new constants_QI();
            File file = new File(updatePAth);

            // extract set clause
            Pattern setPattern = Pattern.compile(constant.SET_CLAUSE, Pattern.DOTALL);
            Matcher setMatcher = setPattern.matcher(query);
            if (setMatcher.find()) {
                updateColumn = setMatcher.group(0).trim().split(constant.EQUAL_OPR)[0];
                updateValue = setMatcher.group(0).trim().split(constant.EQUAL_OPR)[1].replaceAll("\"", "");
            }

            // extract where clause
            Pattern wherePattern = Pattern.compile(constant.WHERE_CLAUSE, Pattern.DOTALL);
            Matcher whereMatcher = wherePattern.matcher(query);
            if (whereMatcher.find()) {
                filterColumn = whereMatcher.group(0).trim().split(constant.EQUAL_OPR)[0];
                filterValue = whereMatcher.group(0).trim().split(constant.EQUAL_OPR)[1]
                        .replaceAll("\"", "").replaceAll("'", "")
                        .replace(";", "");
            }

            // PRIMARY KEY CHECK
            // update db1.tb1 set id=5 where id=1;
            // update db1.tb1 set name=5 where id=1;
            QueryOperations obj = new QueryOperations();
            String primaryKeyColumn = null;
            Boolean PkCheck = false;
            try{
                primaryKeyColumn = TableService.getPrimaryKey(DatabaseService.getRootDatabaseFolderPath() + databaseName, tableName);
            } catch (ExceptionHandler exceptionHandler) {
                exceptionHandler.printStackTrace();
            }
            if(primaryKeyColumn.equals(updateColumn)) {
                ArrayList<String> valuesList = obj.selectTableQuery("select " + updateColumn + " from " + databaseName + "." + tableName + ";", "PK_CHECK");
                System.out.println(valuesList.contains(updateValue));
                PkCheck = valuesList.contains(updateValue);
            }
            if (PkCheck) {
                throw new SQLIntegrityConstraintExceptionRaised("SQLIntegrityConstraintExceptionRaised. There exists a row with the same Primary key column value.");
            } else {
                // if file path exists
//            BufferedReader br = new BufferedReader(new FileReader(updatePAth));
//            BufferedWriter bw = new BufferedWriter(new FileWriter(tempPath));
                if (file.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(updatePAth));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(tempPath));
                    while (((line = br.readLine()) != null)) {
                        if (rowCounter == 0) {
                            ArrayList<String> header = new ArrayList<>(Arrays.asList(line.split(constant.COMMA_OPR)));
                            indexFilterCol = header.indexOf(filterColumn);
                            updateColIndex = header.indexOf(updateColumn);
                            bw.append(line);
                            bw.newLine();
                            rowCounter++;
                        } else {
                            // row data's
                            rowCounter++;
                            String extractedValueOfRow = line.split(constant.COMMA_OPR)[indexFilterCol];
                            if (extractedValueOfRow.equals(filterValue)) {               // check if filter matches
                                // this is the row that is to be updated
                                ArrayList<String> namesList = new ArrayList<>(Arrays.asList(line.split(constant.COMMA_OPR)));
                                String oldValue = namesList.get(updateColIndex);        // for loggers
                                namesList.set(updateColIndex, updateValue);             // update the value
                                String listString = String.join(constant.COMMA_OPR, namesList);
                                bw.append(listString);
                                bw.newLine();
                            } else {
                                bw.append(line);
                                bw.newLine();
                            }
                        }
                    } // end of while
                    bw.close();
                    br.close();
                } else {
                    QueryLogger.logQueryData("Update", Session.getInstance().getUser().getName(), databaseName, tableName, query, "Failure", Instant.now());
                    System.out.println("Path does not exists.");
                    return;
                }
                // delete the actual data file and renaming the temp file to actual file
                file.delete();
                File newfile = new File(tempPath);
                newfile.renameTo(file);
                QueryLogger.logQueryData("Update", Session.getInstance().getUser().getName(), databaseName, tableName, query, "Success", Instant.now());
                System.out.println("Updated successfully.");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
class SQLIntegrityConstraintExceptionRaised extends Exception{
    SQLIntegrityConstraintExceptionRaised(String message){
        System.out.println(message);
    }
}

